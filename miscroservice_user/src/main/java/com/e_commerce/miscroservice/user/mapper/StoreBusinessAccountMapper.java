package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.application.order.ShopMemberOrder;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.user.entity.ShopMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hyf
 * @date 2018年12月13日
 */
@Mapper
public interface StoreBusinessAccountMapper {


    /**
     * 查询账单记录
     * @param userId
     * @return
     */
    List<YjjStoreBusinessAccountLog> findStoreBusinessAccountLog(@Param("userId") Long userId);

    /**
     * 根据账单记录 修改 账单信息 可用金额
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    Integer updateStoreBusinessAccountUseMoney(@Param("yjjStoreBusinessAccountLog") YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);
    /**
     * 根据账单记录 修改 账单信息  待结金额
     * @param yjjStoreBusinessAccountLog
     * @return
     */
    Integer updateStoreBusinessAccountWaitInMoney(@Param("yjjStoreBusinessAccountLog") YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    Integer updateStoreBusinessAccountWaitInMoneyInto(@Param("yjjStoreBusinessAccountLog") YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 查询所有待结算金额日志
     * @return
     */
    List<YjjStoreBusinessAccountLog> findAllWaitInToUseMoneyLog();

    /**
     * 根据小程序id查询
     * @param memberId
     * @return
     */
    ShopMember findStoreBusinessAccountByMemberId(@Param("memberId") Long memberId);

    Integer updateStoreBusinessAccountUseMoneyInto(@Param("yjjStoreBusinessAccountLog") YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 查询流水
     *
     * @param aboutOrderNo aboutOrderNo
     * @param type type
     * @return com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog
     * @author Charlie
     * @date 2019/2/19 14:52
     */
    List<YjjStoreBusinessAccountLog> findByAboutOrderAndType(@Param( "aboutOrderNo" ) String aboutOrderNo, @Param( "type" ) Integer type);

    int waitMoneyInUse(@Param( "id" ) Long id, @Param( "remainingWaitMoney" ) Double remainingWaitMoney);

    /**
     * 根据订单号查询订单
     * @param aboutOrderNo
     * @return
     */
    ShopMemberOrder findOrderByOrderNo(@Param("aboutOrderNo") String aboutOrderNo);
}
