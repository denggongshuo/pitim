package com.pit.im.auth.controller;


import cn.hutool.core.codec.Base64;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.pit.im.auth.domain.AuthToken;
import com.pit.im.auth.serveice.AuthService;
import com.pit.im.exception.ErrorCode;
import com.pit.im.exception.REException;
import com.pit.im.utils.BCryptUtil;
import com.pit.im.utils.RequestUtils;
import com.pit.im.webserver.entity.ext.LoginFrom;
import com.pit.im.webserver.entity.ext.UserExt;
import com.pit.im.webserver.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/")
@Api("认证中心")
public class AuthController {

    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;


    @Autowired
    private AuthService authService;

    @Autowired
    private SysUserService userService;

    @PostMapping("/userlogin")
    @ApiOperation("登陆")
    public R login(@RequestBody LoginFrom loginFrom) {

        //账号
        String loginName = loginFrom.getLoginName();
        //密码
        String password = loginFrom.getPassword();

        UserExt userExt = userService.getUserExt(loginName);

        if (userExt == null) {
            throw new REException(ErrorCode.ACCOUNT_NOTEXITS);
        }
        if (!BCryptUtil.matches(password, userExt.getPassword())) {
            throw new REException(ErrorCode.PASSWORD_ERROR);
        }
        if ("1".equals(userExt.getStatus())) {
            throw new REException(ErrorCode.USER_FORBID);
        }

        //申请令牌
        AuthToken authToken = authService.login(loginName, password, clientId, clientSecret);

        //用户身份令牌
        String access_token = authToken.getAccess_token();

        String jwt_token = authToken.getJwt_token();
        String jsonStr = Base64.decodeStr(jwt_token.split("\\.")[1]);
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        Object userId = jsonObject.get("id");
        String name = (String) jsonObject.get("name");
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("access_token", access_token);
        map.put("userphotoUrl", userExt.getAvatar());

        return R.ok(map);

    }


    @PostMapping("/userlogout")
    @ApiOperation("退出")
    public R logout(HttpServletRequest request) {
        String accessToken = RequestUtils.getAccessToken(request);
        //删除redis中的token
        boolean result = authService.delToken(accessToken);

        return R.ok(null);

    }
}
