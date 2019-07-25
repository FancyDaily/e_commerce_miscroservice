package com.e_commerce.miscroservice.commons.enums.application;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 15:32
 */
public enum CsqOrderEnum {
	/*TYPE_UNKNOWN(-1,"未知"),
	TYPE_ACCOUNT_CHARGE(0, "爱心账户充值"),
	TYPE_FUND_APPLY(1, "申请基金前付款"),
	TYPE_FUND_CHARGE(2, "基金充值"),
	TYPE_ITEM_CHARGE(3,"项目捐入"),*/

	STATUS_UNPAY(1, "待支付"),
	STATUS_ALREADY_PAY(2, "已支付"),
	STATUS_TIME_OUT(3, "支付超时"),
	STATUS_REFUND_UNDERWAY(5, "退款中"),
	STATUS_ALREADY_REFUND(6, "已退款"),

	IS_ANONYMOUS_FALSE(0,"非匿名"),
	IS_ANONYMOUS_TRUE(1, "匿名"),

	INVOICE_STATUS_NO(0, "未开票"),
	          INVOICE_STATUS_YES(1, "已开票")
	;

	public static final long INTERVAL = 30l * 60 * 1000;
	Integer code;
	String desc;

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	CsqOrderEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
