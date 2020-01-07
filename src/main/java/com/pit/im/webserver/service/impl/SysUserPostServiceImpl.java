package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysUserPostDao;
import com.pit.im.webserver.service.SysUserPostService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pit.im.webserver.entity.SysUserPostEntity;


@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao, SysUserPostEntity> implements SysUserPostService {



}