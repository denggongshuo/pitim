package com.pit.im.webserver.dao;

import com.pit.im.webserver.entity.SysDeptEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {
	
}
