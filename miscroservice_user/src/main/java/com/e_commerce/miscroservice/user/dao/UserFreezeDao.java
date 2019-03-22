package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;

import java.util.List;

public interface UserFreezeDao {

    /**
     * 查询用户-冻结表记录
     * @param userId 用户id
     * @param lastTime 最大时间
     * @return
     */
    List<TUserFreeze> queryUserFreezeDESC(Long userId, Long lastTime);

    /**
     * 插入用户冻结记录
     * @param userFreeze
     */
    int insert(TUserFreeze userFreeze);

    /**
     * 根据用户id和订单id查找冻结记录
     * @param userId
     * @param orderId
     * @return
     */
    TUserFreeze selectUserFreezeByUserIdAndOrderId(Long userId, Long orderId);

    int update(TUserFreeze userFreeze);
    /**
     * 查询用户再某条订单下的冻结记录
     * @param createUser 用户ID
     * @param orderId 订单ID
     * @return 冻结记录
     */
	TUserFreeze getUserFreeze(Long createUser, Long orderId);

    /**
     * 根据用户id、时间区间查找冻结记录
     * @param userId
     * @param beginStamp
     * @param endStamp
     * @return
     */
    List<TUserFreeze> selectByUserIdBetween(Long userId, Long beginStamp, Long endStamp);
}
