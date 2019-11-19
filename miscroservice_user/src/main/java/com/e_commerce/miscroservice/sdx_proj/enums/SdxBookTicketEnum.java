package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxBookTicketEnum {

	IS_USED_NO(0, "未被使用"),
	IS_USED_YES(1, "被使用")
	;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxBookTicketEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
