package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户帐号
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("user_account")
public class UserAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 帐号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 禁用状态（0 启用  1 禁用）
	 */
	private Integer disablestate;
	/**
	 * 是否删除（0未删除1已删除）
	 */
	private Integer isdel;
	/**
	 * 创建日期
	 */
	private Date createdate;
	/**
	 * 修改日期
	 */
	private Date updatedate;
	/**
	 * 修改人
	 */
	private Long updateuser;

}
