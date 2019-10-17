package com.e_commerce.miscroservice.lpglxt_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 20:55
 */
public enum TlpglHouseEnum {

	DISCOUNT_STAUTS_NO(0, "未优惠"),
	DISCOUNT_STATUS_YES(1, "优惠中"),
	STATUS_INITAIL(0, "初始"),
	STATUS_SOLDOUT(1, "售出"),
	;

	private int code;
	private String msg;

	public static TlpglHouseEnum getType(Integer type) {
		for(TlpglHouseEnum tEnum: TlpglHouseEnum.values()) {
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

	TlpglHouseEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
