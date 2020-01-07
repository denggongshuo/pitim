package com.pit.im.webserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pit.im.webserver.entity.SysMenuEntity;

import java.util.Map;
import java.util.Set;

/**
 * 菜单权限表
 *
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 根据userid查询拥有的菜单权限
     * @param userId
     * @return
     */
    Set<String> getPermsByUserId(Long userId);
}

