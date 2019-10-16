package com.e_commerce.miscroservice.lpglxt_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 20:55
 */
public enum TlpglCertEnum {

	TYPE_PRICEDISCOUNT(1, "优惠申请"),
	TYPE_SOLDOUTREQUEST(2, "售出请求"),
	TYPE_CUSTOMER(3, "客户报备"),
	STATUS_INITAIL(0, "初始"),
	STATUS_PASS(1, "审核通过"),
	STATUS_REFUSE(2, "审核驳回")
	;

	private int code;
	private String msg;

	public static TlpglCertEnum getType(Integer type) {
		for(TlpglCertEnum tEnum: TlpglCertEnum.values()) {
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

	TlpglCertEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
