package com.e_commerce.miscroservice.sdx_proj.enums;

public enum SdxFocusEnum {
	I_FOCUS(0, "你关注的"),
	FOCUS_ME(1, "关注我的"),
	MUTUAL_FOCUS(2, "互相关注"),
	DATA_NULL(3, "参数为空或参数错误!");

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxFocusEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
