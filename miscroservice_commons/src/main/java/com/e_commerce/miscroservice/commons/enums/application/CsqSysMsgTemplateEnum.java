package com.e_commerce.miscroservice.commons.enums.application;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-22 10:03
 */
public enum CsqSysMsgTemplateEnum {

	TEMPLATE_REGISTER(1, "注册模版", "欢迎加入丛善桥", "欢迎加入从善桥，从现在开始您的善款捐赠。");

	int code;
	String msg;
	String title;
	String content;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	CsqSysMsgTemplateEnum(int code, String msg, String title, String content) {
		this.code = code;
		this.msg = msg;
		this.title = title;
		this.content = content;
	}

	public static CsqSysMsgTemplateEnum getType(Integer type) {
		Optional<CsqSysMsgTemplateEnum> first = Arrays.stream(CsqSysMsgTemplateEnum.values())
			.filter(a -> a.getCode() == type)
			.findFirst();
		return first.isPresent()? first.get():null;
	}
}
