package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.UserInfoDao;
import com.pit.im.webserver.entity.UserInfoEntity;
import com.pit.im.webserver.service.UserInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfoEntity> implements UserInfoService {



}