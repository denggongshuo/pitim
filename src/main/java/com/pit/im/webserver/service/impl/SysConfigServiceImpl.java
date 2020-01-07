package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysConfigDao;
import com.pit.im.webserver.entity.SysConfigEntity;
import com.pit.im.webserver.service.SysConfigService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {



}