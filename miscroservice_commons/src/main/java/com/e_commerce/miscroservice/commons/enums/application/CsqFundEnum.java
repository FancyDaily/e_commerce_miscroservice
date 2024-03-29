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
	STATUS_UNDER_CERT(1, "审核中"),
	STATUS_PUBLIC(2, "已公开"),
	STATUS_CERT_FAIL(3, "审核失败"),
	STATUS_DONE(4, "已完成"),
	STATUS_OFF_SHELF(5, "下架"),
	RAISE_STATUS_RAISING(0, "筹备中"),
	RAISE_STAUTS_DONE(1, "进行中"),
	IS_SHOWN_YES(1, "可展示"),
	IS_SHOWN_NO(0, "不展示")
	;

	public static final Double PUBLIC_MINIMUM = 10000d;		//申请公开基金的最低标准
//	public static final Double PUBLIC_MINIMUM = 100000d;
	public static final String DEFAULT_COVER_PIC = "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/congshanqiao/default_cover_pic_fund.jpg";	//默认封面图
	public static final String DEFAULT_NAME_TEMPLATE = "%s的专项基金";

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
