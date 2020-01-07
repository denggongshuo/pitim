package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysPostDao;
import com.pit.im.webserver.service.SysPostService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysPostEntity;


@Service("sysPostService")
public class SysPostServiceImpl extends ServiceImpl<SysPostDao, SysPostEntity> implements SysPostService {


}