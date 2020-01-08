package com.pit.im.webserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pit.im.utils.RequestUtils;
import com.pit.im.webserver.dao.SysUserDao;
import com.pit.im.webserver.entity.ext.UserExt;
import com.pit.im.webserver.service.SysMenuService;
import com.pit.im.webserver.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysUserEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysMenuService menuService;

    @Override
    public IPage getPage() {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        IPage<SysUserEntity> page = page(new Page<>());
        return page;
    }

    @Override
    public UserExt getUserExt(String loginName) {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
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