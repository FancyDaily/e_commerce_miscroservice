package com.e_commerce.miscroservice.commons.exception.colligate;

/**
 * @author 马晓晨
 * @date 2019/3/12
 */
public class ErrorException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public ErrorException() {
		super();
	}

	public ErrorException(String errorCode,String message) {
		super(message);
		this.setErrorCode(errorCode);
	}

	public ErrorException(String message) {
		super(message);
	}

	public ErrorException(String message,Exception e) {
		super(message);
	}

	public ErrorException(String errorCode,String message, Throwable cause) {
		super(message, cause);
		this.setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
