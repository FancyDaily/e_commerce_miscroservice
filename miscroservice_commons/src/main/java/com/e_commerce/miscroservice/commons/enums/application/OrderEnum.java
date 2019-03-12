package com.e_commerce.miscroservice.commons.enums.application;

public enum OrderEnum {

	TIME_TYPE_NORMAL(0,"指定时间"),
	TIME_TYPE_REPEAT(1,"可重复"),

	COLLECT_TYPE_TIME(1 , "互助时"),
	COLLECT_TYPE_WELFARE(1 , "公益时"),

	STATUS_END(2,"结束"),
	STATUS_NORMAL(1, "正常产出的订单状态");


	private int value;
	private String desc;

	OrderEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}
	public String getDesc() {
		return desc;
	}
}
