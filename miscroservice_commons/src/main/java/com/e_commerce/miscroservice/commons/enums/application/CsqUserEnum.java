package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 17:06
 */
public enum CsqUserEnum {
	AUTHENTICATION_STATUS_NO(0, "未实名"),
	AUTHENTICATION_STATUS_YES(1, "已实名"),
	AUTHENTICATION_TYPE_PERSON(1, "实名类型-个人"),
	AUTHENTICATION_TYPE_ORG_OR_CORP(2, "实名类型-机构"),
	ACCOUNT_TYPE_PERSON(1, "个人用户"),
	ACCOUNT_TYPE_COMPANY(2, "机构用户")
	;

	public static final String DEFAULT_HEADPORTRAITURE_PATH = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/default/default_head.png";	//默认头像
	Integer code;
	String desc;

	public Integer toCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	CsqUserEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}