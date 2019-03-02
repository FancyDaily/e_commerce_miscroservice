package com.e_commerce.miscroservice.commons.enums.pay;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/11 21:37
 * @Copyright 玖远网络
 */
public enum WxPayTradeTypeEnum{


    /*
    JSAPI--JSAPI支付（或小程序支付）、
    NATIVE--Native支付、
    APP--app支付，
    MWEB--H5支付，
    不同trade_type决定了调起支付的方式，请根据支付产品正确上传
     */

    XIAO_CHENG_XU("JSAPI"),
    NATIVE("NATIVE"),
    APP("APP"),
    H5("MWEB");

    private String type;

    WxPayTradeTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
