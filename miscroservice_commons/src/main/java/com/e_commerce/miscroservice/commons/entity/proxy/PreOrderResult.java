package com.e_commerce.miscroservice.commons.entity.proxy;

import lombok.Data;

/**
 * 描述 微信支付 - 统一下单返回结果的封装entity
 *
 * @author hyq
 * @date 2018/9/21 13:57
 * @return
 */
@Data
public class PreOrderResult {

    /*
     返回状态码
      */
    private String return_code;
    /*
     返回信息
      */
    private String return_msg;
    /*
     公众账号ID
      */
    private String appid;
    /*
     商户号
      */
    private String mch_id;
    /*
     设备号
      */
    private String device_info;
    /*
     随机字符串
      */
    private String nonce_str;
    /*
     签名
      */
    private String sign;
    /*
    业务结果
     */
    private String result_code;
    /*
     错误代码
      */
    private String err_code;
    /*
     错误代码描述
      */
    private String err_code_des;
    /*
     交易类型
      */
    private String trade_type;
    /*
     预支付交易会话标识
      */
    private String prepay_id;
    /*
     二维码链接
      */
    private String code_url;

}
