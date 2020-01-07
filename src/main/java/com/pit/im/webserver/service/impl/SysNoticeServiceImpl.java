package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysNoticeDao;
import com.pit.im.webserver.service.SysNoticeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysNoticeEntity;


@Service("sysNoticeService")
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeDao, SysNoticeEntity> implements SysNoticeService {


}