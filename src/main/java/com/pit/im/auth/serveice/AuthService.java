package com.pit.im.auth.serveice;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.pit.im.auth.domain.AuthToken;
import com.pit.im.exception.ErrorCode;
import com.pit.im.exception.REException;
import com.pit.im.webserver.entity.ext.UserExt;
import com.pit.im.webserver.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
@Slf4j
public class AuthService {


    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    private final static String ACCESS_TOKEN = "access_token";

    private final static String AUTHORIZATION = "Authorization";

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String context_path;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SysUserService userService;

    @Autowired
    RestTemplate restTemplate;


    //用户认证申请令牌，将令牌存储到redis
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        Long userId = this.getUserId(username);
        if (null == userId) {
            throw new REException(ErrorCode.ACCOUNT_NOTEXITS);
        }
        //请求spring security申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if (authToken == null) {
            throw new REException(ErrorCode.APPLY_TOKEN_FAIL);

        }
        //用户身份令牌
        String access_token = authToken.getAccess_token();
        //存储到redis中的内容
        String authTokenStr = JSON.toJSONString(authToken);
        boolean result = this.saveToken(access_token, authTokenStr, tokenValiditySeconds);
        if (!result) {
            throw new REException(ErrorCode.LOGIN_FAIL);
        }
        return authToken;

    }


    Long getUserId(String loginName) {
        UserExt userext = userService.getUserExt(loginName);
        if (null == userext) {
            return null;
        }
        return userext.getUserId();
    }

    /**
     * @param access_token 用户身份令牌
     * @param content      内容就是AuthToken对象的内容
     * @param ttl          过期时间
     * @return
     */
    public boolean saveToken(String access_token, String content, long ttl) {

        //保存新的用户信息
        String key = ACCESS_TOKEN + ":" + access_token;
        stringRedisTemplate.boundValueOps(key).set(content, ttl, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire > 0;
    }

    public String getJwtToken(String access_token) {
        String key = ACCESS_TOKEN + ":" + access_token;
        return stringRedisTemplate.opsForValue().get(key);
    }

    //删除token
    public boolean delToken(String access_token) {

        String key = ACCESS_TOKEN + ":" + access_token;

        stringRedisTemplate.delete(key);

        return !stringRedisTemplate.hasKey(key);
    }


    //申请令牌
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {

        String path = context_path.equals("/") ? "" : context_path;
        //令牌申请的地址 http://localhost:port/context-path/auth/oauth/token
        String authUrl = "http://localhost:" + port + path + "/oauth/token";
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add(AUTHORIZATION, httpBasic);

        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);

        //申请令牌信息
        Map bodyMap = exchange.getBody();
        if (bodyMap == null ||
                bodyMap.get("access_token") == null ||
                bodyMap.get("refresh_token") == null ||
                bodyMap.get("jti") == null) {

            //解析spring security返回的错误信息
            if (bodyMap != null && bodyMap.get("error_description") != null) {
                String error_description = (String) bodyMap.get("error_description");
                if (error_description.indexOf("UserDetailsService returned null") >= 0) {
                    throw new REException(ErrorCode.ACCOUNT_NOTEXITS);
                } else if (error_description.indexOf("坏的凭证") >= 0) {
                    throw new REException(ErrorCode.APPLY_TOKEN_FAIL);
                }
            }

            return null;
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) bodyMap.get("jti"));//用户身份令牌
        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));//刷新令牌
        authToken.setJwt_token((String) bodyMap.get("access_token"));//jwt令牌

        return authToken;
    }


    //获取httpbasic的串
    private String getHttpBasic(String clientId, String clientSecret) {
        String string = clientId + ":" + clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
