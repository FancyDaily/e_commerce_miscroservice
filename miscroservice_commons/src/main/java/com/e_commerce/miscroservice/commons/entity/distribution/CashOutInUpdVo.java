package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.entity.proxy.PayResult;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用于记录流水状态修改时,一些额外的参数
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/14 15:45
 * @Copyright 玖远网络
 */
@Data
public class CashOutInUpdVo{

    /**
     * 微信提现后返回的微信订单号
     */
    private String paymentNo;


    /**
     * 微信提现后返回的信息
     */
    private String errCodeDes;

    /**
     * 订单实际金额
     */
    private BigDecimal orderRealPay;

    /**
     * 是否需要更新账号里的订单数量,订单总金额
     */
    private boolean isNeedSetOrderInfo;
}
