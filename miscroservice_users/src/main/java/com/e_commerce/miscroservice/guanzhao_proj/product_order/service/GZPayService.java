package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;

import java.util.Map;

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

    /**
     * 微信支付
     * @param attach
     * @param out_trade_no
     * @param orderNo
     * @param i
     * @param subjectId
     * @return
     */
    Map<String, String> dounifiedOrder(String orderNo, Integer userId, Integer coupon_id, String spbill_create_ip, int i, Long subjectId);

    /**
     * 微信支付回调
     * @param resXml
     * @return
     */
    String payBack(String resXml);
}
