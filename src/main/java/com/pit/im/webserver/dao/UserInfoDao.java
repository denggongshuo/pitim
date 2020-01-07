package com.pit.im.webserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pit.im.webserver.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface UserInfoDao extends BaseMapper<UserInfoEntity> {
	
}
