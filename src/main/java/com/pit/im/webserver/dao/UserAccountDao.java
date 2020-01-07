package com.pit.im.webserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pit.im.webserver.entity.UserAccountEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户帐号
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface UserAccountDao extends BaseMapper<UserAccountEntity> {
	
}
