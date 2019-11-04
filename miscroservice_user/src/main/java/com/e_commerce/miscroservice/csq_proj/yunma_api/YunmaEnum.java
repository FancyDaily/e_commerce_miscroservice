package com.e_commerce.miscroservice.csq_proj.yunma_api;

/**
 * 正元枚举类
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum YunmaEnum {


	;

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	YunmaEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
