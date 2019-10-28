package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * 书袋熊订单枚举
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxBookTransRecordEnum {
	TYPE_BECOME_OWNER(1, "成为主人"),
	TYPE_AFTER_READING_NOTE(2,"读后感"),
	TYPE_DAYS_STAY_STATION(3, "待在驿站已经多少天")
	;
	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxBookTransRecordEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}}
