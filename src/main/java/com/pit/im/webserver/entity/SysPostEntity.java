package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 岗位信息表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("sys_post")
public class SysPostEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 岗位ID
	 */
	@TableId
	private Long postId;
	/**
	 * 岗位编码
	 */
	private String postCode;
	/**
	 * 岗位名称
	 */
	private String postName;
	/**
	 * 显示顺序
	 */
	private Integer postSort;
	/**
	 * 状态（0正常 1停用）
	 */
	private String status;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;

}
