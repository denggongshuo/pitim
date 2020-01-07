package com.pit.im.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: deng
 * @Date: 2019/9/23 11:02
 * @Version 1.0
 */

@Component
public class RequestUtils {

    private final static String JWT_KOKRN = "OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE";

    public static String getJwtToken(HttpServletRequest request) {
        return (String) request.getAttribute(JWT_KOKRN);
    }


    //解析jwtToken
    public static Map<String, String> parseJwtToken(HttpServletRequest request) {

        String jwtToken = (String) request.getAttribute(JWT_KOKRN);
        if (null == jwtToken){
            return new HashMap<>();
        }
        Map<String, String> map = null;
        try {
            if (StringUtils.isNotEmpty(jwtToken)) {
                String[] arr =jwtToken.split("\\.");
                String str = Base64.decodeStr(arr[1], CharsetUtil.UTF_8);
                map = (Map<String, String>) JSON.parse(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
        return map;
    }


    //从jwttoken中获取头
    public static String getUserId(HttpServletRequest request) {
        Map<String, String> map = parseJwtToken(request);
        if (CollUtil.isEmpty(map)){
            return "not find userId";
        }
        return map.get("id");
    }

    //获取用户账号
    public static String getUsername(HttpServletRequest request) {
        Map<String, String> map = parseJwtToken(request);
        if (CollUtil.isEmpty(map)){
            return "not find username";
        }
        return map.get("user_name");
    }

    //获取用户名
    public static String getName(HttpServletRequest request) {
        Map<String, String> map = parseJwtToken(request);
        if (CollUtil.isEmpty(map)){
            return "not dind name";
        }
        return map.get("name");
    }

    //获取过期时间
    public static String getExpTime(HttpServletRequest request) {
        Map<String, String> map = parseJwtToken(request);
        if (CollUtil.isEmpty(map)){
            return "-2";
        }
        return map.get("exp");
    }

    //获取身份令牌
    public static String getAccessToken(HttpServletRequest request) {
        Map<String, String> map = parseJwtToken(request);
        if (CollUtil.isEmpty(map)){
            return "not find jtl";
        }
        return map.get("jti");
    }

}



