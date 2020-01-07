package com.pit.im.webserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pit.im.webserver.entity.UserAccountEntity;


import java.util.Map;

/**
 * 用户帐号
 *
 * @author deng
 * @email
 * @date 2020-01-06 13:37:51
 */
public interface UserAccountService extends IService<UserAccountEntity> {


    boolean login(Map<String, Object> params);
}

