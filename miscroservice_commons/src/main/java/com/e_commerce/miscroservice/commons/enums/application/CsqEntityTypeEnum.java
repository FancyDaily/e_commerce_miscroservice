package com.e_commerce.miscroservice.commons.enums.application;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:37
 */
public enum CsqEntityTypeEnum {

	TYPE_HUMAN(1, "平台外账户"),
	TYPE_ACCOUNT(2, "爱心账户"),
	TYPE_FUND(3, "基金账户"),
	TYPE_SERVICE(4, "项目"),
	SUBTYPE_OTHER(999999, "补充类型");

	int code;
	String msg;

	public int toCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CsqEntityTypeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static CsqEntityTypeEnum getType(int code) {
		List<CsqEntityTypeEnum> collect = Arrays.stream(CsqEntityTypeEnum.values()).filter(a -> a.code == code)
			.collect(Collectors.toList());
		if(collect.isEmpty()) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "code错误!");
		}
		return collect.get(0);
	}
}
