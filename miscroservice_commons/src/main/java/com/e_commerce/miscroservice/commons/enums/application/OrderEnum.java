package com.e_commerce.miscroservice.commons.enums.application;

public enum OrderEnum {

	TIME_TYPE_NORMAL(0,"指定时间"),
	TIME_TYPE_REPEAT(1,"可重复"),

	COLLECT_TYPE_TIME(1 , "互助时"),
	COLLECT_TYPE_WELFARE(1 , "公益时"),

	STATUS_END(2,"结束"),
	STATUS_NORMAL(1, "正常产出的订单状态"),
	STATUS_CANCEL(3, "已取消"),

	PRODUCE_TYPE_SUBMIT(1, "在发布时派生订单"),
	PRODUCE_TYPE_UPPER(2, "在重新上架的时候派生订单"),
	PRODUCE_TYPE_AUTO(3, "在上一张订单结束时下架"),
	PRODUCE_TYPE_ENROLL(4, "在报名时候派生"),
	PRODUCE_TYPE_ENOUGH(5, "在报名人满的时候派生"),

	PRODUCE_RESULT_CODE_SUCCESS(1, "可以派生订单"),
	PRODUCE_RESULT_CODE_EXISTENCE(2, "订单已经存在，没有再继续派生订单"),
	PRODUCE_RESULT_CODE_LOWER_FRAME(3, "订单派生已经超过商品的结束时间，需要进行下架处理"),

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
