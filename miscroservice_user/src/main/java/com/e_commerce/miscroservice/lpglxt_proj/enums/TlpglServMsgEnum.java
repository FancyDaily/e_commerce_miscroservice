package com.e_commerce.miscroservice.lpglxt_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 20:55
 */
public enum TlpglServMsgEnum {

	TYPE_PRICEDISCOUNT(1, "", "", "优惠"),
	TYPE_SOLDOUTREQUEST(2, "", "", "售出"),
	TYPE_CUSTOMER(3, "", "", "客户报备")
	;

	private int code;
	private String templateId;
	private String url;
	private String description;

	public static TlpglServMsgEnum getType(Integer type) {
		for (TlpglServMsgEnum tEnum : TlpglServMsgEnum.values()) {
			if (tEnum.getCode() == type) {
				return tEnum;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}

	TlpglServMsgEnum(int code, String templateId, String url, String description) {
		this.code = code;
		this.templateId = templateId;
		this.url = url;
		this.description = description;
	}
}
