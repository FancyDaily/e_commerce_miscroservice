package com.e_commerce.miscroservice.commons.enums.application;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:37
 */
public enum CsqServiceMsgEnum {

	FUND_PUBLIC_SUCCESS(CsqSysMsgTemplateEnum.FUND_PUBLIC_SUCCESS.getCode(), "项目筹款目标达成", "package/logs/pages/shareJinji/shareJinji", "uB6i-_PyETK8W95Ipt8szxQ0_CWwq9MkNSmPMS9j0D8"),
	CORP_CERT_SUCCESS(CsqSysMsgTemplateEnum.CORP_CERT_SUCCESS.getCode(), "企业认证成功", "/pages/logs/logs", "_jrRQH6g0xY5FChU7Z3_iZqh2olm5IjE0TMNncUmfAI"),
	CORP_CERT_FAIL(CsqSysMsgTemplateEnum.CORP_CERT_FAIL.getCode(), "企业认证失败", "/package/logs/pages/zuzhirenzheng/zuzhirenzheng", "_jrRQH6g0xY5FChU7Z3_iZqh2olm5IjE0TMNncUmfAI"),
	SERVICE_NOTIFY_WHILE_CONSUME(CsqSysMsgTemplateEnum.SERVICE_NOTIFY_WHILE_CONSUME.getCode(), "项目资金消耗", "/package/logs/pages/shareDetail/shareDetail", ""),
	INVOICE_DONE(CsqSysMsgTemplateEnum.INVOICE_DONE.getCode(), "发票已开具", "/package/logs/pages/piaoList/piaoList", "PiA4mWgPtZnaajP98JYWfEdopseqbUcc2ymkPkmTiEc")
	;

	int code;
	String msg;
	String page;
	String templateId;

	CsqServiceMsgEnum(int code, String msg, String page, String templateId) {
		this.code = code;
		this.msg = msg;
		this.page = page;
		this.templateId = templateId;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public String getPage() {
		return page;
	}

	public String getTemplateId() {
		return templateId;
	}

	public static CsqServiceMsgEnum getType(Integer type) {
		Optional<CsqServiceMsgEnum> first = Arrays.stream(CsqServiceMsgEnum.values())
			.filter(a -> a.getCode() == type)
			.findFirst();
		return first.isPresent()? first.get():null;
	}
}
