package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.SysDeptDao;
import com.pit.im.webserver.service.SysDeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pit.im.webserver.entity.SysDeptEntity;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {


}