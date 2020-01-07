package com.pit.im.webserver.service.impl;

import com.pit.im.webserver.dao.UserDepartmentDao;
import com.pit.im.webserver.entity.UserDepartmentEntity;
import com.pit.im.webserver.service.UserDepartmentService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("userDepartmentService")
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentDao, UserDepartmentEntity> implements UserDepartmentService {


}