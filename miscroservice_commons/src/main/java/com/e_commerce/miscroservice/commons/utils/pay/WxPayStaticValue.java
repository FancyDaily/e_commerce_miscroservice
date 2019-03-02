package com.e_commerce.miscroservice.commons.utils.pay;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/16 9:59
 * @Copyright 玖远网络
 */
public class WxPayStaticValue {


    // 公众账号ID
    public static final String APPID = "wx6ad169bccc57554a";
    // 商户号
    public static final String MCH_ID = "1403320302";
    // 商户密钥
    public static final String KEY = "ebdd1da629156627139d0b5be22bee67";


    public static final String NOTIFY_WX_RECHAEGE_URL = "https://local.yujiejie.com/user/user/callback/recharge/weixin";
    // 支付方式，取值如下：JSAPI，NATIVE，APP
    public static final String TRADE_TYPE = "JSAPI";
    public static final String TRADE_APP_TYPE = "APP";

    // 微信支付 - 统一下单地址
    public static final String PLACEANORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
