package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 在线用户记录
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("sys_user_online")
public class SysUserOnlineEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户会话id
	 */
	@TableId
	private String sessionid;
	/**
	 * 登录账号
	 */
	private String loginName;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 登录IP地址
	 */
	private String ipaddr;
	/**
	 * 登录地点
	 */
	private String loginLocation;
	/**
	 * 浏览器类型
	 */
	private String browser;
	/**
	 * 操作系统
	 */
	private String os;
	/**
	 * 在线状态on_line在线off_line离线
	 */
	private String status;
	/**
	 * session创建时间
	 */
	private Date startTimestamp;
	/**
	 * session最后访问时间
	 */
	private Date lastAccessTime;
	/**
	 * 超时时间，单位为分钟
	 */
	private Integer expireTime;

}
