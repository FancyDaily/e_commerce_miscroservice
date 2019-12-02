package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxBookInfoEnum {

	;

	public static Integer DEFAULT_MAXIMUMRESERVE = 3;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxBookInfoEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
