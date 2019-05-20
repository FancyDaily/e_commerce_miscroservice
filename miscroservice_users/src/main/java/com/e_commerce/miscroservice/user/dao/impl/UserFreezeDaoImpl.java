package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.user.dao.UserFreezeDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFreezeDaoImpl implements UserFreezeDao {

    /**
     * 查询用户-冻结表明细
     *
     * @param userId   用户id
     * @param lastTime 最大时间
     * @return
     */
    @Override
    public List<TUserFreeze> queryUserFreezeDESC(Long userId, Long lastTime) {
        return MybatisPlus.getInstance().finAll(new TUserFreeze(), new MybatisPlusBuild(TUserFreeze.class).eq(TUserFreeze::getUserId, userId)
                .lt(TUserFreeze::getCreateTime, lastTime)
                .eq(TUserFreeze::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUserFreeze::getCreateTime)));
    }

    /**
     * 插入冻结记录
     * @param userFreeze
     * @return
     */
    @Override
    public int insert(TUserFreeze userFreeze) {
        return MybatisPlus.getInstance().save(userFreeze);
    }

    /**
     * 根据用户id和订单id查找冻结记录
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public TUserFreeze selectUserFreezeByUserIdAndOrderId(Long userId, Long orderId) {
        return MybatisPlus.getInstance().findOne(new TUserFreeze(),new MybatisPlusBuild(TUserFreeze.class)
        .eq(TUserFreeze::getUserId,userId)
        .eq(TUserFreeze::getOrderId,orderId)
        .eq(TUserFreeze::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 更新冻结记录
     * @param userFreeze
     * @return
     */
    @Override
    public int update(TUserFreeze userFreeze) {
        return MybatisPlus.getInstance().update(userFreeze,new MybatisPlusBuild(TUserFreeze.class)
        .eq(TUserFreeze::getId,userFreeze.getId())
        .eq(TUserFreeze::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public TUserFreeze getUserFreeze(Long createUser, Long orderId) {
        return MybatisPlus.getInstance().findOne(new TUserFreeze(), new MybatisPlusBuild(TUserFreeze.class)
                .eq(TUserFreeze::getUserId, createUser).eq(TUserFreeze::getOrderId, orderId));
    }

    @Override
    public List<TUserFreeze> selectByUserIdBetween(Long userId, Long beginStamp, Long endStamp) {
        return MybatisPlus.getInstance().finAll(new TUserFreeze(), new MybatisPlusBuild(TUserFreeze.class)
        .eq(TUserFreeze::getUserId,userId)
                .between(TUserFreeze::getCreateTime,beginStamp,endStamp)
                .eq(TUserFreeze::getIsValid,AppConstant.IS_VALID_YES));
    }

}
