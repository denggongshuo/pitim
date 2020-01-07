package com.pit.im.webserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pit.im.webserver.entity.UserMessageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface UserMessageDao extends BaseMapper<UserMessageEntity> {
	
}
