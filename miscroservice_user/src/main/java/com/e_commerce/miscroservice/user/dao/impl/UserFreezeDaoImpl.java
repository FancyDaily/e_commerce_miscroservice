package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
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
        return MybatisOperaterUtil.getInstance().finAll(new TUserFreeze(), new MybatisSqlWhereBuild(TUserFreeze.class).eq(TUserFreeze::getUserId, userId)
                .lt(TUserFreeze::getCreateTime, lastTime)
                .eq(TUserFreeze::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserFreeze::getCreateTime)));
    }

    /**
     * 插入冻结记录
     * @param userFreeze
     * @return
     */
    @Override
    public int insert(TUserFreeze userFreeze) {
        return MybatisOperaterUtil.getInstance().save(userFreeze);
    }

    /**
     * 根据用户id和订单id查找冻结记录
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public TUserFreeze selectUserFreezeByUserIdAndOrderId(Long userId, Long orderId) {
        return MybatisOperaterUtil.getInstance().findOne(new TUserFreeze(),new MybatisSqlWhereBuild(TUserFreeze.class)
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
        return MybatisOperaterUtil.getInstance().update(userFreeze,new MybatisSqlWhereBuild(TUserFreeze.class)
        .eq(TUserFreeze::getId,userFreeze.getId())
        .eq(TUserFreeze::getIsValid,AppConstant.IS_VALID_YES));
    }

}
