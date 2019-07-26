package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-25 21:19
 */
public enum CsqSceneEnum {

	TYPE_FUND(1, "基金"),
	TYPE_SERVICE(2, "服务")
	;

	private Integer code;

	private String msg;

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CsqSceneEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
