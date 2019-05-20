package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserFollow;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.UserFollowDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserFollowDaoImpl implements UserFollowDao {
    /**
     * 返回是否关注
     * @param userId 用户id
     * @param userFollowId 将要被关注的对象id
     * @return
     */
    @Override
    public boolean isAtten(Long userId, Long userFollowId) {
        return !MybatisPlus.getInstance().finAll(new TUserFollow(), new MybatisPlusBuild(TUserFollow.class)
                .eq(TUserFollow::getUserId, userId)
                .eq(TUserFollow::getUserFollowId, userFollowId)
                .eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES)).isEmpty();
    }

    /**
     * 返回互相关注Map
     * @param userId
     * @param userFollowId
     * @return
     */
    @Override
    public Map<String, Object> findRecords(Long userId, Long userFollowId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<TUserFollow> userFollows = MybatisPlus.getInstance().finAll(new TUserFollow(), new MybatisPlusBuild(TUserFollow.class)
                .groupBefore().
                        eq(TUserFollow::getUserId, userId)
                .eq(TUserFollow::getUserFollowId, userFollowId).eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES)
                .groupAfter().or().groupBefore().
                        eq(TUserFollow::getUserId, userFollowId)
                .eq(TUserFollow::getUserFollowId, userId).eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES)
                .groupAfter());
        if(userFollows.size()>1) {
            resultMap.put("isFollowTheSame", true);  //互关标志
        } else {
            resultMap.put("isFollowTheSame", false); //互关标志
        }

        if (userFollows.isEmpty()) { //如果找不到记录
            return resultMap;
        }

        //我关注ta
        for (TUserFollow userFollow : userFollows) {
            if (userId.equals(userFollow.getUserId()) && userFollowId.equals(userFollow.getUserFollowId())) {  //我关注ta的记录
                resultMap.put("follow", userFollow);
                continue;
            }
            resultMap.put("beFollowed", userFollow);
        }
        return resultMap;
    }

    /**
     * 更新指定记录
     * @param userFollow
     * @return
     */
    @Override
    public int update(TUserFollow userFollow) {
        return MybatisPlus.getInstance().update(userFollow,new MybatisPlusBuild(TUserFollow.class).eq(TUserFollow::getId,userFollow.getId()));
    }

    /**
     * 插入记录
     * @param userFollow
     */
    @Override
    public int insert(TUserFollow userFollow) {
        return MybatisPlus.getInstance().save(userFollow);
    }

    /**
     * 查询userId为指定userId的,creatTime小于lastTime的记录
     * @param userId
     * @param lastTime 分页参数之一
     * @return
     */
    @Override
    public List<TUserFollow> findUserIdRecords(Long userId, Long lastTime) {
        return MybatisPlus.getInstance().finAll(new TUserFollow(),new MybatisPlusBuild(TUserFollow.class)
                .eq(TUserFollow::getUserId,userId)
                .lt(TUserFollow::getCreateTime,lastTime)
                .eq(TUserFollow::getIsValid,AppConstant.IS_VALID_YES)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUserFollow::getCreateTime)));
    }

    /**
     * 查询userFollowId为指定userId的,creatTime小于lastTime的记录
     * @param userId
     * @param lastTime
     * @return
     */
    @Override
    public List<TUserFollow> findUserFollowIdRecords(Long userId, Long lastTime) {
        return MybatisPlus.getInstance().finAll(new TUserFollow(),new MybatisPlusBuild(TUserFollow.class)
                .eq(TUserFollow::getUserFollowId,userId)
                .lt(TUserFollow::getCreateTime,lastTime)
                .eq(TUserFollow::getIsValid,AppConstant.IS_VALID_YES)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUserFollow::getCreateTime)));
    }

    /**
     * 查询指定对方的关注状态 0未关注 1已关注 3互相关注
     * @param id
     * @param userId
     * @return
     */
    @Override
    public Integer queryAttenStatus(Long id, Long userId) {
        List<TUserFollow> userFollows = MybatisPlus.getInstance().finAll(new TUserFollow(), new MybatisPlusBuild(TUserFollow.class)
                .groupBefore().
                        eq(TUserFollow::getUserId, id)
                .eq(TUserFollow::getUserFollowId, userId).eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES)
                .groupAfter().or().groupBefore().
                        eq(TUserFollow::getUserId, userId)
                .eq(TUserFollow::getUserFollowId, id).eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES)
                .groupAfter());
        Integer myStatus = 0;
        Integer herStatus = 0;
        //我关注ta
        for (TUserFollow userFollow : userFollows) {
            if (id.equals(userFollow.getUserId()) && userId.equals(userFollow.getUserFollowId())) {  //我关注ta的记录
                myStatus = 1;
                continue;
            }
            herStatus = 1;
        }
        Integer status = myStatus;
        if(myStatus.equals(1) && herStatus.equals(1)) {
            status = 2;
        }

        return status;
    }

    @Override
    public Long countUserFollow(Long userId, Long userFollowId) {
        return MybatisPlus.getInstance().count(new MybatisPlusBuild(TUserFollow.class)
                .eq(TUserFollow::getUserId, userId).eq(TUserFollow::getUserFollowId, userFollowId)
                .eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES));
    }
}
