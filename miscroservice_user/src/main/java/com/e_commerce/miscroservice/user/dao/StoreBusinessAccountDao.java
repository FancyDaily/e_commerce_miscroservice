package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.user.entity.ShopMember;

import java.util.List;


/**
 * 账户
 * @author hyf
 * @date 2018年12月13日
 */
public interface StoreBusinessAccountDao {

    /**
     * 根据账户用户id查询账户
     * @param userId
     * @return
     */
    YjjStoreBusinessAccount findStoreBusinessAccount(Long userId);

    /**
     * 添加 账户记录
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    Integer insertStoreBusinessAccountLog(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 账单记录
     * @param userId
     * @param page
     * @return
     */
    List<YjjStoreBusinessAccountLog> findStoreBusinessAccountLog(Long userId, Integer page);

    /**
     * 修改账户信息
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    Integer updateStoreBusinessAccount(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 查询所有待结算金额日志
     * @return
     */
    List<YjjStoreBusinessAccountLog> findAllWaitInToUseMoneyLog();

    void updateAllWaitInToMoney();

    /**
     * 根据小程序id查询
     * @param memberId
     * @return
     */
    ShopMember findStoreBusinessAccountByMemberId(Long memberId);

    /**
     * 创建用户账号
     * @param account
     * @return
     */
    Integer insertStoreBusinessAccount(YjjStoreBusinessAccount account);

    /**
     * 查询流水
     *
     * @param orderNo orderNo
     * @param type type
     * @return com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog
     * @author Charlie
     * @date 2019/2/19 15:21
     */
    YjjStoreBusinessAccountLog findByAboutOderAndType(String orderNo, Integer type);


    List<YjjStoreBusinessAccountLog> findByAboutOder(String shopMemberOrderNo);

    /**
     * 待结资金结算
     *
     * @param id id
     * @param remainingWaitMoney remainingWaitMoney
     * @return int
     * @author Charlie
     * @date 2019/2/19 15:22
     */
    int waitMoneyInUse(Long id, Double remainingWaitMoney);

    /**
     * 根据订单号查询订单
     * @param aboutOrderNo
     * @return
     */
    ShopMemberOrder findOrderByOrderNo(String aboutOrderNo);
}
