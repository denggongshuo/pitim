package com.pit.im.auth.domain;

import lombok.Data;

/**
 * @author deng
 * @date 2020/1/7 10:47
 */
@Data
public class AuthToken {
    String access_token;//访问token就是短令牌，用户身份令牌

    String refresh_token;//刷新token

    String jwt_token;//jwt令牌

}
