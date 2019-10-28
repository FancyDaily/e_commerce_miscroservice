package com.e_commerce.miscroservice.sdx_proj.enums;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-03 15:53
 */
public enum SdxRedisEnum {

	CSQ_GLOBAL_DONATE_BROADCAST(0,"csq:global:doante:broadcast"),
//	CSQ_GLOBAL_DONATE_BROADCAST_GATHER(1, "csq:global:doante:broadcast:gather"),
	ALL(-1, "all");

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	SdxRedisEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}}
