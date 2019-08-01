package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 11:56
 */
public enum CsqKeyValueEnum {

	TYPE_DAILY_DONATE(1, "每日一善"),
	TYPE_INVITE(2, "邀请人关系"),
	TYPE_SCENE(3, "场景值");

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CsqKeyValueEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}}
