package com.pit.im.webserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pit.im.webserver.entity.SysUserEntity;
import com.pit.im.webserver.entity.ext.UserExt;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 根据账号查询用户信息（包括权限）
     * @param loginName
     * @return
     */
    UserExt getUserExt(String loginName);

    IPage getPage();

}

