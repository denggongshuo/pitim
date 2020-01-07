package com.pit.im.auth.serveice;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * @Author: deng
 * @Date: 2019/9/16 16:40
 * @Version 1.0
 */
@Data
public class UserJwt extends User {

    private Long id;

    private String name;


    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserJwt(String username, String password, boolean enabled,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled,
                true,
                true,
                true, authorities);
    }


}
