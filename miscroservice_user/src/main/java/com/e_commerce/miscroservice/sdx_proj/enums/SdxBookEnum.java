package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxBookEnum {

	STATUS_INITIAL(0, "初始(捐助过程中...)"),
	STATUS_ON_SHELF(1, "在书架上"),
	STATUS_BOUGHT(2, "被买走"),
	STATUS_CANCLE(3, "捐助过程取消")
	;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxBookEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
