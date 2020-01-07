package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 定时任务调度日志表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("sys_job_log")
public class SysJobLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务日志ID
	 */
	@TableId
	private Long jobLogId;
	/**
	 * 任务名称
	 */
	private String jobName;
	/**
	 * 任务组名
	 */
	private String jobGroup;
	/**
	 * 任务方法
	 */
	private String methodName;
	/**
	 * 方法参数
	 */
	private String methodParams;
	/**
	 * 日志信息
	 */
	private String jobMessage;
	/**
	 * 执行状态（0正常 1失败）
	 */
	private String status;
	/**
	 * 异常信息
	 */
	private String exceptionInfo;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
