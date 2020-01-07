package com.pit.im.webserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pit.im.webserver.dao.UserAccountDao;
import com.pit.im.webserver.entity.UserAccountEntity;
import com.pit.im.webserver.service.UserAccountService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;


@Service("userAccountService")
public class UserAccountServiceImpl extends ServiceImpl<UserAccountDao, UserAccountEntity> implements UserAccountService {
    @Override
    public boolean login(Map<String, Object> params) {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        int i = super.count(new QueryWrapper<UserAccountEntity>()
                .eq("account", username)
                .eq("password", password));
        return i > 0;
    }
}