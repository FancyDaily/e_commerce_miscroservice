package com.e_commerce.miscroservice.commons.enums.application;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-22 10:03
 */
public enum CsqSysMsgTemplateEnum {

//	REGISTER(1, "注册", "欢迎加入丛善桥", "欢迎来到丛善桥，在这里你可以拥有自己的爱心账户，记录你所有的公益捐赠信息。"),
	REGISTER(1, "注册", "欢迎加入丛善桥", "欢迎来到丛善桥！\n" + "\t在这里，您可以拥有自己的爱心账户，设立专项基金。您从平台捐赠的每一笔善款都将被详细记录，永久保留！马上开启您的公益体验之旅吧~"),
	ACCOUNT_ACTIVATE(2, "爱心账户已开通", "爱心账户已开通", "恭喜您！已经成功开启爱心账户，您在平台的公益捐赠都将为您终身记录！"),
	FUND_PUBLIC_SUCCESS(3, "基金已开放", "基金开放募捐", "恭喜您！已经成功激活您的专项基金\"%s\"，并可以在平台公开募资了！"),
	SERVICE_RECOMMEND(4, "您收到一个项目", "您收到一个项目","根据您的捐赠意向为您推荐一个项目。"),
	CORP_CERT_SUCCESS(5, "组织认证通过", "组织认证现已审核通过", "您的企业账户已经认证成功，可以正常捐赠了。"),
	CORP_CERT_FAIL(6, "组织认证失败", "组织认证未通过", "您的企业账户由于信息不匹配认证失败，请确认营业执照清晰无码，企业名称跟证照相匹配后重新申请。%s"),
	SERVICE_NOTIFY_WHILE_CONSUME(7, "项目支出播报", "您参与的项目进行了一笔支出", "您参与的项目\"%s\"进行了一笔支出。"),
//	INVOICE_DONE(8, "您的发票已开具", "您的发票已开具", "您的订单号为\"%s\"等发票已开具")
	INVOICE_DONE(8, "您的发票已开具", "您的发票已开具", "您的发票已开具"),
	SERVICE_OR_FUND_OUT(9, "项目执行进展提醒", "您参与的项目%s有了一笔支出", "您参与的项目有了一笔支出:%s")
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
