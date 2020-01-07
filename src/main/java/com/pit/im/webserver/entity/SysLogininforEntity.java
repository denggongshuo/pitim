package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统访问记录
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("sys_logininfor")
public class SysLogininforEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 访问ID
	 */
	@TableId
	private Long infoId;
	/**
	 * 登录账号
	 */
	private String loginName;
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
	 * 登录状态（0成功 1失败）
	 */
	private String status;
	/**
	 * 提示消息
	 */
	private String msg;
	/**
	 * 访问时间
	 */
	private Date loginTime;

}
