package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-17 11:41
 */
public enum CsqServiceEnum {

	TYPE_SERIVE(0, "类型-项目"),
	TYPE_FUND(1, "类型-基金"),
	STATUS_UNDER_CERT(-1, "待审核"),
	STATUS_INITIAL(0, "初始状态");

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CsqServiceEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
