package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserFollow;

import java.util.List;
import java.util.Map;

public interface UserFollowDao {


    /**
     * 是否关注
     * @param userId 用户id
     * @param userFollowId 将要被关注的对象id
     * @return
     */
    boolean isAtten(Long userId, Long userFollowId);

    /**
     * 获取指定双方的关注、被关注记录
     * @param userId
     * @param userFollowId
     * @return
     */
    Map<String, Object> findRecords(Long userId, Long userFollowId);

    /**
     * 更新指定记录
     * @param userFollow
     * @return
     */
    int update(TUserFollow userFollow);

    /**
     * 插入记录
     * @param userFollow
     */
    int insert(TUserFollow userFollow);

    /**
     * 查询userId为指定userId的,creatTime小于lastTime的记录
     * @param userId
     * @param lastTime
     * @return
     */
    List<TUserFollow> findUserIdRecords(Long userId, Long lastTime);

    /**
     * 查询userFollowId为指定userId的,creatTime小于lastTime的记录
     * @param userId
     * @param lastTime
     * @return
     */
    List<TUserFollow> findUserFollowIdRecords(Long userId, Long lastTime);

    /**
     * 查询指定对方的关注状态
     * @param id
     * @param userId
     * @return
     */
    Integer queryAttenStatus(Long id, Long userId);

    /**
     * 是否有该用户的关注信息
     * @param userId 关注用户
     * @param userFollowId 被关注用户
     * @return
     */
    Long countUserFollow(Long userId, Long userFollowId);
}
