package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-26 15:36
 */
@Data(matchSuffix = true)
public class CsqBasicUserVo {

	@Id
	protected Long id;

	@Transient
	TCsqUserAuth csqUserAuth;

	@Transient
	protected Double totalDonate;	//累积捐助(项目详情)

	@Transient
	protected Integer minutesAgo;

	@Column(commit = "账号")
	protected String userAccount;

	@Column(commit = "昵称")
	protected String name;

	@Column(commit = "手机号")
	protected String userTel;

	@Column(commit = "头像")
	protected String userHeadPortraitPath;

	@Column(commit = "性别", length = 11, defaultVal = "0")
	protected Integer sex;

	@Column(commit = "个人描述")
	protected String remarks;

	public TCsqUser copyTCsqUser() {
		return null;
	}
}
