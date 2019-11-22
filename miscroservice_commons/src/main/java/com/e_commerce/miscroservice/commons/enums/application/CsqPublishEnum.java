package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:31
 */
public enum CsqPublishEnum {
	MAIN_KEY_TREND(1, "关注方向"),
	MAIN_KEY_DAILY_DONATE(2, "日推"),
	MAIN_KEY_SDX_DAILY_SUGGEST(3, "书袋熊每日推荐")
	;

	Integer code;
	String  msg;

	CsqPublishEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer toCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
