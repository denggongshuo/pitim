package com.pit.im.webserver.dao;

import com.pit.im.webserver.entity.SysUserOnlineEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 在线用户记录
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface SysUserOnlineDao extends BaseMapper<SysUserOnlineEntity> {
	
}
