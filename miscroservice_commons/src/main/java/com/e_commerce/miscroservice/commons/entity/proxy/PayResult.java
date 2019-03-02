package com.e_commerce.miscroservice.commons.entity.proxy;

import lombok.Data;

/**
 * 描述 参数
 * @author hyq
 * @date 2018/9/21 13:55
 * @return
 */
@Data
public class PayResult {

    /*
    返回状态码
     */
    private String return_code;
    /*
    公众账号ID
     */
    private String appid;
    /*
     商户号
      */
    private String mch_id;
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
    用户标识
     */
    private String openid;
    /*
    交易类型
     */
    private String trade_type;
    /*
    付款银行
     */
    private String bank_type;
    /*
    总金额
     */
    private int total_fee;
    /*
    现金支付金额
     */
    private int cash_fee;
    /*
    微信支付订单号
     */
    private String transaction_id;
    /*
    商户订单号
     */
    private String out_trade_no;
    /*
    支付完成时间
     */
    private String time_end;
    /*
    返回信息
     */
    private String return_msg;
    /*
    设备号
     */
    private String device_info;
    /*
    错误代码
     */
    private String err_code;
    /*
    错误代码描述
     */
    private String err_code_des;
    /*
    是否关注公众账号
     */
    private String is_subscribe;
    /*
    货币种类
     */
    private String fee_type;
    /*
    现金支付货币类型
     */
    private String cash_fee_type;
    /*
    代金券或立减优惠金额
     */
    private String coupon_fee;
    /*
    代金券或立减优惠使用数量
     */
    private String coupon_count;
    /*
    代金券或立减优惠ID
     */
    private String coupon_id_$n;
    /*
    单个代金券或立减优惠支付金额
     */
    private String coupon_fee_$n;
    /*
    商家数据包
     */
    private String attach;

}
