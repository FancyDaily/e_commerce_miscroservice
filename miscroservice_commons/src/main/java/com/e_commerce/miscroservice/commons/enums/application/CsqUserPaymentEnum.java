package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:37
 */
public enum CsqUserPaymentEnum {

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

	CsqUserPaymentEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
