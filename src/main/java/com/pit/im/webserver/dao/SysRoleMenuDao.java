package com.pit.im.webserver.dao;

import com.pit.im.webserver.entity.SysRoleMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 角色和菜单关联表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Mapper
@Repository
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {

    /**
     * 根据useri查询菜单权限
     * @param userId
     * @return
     */
    Set<String> getPermsByUserId(Long userId);
}
