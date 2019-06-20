package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:23
 */
public enum CsqSysMsgEnum {

	TYPE_NORMAL(1, "普通类型"),
	TYPE_SREVICE(2, "项目推送类型"),
	IS_READ_FALSE(0, "未读"),
	IS_READ_TRUE(1, "已读");

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CsqSysMsgEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
