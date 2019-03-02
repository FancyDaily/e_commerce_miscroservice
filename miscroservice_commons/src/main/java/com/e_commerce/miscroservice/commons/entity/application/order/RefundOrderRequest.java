package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

@Data
public class RefundOrderRequest extends BaseEntity {
    //售后单号
    private Long refundOrderNo;
    //退款订单号
    private Long orderNo;
    //退款人手机
    private String storePhone;
    //退款人姓名
    private String storeName;
    //退款商品名称
    private String shopName;
    //售后状态
    private Integer refundStatus;
    //售后类型
    private Integer refundType;
    //退单理由
    private String refundReason;
    //申请退款费用最小值
    private Double refundCostMin;
    //申请退款费用最大值
    private Double refundCostMax;
    //实际退款金额最小值
    private Double refundMoneyMin;
    //实际退款金额最大值
    private Double refundMoneyMax;
    //申请退款数量最小值
    private Integer returnCountMin;
    //申请退款数量最大值
    private Integer returnCountMax;
    //申请退款时间
    private String applyTimeMin;
    //申请退款时间
    private String applyTimeMax;
    //受理时间
    private String storeDealRefundTimeMin;
    //受理时间
    private String storeDealRefundTimeMax;

    //供应商姓名
    private String supplierName;
    //供应商id
    private Integer supplierId;

}
