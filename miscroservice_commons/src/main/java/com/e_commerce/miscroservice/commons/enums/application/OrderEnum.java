package com.e_commerce.miscroservice.commons.enums.application;

public enum OrderEnum {

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
