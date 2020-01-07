package com.pit.im.webserver.dao;

import com.pit.im.webserver.entity.SysNoticeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface SysNoticeDao extends BaseMapper<SysNoticeEntity> {
	
}
