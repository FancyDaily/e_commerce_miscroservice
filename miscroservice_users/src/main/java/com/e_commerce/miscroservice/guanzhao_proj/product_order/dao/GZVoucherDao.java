package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;

import java.util.List;

public interface GZVoucherDao {

    /**
     * 插入一条代金券记录
     * @param voucher
     * @param voucherCopy
     * @return
     */
    int insert(TGzVoucher voucher, TGzVoucher voucherCopy);

    /**
     * 根据用户Id、可用状态、情形
     * @param userId
     * @param availableStatus
     * @return
     */
    List<TGzVoucher> selectByUserIdAndAvailableStatusWithCondition(Long userId, Integer... availableStatus);
}