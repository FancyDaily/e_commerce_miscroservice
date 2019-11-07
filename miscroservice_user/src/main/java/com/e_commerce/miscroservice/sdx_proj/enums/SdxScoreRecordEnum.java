package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * 书袋熊订单枚举
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxScoreRecordEnum {
	IN_OUT_IN(0, "收入"),
	IN_OUT_OUT(1, "支出s"),
	TYPE_BOOK(1, "书籍类型"),
	TYPE_AFTER_READING_NOTE(2, "读后感类型"),
	;
	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxScoreRecordEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}}
