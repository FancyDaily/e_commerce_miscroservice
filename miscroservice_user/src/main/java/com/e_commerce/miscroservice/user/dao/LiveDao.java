package com.e_commerce.miscroservice.user.dao;


import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.user.entity.rep.AnchorRep;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/15 17:04
 */
public interface LiveDao {


    /**
     * 判断是否开通微信小程序
     * @param storeId
     * @return
     */
    Boolean openWxUserByStoreId(Long storeId);

    /**
     * 保存申请主播
     * @param obj
     */
    void applyLive(LiveUser obj);

    /**
     * 主播开通状态
     * @param id
     * @return
     */
    Integer findApplyLiveStatus(Long id);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    LiveUser findLiveUserByPhone(String phone);

    /**
     * 更新主播状态
     * @param liveUser
     * @return
     */
    Integer upLiveAnchor(LiveUser liveUser);

    /**
     * 根据小程序会员id查询
     * @param memberId
     * @return
     */
    LiveUser findLiveUserByMemberId(Long memberId);
    /**
     * 根据id查询
     * @param userId
     * @return
     */
    LiveUser findLiveUserById(Long userId);


    /**
     * 通过房间号查找
     *
     * @param roomNum roomNum
     * @return com.e_commerce.miscroservice.user.entity.LiveUser
     * @author Charlie
     * @date 2019/1/17 13:07
     */
    LiveUser findLiveUserByRoomNum(Long roomNum);

    /**
     * 主播列表
     * @param storeId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<AnchorRep> findLiveUserByStoreId(Long storeId, Integer pageNumber, Integer pageSize);
}
