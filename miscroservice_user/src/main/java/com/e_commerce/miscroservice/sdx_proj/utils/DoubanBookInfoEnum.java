package com.e_commerce.miscroservice.sdx_proj.utils;

/**
 * @Author: FangyiXu
 * @Date: 2019-11-12 15:08
 */
public enum DoubanBookInfoEnum {
	;

	public static String AUTHOR = "作者";
	public static String PUBLISHER = "出版社";
	public static String TRANSLATOR = "译者";
	public static String PUBLISH_TIME = "出版年";
	public static String TOTAL_PAGES = "页数";
	public static String PRICE = "定价";
	public static String BINDING_LAYOUT = "装帧";
	public static String SERIES = "丛书";
	public static String ISBN = "ISBN";

	public static String NAME = "书名";
	public static String COVER_PIC = "封面图";
	public static String RATING = "评分";

	public static String INTRODUCTION = "简介";
	public static String CATALOG = "目录";

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	DoubanBookInfoEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
