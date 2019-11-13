package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxWishListEnum {

	IS_WATING_FOR_NOTICE_YES(1, "是否处于来货提醒状态-是"),
	IS_WATING_FOR_NOTICE_NO(0, "是否处于来货提醒状态-否")
	;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxWishListEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
