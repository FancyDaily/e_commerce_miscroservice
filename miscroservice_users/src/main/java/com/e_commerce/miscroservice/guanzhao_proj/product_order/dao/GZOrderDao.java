package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;

import java.util.List;

public interface GZOrderDao {
    /**
     * 查询我的订单
     * @param id
     * @return
     */
    List<TGzOrder> findMyOrderList(Long id);

    List<TGzOrder> selectByUserIdAndSubjectIdAndStatusCreateTimeDesc(Long userId, Long subjectId, int i);

    /**
     * 保存订单
     * @param payPo
     */
    void saveOrder(TGzOrder payPo);

    /**
     * 查询订单
     * @param out_trade_no
     * @return
     */
    TGzOrder findByOrderNo(String out_trade_no);

    /**
     * 更新订单
     * @param order
     */
    void updateOrder(TGzOrder order);

    void updateByPrimaryKey(TGzOrder order);

    /**
     * 查询我的订单
     * @param orderId
     * @return
     */
    TGzOrder findByOrderId(String orderId);

    /**
     * 查询该课程的所有订单
     * @param id
     * @param payStatus
     * @return
     */
    List<TGzOrder> selectBySubjectIdAndStatus(Long id, Integer payStatus);

    /**
     * 根据价格、状态查找订单
     * @param price
     * @return
     */
    List<TGzOrder> selectByPrice(double price, Integer Status);

    /**
     * 查找某个用户过期的订单
     * @param userId
     * @return
     */
    List<TGzOrder> selectByUserIdExpired(Long userId);

    /**
     * 查找某个用户过期并且使用了优惠券的订单
     * @param userId
     * @return
     */
    List<TGzOrder> selectByUserIdExpiredUsedVoucher(Long userId);

	/**
	 * 根据订单号查找订单
	 * @param orderNo
	 * @return
	 */
	TGzOrder selectByOrderNo(String orderNo);

	/**
	 * 根据课程号和用户编号查找
	 * @param subjectId
	 * @param userId
	 * @return
	 */
	List<TGzOrder> selectBySubjectIdAndUserIdUnpayDesc(Long subjectId, Long userId);
}
