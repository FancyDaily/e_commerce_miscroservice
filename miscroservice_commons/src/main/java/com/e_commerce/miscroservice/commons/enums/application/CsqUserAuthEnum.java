package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 17:38
 */
public enum CsqUserAuthEnum {

	TYPE_PERSON(0, "个人认证"),
	TYPE_CORP(1, "组织认证"),
	STATUS_UNDER_CERT(0, "待审核"),
	STATUS_CERT_PASS(1, "审核通过"),
	STATUS_CERT_REFUSE(2, "审核拒绝");

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
