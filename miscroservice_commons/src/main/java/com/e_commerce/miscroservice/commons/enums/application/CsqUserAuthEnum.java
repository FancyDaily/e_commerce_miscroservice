package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 17:38
 */
public enum CsqUserAuthEnum {

	STATUS_UNDER_CERT(0, "待审核");

	private int code;
	private String desc;

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	CsqUserAuthEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
