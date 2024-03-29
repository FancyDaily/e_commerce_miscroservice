package com.e_commerce.miscroservice.sdx_proj.enums;

import java.util.Arrays;

/**
 * 书袋熊订单枚举
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxBookOrderEnum {
	TYPE_DONATE(1, "捐书"),
	TYPE_PURCHASE(2, "购书"),
	SHIP_TYPE_MAILING(1, "邮寄"),
	SHIP_TYPE_IN_PERSON(2,"自送"),
//	STATUS_INVALID(-1, "无效的"),
	STATUS_UNPAY(-1, "待支付"),
	STATUS_INITAIL(0, "书籍待收取(邮寄)"),
	STATUS_PROCESSING(1, "进行中"),
	STATUS_DONE(2, "已完成"),
	STATUS_CANCLE(3, "已取消"),
	ATTACH_DONATE(TYPE_DONATE.getCode(), "donate"),
	ATTACH_PURCHASE(TYPE_PURCHASE.getCode(), "purchase")
;
	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxBookOrderEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static Integer getTypeCode(String type) {
		for(SdxBookOrderEnum theEnum: Arrays.asList(TYPE_DONATE, TYPE_PURCHASE)) {
			if(theEnum.getMsg().equals(type)) {
				return theEnum.getCode();
			}
		}
		return null;
	}

}
