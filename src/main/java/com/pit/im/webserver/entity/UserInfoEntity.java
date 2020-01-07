package com.pit.im.webserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户信息表
 * 
 * @author deng
 * @email ${email}
 * @date 2020-01-06 13:37:51
 */
@Data
@TableName("user_info")
public class UserInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 部门
	 */
	private Long deptid;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 性别（0女 1男）
	 */
	private Integer sex;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 身份证
	 */
	private String cardid;
	/**
	 * 签名
	 */
	private String signature;
	/**
	 * 毕业院校
	 */
	private String school;
	/**
	 * 学历
	 */
	private Integer education;
	/**
	 * 现居住地址
	 */
	private String address;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 个人头像
	 */
	private String profilephoto;
	/**
	 * 创建时间
	 */
	private Date createdate;
	/**
	 * 创建人
	 */
	private Long createuser;
	/**
	 * 修改时间
	 */
	private Date updatedate;
	/**
	 * 修改人
	 */
	private Long updateuser;

}
