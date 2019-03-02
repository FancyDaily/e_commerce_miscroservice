package com.e_commerce.miscroservice.user.service.store;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * 账户
 * @author hyf
 * @date 2018年12月13日
 */
public interface StoreBusinessAccountService {


    /**
     * 更新账户金额 账户日志
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    Integer updateStoreBusinessAccount(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 查询账户
     * @param userId
     * @return
     */
    YjjStoreBusinessAccount selectStoreBusinessAccount(Long userId);

    /**
     * 查询账户记录
     * @param userId
     * @param page
     * @return
     */
    SimplePage selectStoreBusinessAccountLog(Long userId, Integer page);

    /**
     * 将待结金额转换为可用金额
     * @param userId
     */
    void waitInMoneyToUse(Long userId);

    /**
     * 将所有的都转换
     */
    void waitInMoneyToUseAll();

    Response checkShopInShop(Long userId);

    /**
     * 发货15天,结算
     *
     * @param shopMemberOrderNo shopMemberOrderNo
     * @param storeId storeId
     * @param storeOrderNo
     * @return boolean
     * @author Charlie
     * @date 2019/2/19 15:39
     */
    boolean sendGoodsAfter15DaysWaitMoneyIn(String shopMemberOrderNo, Long storeId, Long storeOrderNo);
}
