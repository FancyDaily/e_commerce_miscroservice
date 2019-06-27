package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-26 15:36
 */
@Data
public class CsqBasicUserVo {

	@Id
	private Long id;

	@Transient
	private Double totalDonate;	//累积捐助(项目详情)

	@Transient
	private Integer minutesAgo;

	@Column(commit = "账号")
	private String userAccount;

	@Column(commit = "昵称")
	private String name;

	@Column(commit = "手机号")
	private String userTel;

	@Column(commit = "头像")
	private String userHeadPortraitPath;

	@Column(commit = "性别", length = 11, defaultVal = "0")
	private Integer sex;

	@Column(commit = "个人描述")
	private String remarks;




}
