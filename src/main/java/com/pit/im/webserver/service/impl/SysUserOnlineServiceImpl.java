package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysUserOnlineDao;
import com.pit.im.webserver.service.SysUserOnlineService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysUserOnlineEntity;


@Service("sysUserOnlineService")
public class SysUserOnlineServiceImpl extends ServiceImpl<SysUserOnlineDao, SysUserOnlineEntity> implements SysUserOnlineService {


}