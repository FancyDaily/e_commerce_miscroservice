package com.e_commerce.miscroservice.lpglxt_proj.utils;

public class WeixinUtil {
    /**
     * 微信开发平台应用ID
     */
    public static final String APP_ID = "wx7d22702e405fbcd2";
    /**
     * 微信开发平台应用app_secret
     */
    public static final String APP_SECRET = "b2626e43a79504f8b3f3c7713dde0186";
    /**
     * 应用对应的密钥
     */
    public static final String APP_KEY = "F3Ahskdph49dVjAKO0X2KSSFMKSts5Dd";

    /**
     * 微信支付商户号
     */
    public static final String MCH_ID = "1546744731";

    /**
     * 获取预支付id的接口url
     */
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

   /**
     * 微信服务器支付回调通知url
     */
    public static String NOTIFY_URL="http://l.p4j.cc:9898/pay/wxNotify";


}
