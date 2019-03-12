package com.e_commerce.miscroservice.commons.enums.application;

public enum OrderEnum {

	TIME_TYPE_NORMAL(0,"指定时间"),
	TIME_TYPE_REPEAT(1,"可重复"),

	COLLECT_TYPE_TIME(1 , "互助时"),
	COLLECT_TYPE_WELFARE(2 , "公益时"),

	STATUS_END(2,"结束"),
	STATUS_NORMAL(1, "正常产出的订单状态"),
	STATUS_CANCEL(3, "已取消"),

	PRODUCE_TYPE_SUBMIT(1, "在发布时派生订单"),
	PRODUCE_TYPE_UPPER(2, "在重新上架的时候派生订单"),
	PRODUCE_TYPE_AUTO(3, "在上一张订单结束时下架"),
	PRODUCE_TYPE_ENROLL(4, "在报名时候派生"),
	//显示状态: 1、已结束  2、已取消 3、待选人 4、被拒绝  5、已报名 6、已入选
	SHOW_STATUS_ENROLL_CHOOSE_ALREADY_END(1, "已结束"),
	SHOW_STATUS_ENROLL_CHOOSE_ALREADY_CANCEL(2, "已取消"),
	SHOW_STATUS_ENROLL_CHOOSE_WAIT_CHOOSE(3,"待选人"),
	SHOW_STATUS_ENROLL_CHOOSE_BE_REFUSED(4, "被拒绝"),
	SHOW_STATUS_ENROLL_CHOOSE_ALREADY_ENROLL(5,"已报名"),
	SHOW_STATUS_ENROLL_CHOOSE_ALREADY_CHOOSED(6, "已入选");
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
