package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxUserEnum {

	IS_SDX_NO(0, "非书袋熊用户")
	,IS_SDX_YES(1, "书袋熊用户")
	;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxUserEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
