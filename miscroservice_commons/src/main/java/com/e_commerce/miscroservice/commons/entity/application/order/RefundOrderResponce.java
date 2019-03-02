package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

@Data
public class RefundOrderResponce extends RefundOrder {
    private Integer untreatedTimeMoreThan4Hours;

    /**
     * 实际退款金额
     */
    private String refundCostMoney;
    /**
     * 商品名称
     */
    private String shopName;

    /**
     * 商品的skuid
     */
    private Long skuId;
    /**
     * 受理时间
     */
    private String dealTime;

    /**
     * 申请时间
     */
    private String applyRefundTime;
    /**
     * 售后单号
     */
    private String refundNewOrderNo;

}
