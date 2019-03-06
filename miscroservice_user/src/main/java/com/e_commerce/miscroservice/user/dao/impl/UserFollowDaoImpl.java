package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserFollow;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
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
        return !MybatisOperaterUtil.getInstance().finAll(new TUserFollow(), new MybatisSqlWhereBuild(TUserFollow.class)
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
        List<TUserFollow> userFollows = MybatisOperaterUtil.getInstance().finAll(new TUserFollow(), new MybatisSqlWhereBuild(TUserFollow.class)
                .groupBefore().
                        eq(TUserFollow::getUserId, userId)
                .eq(TUserFollow::getUserFollowId, userFollowId)
                .groupAfter().or().groupBefore().
                        eq(TUserFollow::getUserId, userFollowId)
                .eq(TUserFollow::getUserFollowId, userId).eq(TUserFollow::getIsValid, AppConstant.IS_VALID_YES)
                .groupAfter());
        if (userFollows.isEmpty()) { //如果找不到记录
            resultMap.put("isFollowTheSame", false); //互关标志
            return resultMap;
        }

        if(userFollows.size()>1) {
            resultMap.put("isFollowTheSame", true);  //互关标志
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
        return MybatisOperaterUtil.getInstance().update(userFollow,new MybatisSqlWhereBuild(TUserFollow.class).eq(TUserFollow::getId,userFollow.getId()));
    }

    /**
     * 插入记录
     * @param userFollow
     */
    @Override
    public int insert(TUserFollow userFollow) {
        return MybatisOperaterUtil.getInstance().save(userFollow);
    }

    /**
     * 查询userId为指定userId的,creatTime小于lastTime的记录
     * @param userId
     * @param lastTime 分页参数之一
     * @return
     */
    @Override
    public List<TUserFollow> findUserIdRecords(Long userId, Long lastTime) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserFollow(),new MybatisSqlWhereBuild(TUserFollow.class)
                .eq(TUserFollow::getUserId,userId)
                .lt(TUserFollow::getCreateTime,lastTime)
                .orderBy(MybatisSqlWhereBuild.ORDER.DESC,TUserFollow::getUpdateTime));
    }

    /**
     * 查询userFollowId为指定userId的,creatTime小于lastTime的记录
     * @param userId
     * @param lastTime
     * @return
     */
    @Override
    public List<TUserFollow> findUserFollowIdRecords(Long userId, Long lastTime) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserFollow(),new MybatisSqlWhereBuild(TUserFollow.class)
                .eq(TUserFollow::getUserFollowId,userId)
                .lt(TUserFollow::getCreateTime,lastTime)
                .orderBy(MybatisSqlWhereBuild.ORDER.DESC,TUserFollow::getUpdateTime));
    }
}
