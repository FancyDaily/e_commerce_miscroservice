package com.e_commerce.miscroservice.commons.enums.application;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-22 10:03
 */
public enum CsqSysMsgTemplateEnum {

	TEMPLATE_REGISTER(1, "注册模版");

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CsqSysMsgTemplateEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static CsqSysMsgEnum getType(Integer type) {
		Optional<CsqSysMsgEnum> first = Arrays.stream(CsqSysMsgEnum.values())
			.filter(a -> a.getCode() == type)
			.findFirst();
		return first.isPresent()? first.get():null;
	}
}
