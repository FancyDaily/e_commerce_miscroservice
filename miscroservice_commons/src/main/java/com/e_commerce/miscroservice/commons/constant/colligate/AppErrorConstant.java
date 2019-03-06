package com.e_commerce.miscroservice.commons.constant.colligate;

/**
 * 功能描述:应用系统错误代码
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Sep 11, 2017 2:54:54 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface AppErrorConstant {

	enum AppError {
		SysError("500", "服务器异常，请稍后重试"), EmptyMessage("8888", "参数为空"), OcrError("70001", "身份证识别出错"),
		JsonError("70002", "Json转换出错"), QuartzError("600001", "任务启动失败"), JobNon("600002", "作业不存在") ,IOError("7777","IO异常");
		private String errorCode;
		private String errorMsg;

		AppError(String errorCode, String erroMsg) {
			this.setErrorCode(errorCode);
			this.setErrorMsg(erroMsg);
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}

		public String getErrorCode() {
			return errorCode;
		}

		void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
	}

	/**
	 * token参数为空
	 */
	String EMPTY_TOKEN = "92000";

	/**
	 * token参数未通过校验
	 */
	String NOT_PASS_TOKEN = "92001";

	/**
	 * 必填参数为空
	 */
	String INCOMPLETE_PARAM = "92002";

	/**
	 * 错误的登录者（被禁用等）
	 */
	String ERROR_LOGIN = "92003";

	/**
	 * 参数未通过校验
	 */
	String NOT_PASS_PARAM = "92004";

	/**
	 * 短信验证码发送错误
	 */
	String ERROR_SENDING_SMS = "92005";

	/**
	 * 错误的分页信息
	 */
	String ERROR_PAGE_INFO = "92006";

	/**
	 * 微信错误Message
	 */
	String WECHAT_EMPTY_MESSAGE = "微信授权字段不全！";

	/**
	 * 短信验证码已发送Message
	 */
	String SMS_SEND_SUCCESS_MESSAGE = "短信验证码已发送";

	/**
	 * 短信验证码未发送Message
	 */
	String SMS_NOT_SEND_MESSAGE = "短信验证码未发送";

	/**
	 * 缺少token参数Message
	 */
	String MISSING_TOKEN_MESSAGE = "缺少token参数";

	/**
	 * 失效的登录状态Message
	 */
	String INVALID_LOGIN_MESSAGE = "登录状态已失效，请重新登录";

	/**
	 * 错误的分页信息Message
	 */
	String ERROR_PAGE_MESSAGE = "页码和分页大小不能为空";

	/**
	 * 技能id不存在Message
	 */
	String MISSING_TECH_ID_MESSAGE = "技能id不存在";

	/**
	 * 短信验证码错误
	 */
	String SendSms_Error = "30001";

	/**
	 * 短信验证码校验错误
	 */
	String SendSms_Valid_Error = "30002";

	/**
	 * 短信校验成功
	 */
	String SendSms_Success = "30003";

	/**
	 * 手机校验成功
	 */
	String Auhto_Success = "30004";

	/**
	 * 字段为空
	 */
	String Field_Error = "30005";

	String OCR_ERROR = "";

}
