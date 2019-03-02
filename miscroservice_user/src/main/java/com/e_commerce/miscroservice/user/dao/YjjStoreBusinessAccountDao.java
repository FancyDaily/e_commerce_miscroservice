package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;

/**
 * Create by hyf on 2018/11/13
 */
public interface YjjStoreBusinessAccountDao {
    /**
     * 根据用户id查询 用户账户表
     * @param id
     * @return
     */
    YjjStoreBusinessAccount findOne(Long id);

    /**
     * 添加新的账户
     * @param addNew
     */
    Integer addOne(YjjStoreBusinessAccount addNew);

    /**
     * 更新账户
     * @param account
     */
    Integer updateOne(YjjStoreBusinessAccount account);

    /**
     * 更新账户金额操作
     * @param userId
     * @param operMoney
     * @return
     */
    Integer upUseMoney(Long userId, Double operMoney);

}
