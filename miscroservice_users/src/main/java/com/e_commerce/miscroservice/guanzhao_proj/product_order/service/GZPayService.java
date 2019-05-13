package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;

/**
 * @Description TODO
 * @ClassName GZPayService
 * @Auhor huangyangfeng
 * @Date 2019-05-12 18:58
 * @Version 1.0
 */
public interface GZPayService {
    /**
     * 支付宝预生产订单
     * @param payPo
     */
    AliPayPo qrCodeTradePre(AliPayPo payPo);

    /**
     * 支付宝app支付
     * @param payPo
     * @return
     */
    void appTrade(AliPayPo payPo);
}
