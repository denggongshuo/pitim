package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 部门
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("user_department")
public class UserDepartmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 部门人数
	 */
	private Integer count;
	/**
	 * 等级
	 */
	private Integer level;
	/**
	 * 上级部门ID
	 */
	private Long parentid;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createdate;
	/**
	 * 修改时间
	 */
	private Date updatedate;
	/**
	 * 修改人
	 */
	private Long updateuser;
	/**
	 * 是否删除（0否1是）
	 */
	private Integer isdel;

}
