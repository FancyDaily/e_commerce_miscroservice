package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserFreeze;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;

import java.util.List;

public interface UserFreezeDao {

    /**
     * 查询用户-冻结表记录
     * @param userId 用户id
     * @param lastTime 最大时间
     * @param order 排序规则
     * @return
     */
    List<TUserFreeze> queryUserFreeze(Long userId, Long lastTime, MybatisSqlWhereBuild.ORDER order);
}
