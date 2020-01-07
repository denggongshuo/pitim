package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysOperLogDao;
import com.pit.im.webserver.service.SysOperLogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysOperLogEntity;


@Service("sysOperLogService")
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogDao, SysOperLogEntity> implements SysOperLogService {


}