package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 16:54
 */
public enum CsqFundEnum {

	AGENT_MODE_STATUS_OFF(0, "未托管"),	//托管状态: 未托管(自由支配)
	AGENT_MODE_STATUS_ON(1, "托管中"),	//平台托管(平台代为处理)
	STATUS_WAIT_ACTIVATE(-1, "待激活"),
	STATUS_ACTIVATED(0, "未公开"),		//TODO 6.11 说明: 未公开、审核中、审核失败 -> 实际等同于同一种状态: 未公开
	STATUS_PRIVATE(1, "待审核"),
	STATUS_PUBLIC(2, "已公开"),
	STATUS_CERT_FAIL(3, "审核失败");

	public static final Double PUBLIC_MINIMUM = 10000d;	//申请公开基金的最低标准

	int val;
	String desc;

	CsqFundEnum(int val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	public int getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

}