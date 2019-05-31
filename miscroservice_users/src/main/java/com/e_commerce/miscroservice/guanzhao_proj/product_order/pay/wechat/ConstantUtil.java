package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

public class ConstantUtil {
    /**
     * 微信开发平台应用ID
     */
    public static final String APP_ID = "wxd19384cf3643922a";
    /**
     * 应用对应的密钥
     */
    public static final String APP_KEY = "XmhML5NXoU6dzN72GxVBrZJFmyXy0Ldu";
//	wxb8edf6df645eb4e5
    /**
     * 微信支付商户号
     */
    public static final String MCH_ID = "1536688811";

    /**
     * 获取预支付id的接口url
     */
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 微信服务器回调通知url
     */
    public static String NOTIFY_URL = "";
}
