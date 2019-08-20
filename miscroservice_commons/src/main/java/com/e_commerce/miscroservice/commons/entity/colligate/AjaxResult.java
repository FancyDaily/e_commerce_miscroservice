package com.e_commerce.miscroservice.commons.entity.colligate;

import java.io.Serializable;

/**
 * 功能描述:AJAX请求返回结果
 */
public class AjaxResult implements Serializable {

	public static final int SUCCESS = 1;
	public static final int FAILURE = -1;

	private static final long serialVersionUID = 5576237395711742681L;

	private boolean success = false;

	private String errorCode;

	private String msg = "";

	private Object data = null;

	public static AjaxResult successMsg(String msg) {
		AjaxResult result = new AjaxResult();
		result.setMsg(msg);
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object obj) {
		this.data = obj;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
