package com.e_commerce.miscroservice.commons.entity.colligate;

import lombok.Data;


@Data
public class AliPayPo {

    /**
     * 支付记录ID
     */
    private Integer id;

    /**
     * 课程编号
     */
    private Long subjectId;

    /**
     * 课程名称
     */
    private Long subjectName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单实付金额
     */
    private Double payMoney;

    /**
     * 接口名称
     */
    private String method;

    // --> 业务参数
    /**
     * 该交易在支付宝系统中的交易流水号
     */
    private String tradeNo;

    /**
     * 收款支付宝账号对应的支付宝唯一用户号
     */
    private String sellerId;

    /**
     * 当面付预下单请求生成的二维码码串
     */
    private String qrCode;

    /**
     * 商户网站唯一订单号
     */
    private String outTradeNo;

    // <-- 业务参数

    /**
     * 交易成功失败
     */
    private Boolean isSuccess;

    /**
     * 网关返回码
     */
    private String code;

    /**
     * 网关返回码描述
     */
    private String msg;

    /**
     * 阿里业务返回码
     */
    private String subCode;

    /**
     * 阿里业务返回码描述
     */
    private String subMsg;


}
