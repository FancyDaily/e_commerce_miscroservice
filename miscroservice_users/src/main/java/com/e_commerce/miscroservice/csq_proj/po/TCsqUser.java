package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:14
 */
@Table(commit = "从善桥用户表")
@Data
@Builder
public class TCsqUser  extends BaseEntity{
	@Id
	private Long id;

	@Transient
	private String uuid;

	@Transient
	private String token;

	@Column(commit = "密码")
	private String password;

	@Column(commit = "账号")
	private String userAccount;

	@Column(commit = "昵称")
	private String name;

	@Column(commit = "手机号")
	private String userTel;

	@Column(commit = "权限", length = 11, defaultVal = "0")
	private Integer jurisdiction;

	@Column(commit = "头像")
	private String userHeadPortraitPath;

	@Column(commit = "主页背景图")
	private String userPicturePath;

	@Column(commit = "微信openid")
	private String vxOpenId;

	@Column(commit = "微信号")
	private String vxId;

	//微信名、年龄、地区等基本信息
	@Column(commit = "职业")
	private String occupation;

	@Column(commit = "公司")
	private String workPlace;

	@Column(commit = "大学")
	private String college;

	@Column(commit = "年龄", length = 11)
	private Integer age;

	@Column(commit = "生日")
	private Long birthday;

	@Column(commit = "性别", length = 11, defaultVal = "0")
	private Integer sex;

	@Column(commit = "最高学历")
	private String maxEducation;

	@Column(commit = "关注数", length = 11, defaultVal = "0")
	private Integer followNum;

	@Column(commit = "个人描述")
	private String remarks;

	@Column(commit = "等级", length = 11, defaultVal = "1")
	private Integer level;

	@Column(commit = "成长值")
	private Long growthValue;

	@Column(commit = "捐款数", length = 11, defaultVal = "0")
	private Integer payNum;

	@Column(commit = "爱心账户余额")
	private Long surplusAmount;

	@Column(commit = "实名认证状态", length = 11, defaultVal = "0")
	private Integer authenticationStatus;

	@Column(commit = "实名认证类型", length = 11, defaultVal = "1")
	private Integer authenticationType;

	@Column(commit = "技能")
	private String skill;

	@Column(commit = "完整度", length = 11, defaultVal = "0")
	private Integer integrity;

	@Column(commit = "大V标记状态", length = 11, defaultVal = "0")
	private Integer masterStatus;

	@Column(commit = "微信基本信息授权状态", length = 11, defaultVal = "0")
	private Integer authStatus;

	@Column(commit = "邀请码")
	private String inviteCode;

	@Column(commit = "可用状态")
	private String avaliableStatus;

	@Column(commit = "账号类型(个人，组织 etc.)", length = 11, defaultVal = "1")
	private Integer accountType;

	@Column(commit = "是否为预注册账号", length = 11, defaultVal = "0")
	private Integer isFake;


}
