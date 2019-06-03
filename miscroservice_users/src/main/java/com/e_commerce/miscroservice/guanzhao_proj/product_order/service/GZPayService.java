package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;

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
     * @param orderNo
     * @param userId
     * @param coupon_id
     * @param spbill_create_ip
     * @param i
     * @param subjectId
     * @return
     */
    Map<String, String> dounifiedOrder(String orderNo, Long userId, Long coupon_id, String spbill_create_ip, int i, Long subjectId);

	Map<String, Object> produceOrder(Long subjectId, String orderNum, Long couponId, Long userId, boolean isRandomDisCount);

	/**
     * 微信支付回调
     * @param resXml
     * @return
     */
    String payBack(String resXml);

    void afterPaySuccess(TGzOrder tGzOrder, String out_trade_no);

    void dealWithPrice(double price);

    /**
     * 支付宝预生成(个人二维码生成)
     * @param orderNo
     * @param coupon_id
     * @param subjectId
     * @param userId
     */
    Map<String, Object> preOrder(String orderNo, Long coupon_id, Long subjectId, Long userId);


}
