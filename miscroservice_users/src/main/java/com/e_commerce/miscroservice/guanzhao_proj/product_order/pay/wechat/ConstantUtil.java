package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConstantUtil {
    /**
     * 微信开发平台应用ID
     */
    public static final String APP_ID = "wxb8edf6df645eb4e5";
	/**
	 * 微信开发平台应用app_secret
	 */
	public static final String APP_SECRET = "39f5d00a3fb73d82e817525564f99b25";
    /**
     * 应用对应的密钥
     */
    public static final String APP_KEY = "5uBcQ1wcsu8U46xEwgYxv68aRxqsRsLM";

    /**
     * 微信支付商户号
     */
    public static final String MCH_ID = "1536688811";

	/**
     * 获取预支付id的接口url
     */
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 向微信发起退款的接口url
	 */
    public static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    /**
     * 微信服务器支付回调通知url
     */
    public static String NOTIFY_URL;

	/**
	 * 退款回调通知url
	 */
	public static String NOTIFY_URL_REFUD;

	@Value("${notify.url}")
	private void setNotifyUrl(String value) {
		NOTIFY_URL = value;
	}

}
