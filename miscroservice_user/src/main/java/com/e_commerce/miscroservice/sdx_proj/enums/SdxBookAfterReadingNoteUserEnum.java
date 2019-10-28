package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * 书袋熊订单枚举
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxBookAfterReadingNoteUserEnum {
	IS_THUMB_YES(1, "点赞/踩"),
	IS_THUMB_NO(0, "未点赞/踩"),
	THUMB_TYPE_UP(1, "赞"),
	THUMB_TYPE_DOWN(2, "踩"),
	TYPE_THUMB_OR_PURCHASE(1, "点赞或购买"),
	TYPE_MESSAGE(2, "评论"),
	IS_PURCHASE_YES(1, "已付费"),
	IS_PURCHASE_NO(0, "未付费")
;
	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxBookAfterReadingNoteUserEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}}
