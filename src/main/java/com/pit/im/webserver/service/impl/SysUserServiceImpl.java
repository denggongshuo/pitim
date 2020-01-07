package com.pit.im.webserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pit.im.webserver.dao.SysUserDao;
import com.pit.im.webserver.entity.ext.UserExt;
import com.pit.im.webserver.service.SysMenuService;
import com.pit.im.webserver.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysUserEntity;

import java.util.Set;


@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysMenuService menuService;

    @Override
    public UserExt getUserExt(String loginName) {
        SysUserEntity userEntity = getOne(new QueryWrapper<SysUserEntity>()
                .eq("login_name", loginName)
                .eq("del_flag", "0"));
        if (null == userEntity) {
            return null;
        }
        UserExt userExt = new UserExt();
        BeanUtil.copyProperties(userEntity, userExt);
        Set<String> perms = menuService.getPermsByUserId(userEntity.getUserId());
        userExt.setPermissions(perms);
        return userExt;
    }
}