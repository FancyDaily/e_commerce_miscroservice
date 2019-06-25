package com.e_commerce.miscroservice.commons.exception.colligate;

/**
 * 功能描述:错误信息工具类
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2018年10月29日 上午10:52:10
 */
public class MessageException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public MessageException() {
		super();
	}

	public MessageException(String errorCode,String message) {
		super(message);
		this.setErrorCode(errorCode);		
	}
	
	public MessageException(String message) {
		super(message);		
	}
	
	public MessageException(String message,Exception e) {
		super(message);		
	}

	public MessageException(String errorCode,String message, Throwable cause) {
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
