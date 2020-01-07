package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysLogininforDao;
import com.pit.im.webserver.service.SysLogininforService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysLogininforEntity;


@Service("sysLogininforService")
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforDao, SysLogininforEntity> implements SysLogininforService {


}