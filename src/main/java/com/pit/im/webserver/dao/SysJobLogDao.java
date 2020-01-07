package com.pit.im.webserver.dao;

import com.pit.im.webserver.entity.SysJobLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务调度日志表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface SysJobLogDao extends BaseMapper<SysJobLogEntity> {
	
}
