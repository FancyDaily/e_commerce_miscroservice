package com.e_commerce.miscroservice.commons.entity.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/9 11:27
 * @Copyright 玖远网络
 */
@Data
public class ShopMemberOrderVo{

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单交易时间
     */
    private Long payTime;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 实际消费
     */
    private BigDecimal realPay;

    /**
     * 订单数量(用来做流水记录时候做判断)
     */
    private Integer orderRecordCount = 1;

    /**
     * 订单确认收货时间
     */
    private Long orderSuccessTime;
}
