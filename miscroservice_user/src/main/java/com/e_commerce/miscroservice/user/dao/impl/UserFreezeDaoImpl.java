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
     * @param order    排序规则
     * @return
     */
    @Override
    public List<TUserFreeze> queryUserFreeze(Long userId, Long lastTime, MybatisSqlWhereBuild.ORDER order) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserFreeze(), new MybatisSqlWhereBuild(TUserFreeze.class).eq(TUserFreeze::getUserId, userId)
                .eq(TUserFreeze::getCreateTime, lastTime)
                .eq(TUserFreeze::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserFreeze::getCreateTime)));
    }

    @Override
    public int insert(TUserFreeze userFreeze) {
        return MybatisOperaterUtil.getInstance().save(userFreeze);
    }
}
