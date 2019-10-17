package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼盘管理用户表
 * @Author: FangyiXu
 * @Date: 2019-10-14 15:45
 */
@Table(commit = "楼盘管理用户表")
@Data
@Builder
@NoArgsConstructor
public class TLpglUser extends BaseEntity {

	@Column(commit = "密码")
	private String password;

	@Column(commit = "账号")
	private String userAccount;

	@Column(commit = "昵称", charset = "utf8mb4")
	private String name;

	@Column(commit = "手机号")
	private String userTel;

	@Column(commit = "职位", length = 11)
	private Integer position;

	@Column(commit = "头像")
	private String userHeadPortraitPath;

	@Column(commit = "微信openid")
	private String vxOpenId;

	@Column(commit = "微信号")
	private String vxId;

	@Column(commit = "性别", length = 11, defaultVal = "0")
	private Integer sex;

	@Column(commit = "个人描述", charset = "utf8mb4")
	private String remarks;

	@Column(commit = "可用状态", defaultVal = "1")
	private Integer availableStatus;

	@Column(commit = "邮箱")
	private String mail;

	@Column(commit = "分组编号")
	private Integer groupId;

}
