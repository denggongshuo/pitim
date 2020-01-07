package com.pit.im.webserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.dao.SysJobLogDao;
import com.pit.im.webserver.service.SysJobLogService;
import org.springframework.stereotype.Service;
import com.pit.im.webserver.entity.SysJobLogEntity;


@Service("sysJobLogService")
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogDao, SysJobLogEntity> implements SysJobLogService {


}