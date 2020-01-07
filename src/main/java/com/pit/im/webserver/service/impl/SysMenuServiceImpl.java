package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysMenuDao;
import com.pit.im.webserver.dao.SysRoleMenuDao;
import com.pit.im.webserver.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysMenuEntity;

import java.util.Set;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

    @Autowired
    private SysRoleMenuDao roleMenuDao;

    @Override
    public Set<String> getPermsByUserId(Long userId) {
        Set<String> perms = roleMenuDao.getPermsByUserId(userId);
        return perms;
    }
}