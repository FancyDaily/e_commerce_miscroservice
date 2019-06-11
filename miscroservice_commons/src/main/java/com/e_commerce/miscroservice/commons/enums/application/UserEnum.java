package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 17:06
 */
public enum UserEnum {
	AUTHENTICATION_STATUS_NO(1, "未实名"),
	AUTHENTICATION_STATUS_YES(2, "已实名"),
	AUTHENTICATION_TYPE_PERSON(1, "实名类型-个人"),
	AUTHENTICATION_TYPE_ORG_OR_CORP(2, "实名类型-机构")
	;

	Integer code;
	String desc;

	public Integer toCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	UserEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
