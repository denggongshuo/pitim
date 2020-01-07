package com.pit.im.auth.serveice;


import cn.hutool.core.collection.CollUtil;
import com.pit.im.webserver.entity.ext.UserExt;
import com.pit.im.webserver.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private SysUserService userService;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {
            try {
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
                if (clientDetails != null) {
                    //密码
                    String clientSecret = clientDetails.getClientSecret();
                    return new User(username, clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                }
            } catch (ClientRegistrationException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //远程调用用户中心根据账号查询用户信息
        UserExt userExt = userService.getUserExt(username);
        if (userExt == null) {
            //返回空给spring security表示用户不存在
            return null;
        }


        //取出正确密码（hash值）
        String password = userExt.getPassword();

        //从数据库获取权限
        Set<String> permissions = userExt.getPermissions();
        if (CollUtil.isEmpty(permissions)) {
            permissions = new HashSet<>();
        }
        String user_permission_string = StringUtils.join(permissions.toArray(), ",");

        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));
        userDetails.setId(userExt.getUserId());
        userDetails.setName(userExt.getUserName());//用户名称

        return userDetails;
    }
}
