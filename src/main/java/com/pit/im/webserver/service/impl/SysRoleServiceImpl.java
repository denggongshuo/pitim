package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysRoleDao;
import com.pit.im.webserver.service.SysRoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysRoleEntity;


@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {


}