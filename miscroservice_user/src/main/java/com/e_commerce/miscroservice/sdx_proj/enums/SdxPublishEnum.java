package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxPublishEnum {

	MAIN_KEY_BOOK_DAILY_SUGGEST(3, "每日推荐书目"),
	;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxPublishEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
