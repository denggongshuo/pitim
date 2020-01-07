package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysJobDao;
import com.pit.im.webserver.service.SysJobService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pit.im.webserver.entity.SysJobEntity;


@Service("sysJobService")
public class SysJobServiceImpl extends ServiceImpl<SysJobDao, SysJobEntity> implements SysJobService {


}