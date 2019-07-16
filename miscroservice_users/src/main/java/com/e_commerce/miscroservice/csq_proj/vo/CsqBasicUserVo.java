package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-26 15:36
 */
@Data(matchSuffix = true)
public class CsqBasicUserVo {

	@Id
	protected Long id;

	private String weiboAccount;	//微博账号

	private String wechatPubAccount;	//微信公众号

	@Transient
	CsqUserAuthVo csqUserAuth;

	private Integer existDayCnt;	//加入天数

	@Column(commit = "爱心账户状态")
	private Integer balanceStatus;

	private boolean gotFund;	//是否已经有至少一个基金

	private boolean gotCompanyAccount;	//是否已经有注册组织账号

	@Column(commit = "账号类型(个人，组织 etc.)", length = 11, defaultVal = "1")
	private Integer accountType;

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

	@Column(commit = "联系人", defaultVal = "未设置")
	protected String contactPerson;

	@Column(commit = "联系方式")
	protected String contactNo;

	public TCsqUser copyTCsqUser() {
		return null;
	}
}
