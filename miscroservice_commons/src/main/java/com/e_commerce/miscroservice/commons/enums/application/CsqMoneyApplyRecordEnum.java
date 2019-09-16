package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-12 10:08
 */
public enum CsqMoneyApplyRecordEnum {
	STATUS_CERT_INITIAL(-1, "初始状态"),
	STATUS_CERT_UNPASS(0, "审核未通过"),
	STATUS_UNDER_CERT(1, "待审核"),
	STATUS_CERT_PASS(2,"审核通过")
	;

	Integer code;
	String desc;

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	CsqMoneyApplyRecordEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
