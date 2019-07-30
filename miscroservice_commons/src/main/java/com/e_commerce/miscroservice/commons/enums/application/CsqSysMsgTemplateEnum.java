package com.e_commerce.miscroservice.commons.enums.application;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-22 10:03
 */
public enum CsqSysMsgTemplateEnum {

	REGISTER(1, "注册", "欢迎加入丛善桥", "欢迎加入从善桥，从现在开始您的善款捐赠。"),
	CORP_CERT_SUCCESS(2, "组织认证通过", "组织认证现已审核通过", "恭喜！您提交营业执照等认证信息现已审核通过，现在您可以体验完整功能。"),
	CORP_CERT_FAIL(3, "组织认证失败", "组织认证未通过", "很遗憾，您提交的认证请求未能通过。"),
	SERVICE_RECOMMEND(4, "您收到一个项目", "您收到一个项目","根据您的捐赠意向为您推荐一个项目。"),
	SERVICE_NOTIFY_WHILE_CONSUME(5, "项目支出播报", "您参与的项目进行了一笔支出", "您参与的项目\"%s\"进行了一笔支出。")
	;

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
