package com.e_commerce.miscroservice.commons.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 15:47
 */
@Configuration
public class CsqWechatConstant {

	/**
	 * 微信开发平台应用ID
	 */
//	public static String APP_ID = "wx77a8c12808edf5b9";
	public static String APP_ID = "wx772231ae2be61a3b";
	public static String SDX_APP_ID = "wx490e0363ff3cc1d6";

	/**
	 * 微信开发平台应用app_secret
	 */
//	public static String APP_SECRET = "9e003f827136566c620d94a6817d4b42";
	public static String APP_SECRET = "4fa48a6fffe061e752f1fae11f0e37e0";
	public static String SDX_APP_SECRET = "5db718caf6ca3749b7502f3c74d90d6c";

	/**
	 * 应用对应的密钥
	 */
	public static String APP_KEY = "5uBcQ1wcsu8U46xEwgYxv68aRxqsRsLM";

	/**
	 * 微信支付商户号
	 */
	public static final String MCH_ID = "1230977302";
//	public static final String MCH_ID = "1305322501";

	/**
	 * 获取预支付id的接口url
	 */
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 向微信发起退款的接口url
	 */
	public static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/**
	 * 微信服务器回调通知url
	 */
	public static String NOTIFY_URL = "https://test.xiaoshitimebank.com/user/csq/pay/wxNotify/pay";

	/**
	 * 退款回调通知url
	 */
	public static String NOTIFY_URL_REFUD;

	@Value("${csq.notify.url}")
	private void setNotifyUrl(String value) {
		NOTIFY_URL = value;
	}

	@Value("${csq.app_id}")
	private void setAppId(String val) {
		APP_ID = val;
	}

	@Value("${csq.app_secret}")
	private void setAppSecret(String val) {
		APP_SECRET = val;
	}

	@Value("${csq.app_key}")
	private void setAppKey(String val) {
		APP_KEY = val;
	}
}
