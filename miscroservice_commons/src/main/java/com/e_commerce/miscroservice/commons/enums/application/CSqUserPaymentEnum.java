package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:37
 */
public enum CSqUserPaymentEnum {

	TYPE_HUMAN(1, "平台外账户"),
	TYPE_ACCOUNT(2, "爱心账户"),
	TYPE_FUND(3, "基金账户"),
	TYPE_SERVICE(4, "项目"),
	SUBTYPE_OTHER(999999, "补充类型"),
	INOUT_IN(0, "收入"),
	INOUT_OUT(1, "支出");

	int code;
	String msg;

	public int toCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CSqUserPaymentEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
