package com.e_commerce.miscroservice.user.dao;


import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;

import java.util.List;

/**
 * Create by hyf on 2018/11/13
 */
public interface YjjStoreBusinessAccountLogDao {

    /**
     * 添加流水日志
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    Integer addOne(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 根据订单号查询订单 包含冻结状态
     * @param out_trade_no
     * @return
     */
    YjjStoreBusinessAccountLog findOneByOrderNo(String out_trade_no);

    /**
     * 更新流水
     * @param yjjStoreBusinessAccountLog
     */
    void upOne(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 根据页码收支查询
     * @param valueOf
     * @param page
     * @param inOut
     * @param type
     * @param typeAdaptor
     * @return
     */
    List<YjjStoreBusinessAccountLog> findAll(Long valueOf, Integer page, Integer inOut, Integer type, Integer typeAdaptor);

    /**
     * 根据id查询最近的一条记录
     * @param id
     * @return
     */
    YjjStoreBusinessAccountLog findLimitTimeOne(Integer id);

    /**
     * 账户流水详情
     * @param req
     * @return
     */
    YjjStoreBusinessAccountLog findOne(YjjStoreBusinessAccountLog req);
}
