package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 16:38
 */
public enum CsqInvoiceEnum {
	ISOUT_NOT_YET(0, "待开票"),
	ISOUT_OUT_ALREADY(1, "已经开票")
	;
	public static final double MINIMUM_AMOUNT = 100d;

	int code;
	String msg;

	CsqInvoiceEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
