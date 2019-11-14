package com.e_commerce.miscroservice.lpglxt_proj.utils;

public class WeixinUtil {
    /**
     * 微信开发平台应用ID
     */
    public static final String APP_ID = "wx931234879c8cf896";
    /**
     * 微信开发平台应用app_secret
     */
    public static final String APP_SECRET = "927f8c1f72831e66cd1f1634960269f5";
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
