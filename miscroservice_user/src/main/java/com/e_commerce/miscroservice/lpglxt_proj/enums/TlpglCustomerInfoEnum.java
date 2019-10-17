package com.e_commerce.miscroservice.lpglxt_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 20:55
 */
public enum TlpglCustomerInfoEnum {

	STATUS_INVALID(0, "初始"),
	STATUS_VALID(1, "有效"),
	;

	private int code;
	private String msg;

	public static TlpglCustomerInfoEnum getType(Integer type) {
		for(TlpglCustomerInfoEnum tEnum: TlpglCustomerInfoEnum.values()) {
			if(tEnum.getCode() == type) {
				return tEnum;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	TlpglCustomerInfoEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
