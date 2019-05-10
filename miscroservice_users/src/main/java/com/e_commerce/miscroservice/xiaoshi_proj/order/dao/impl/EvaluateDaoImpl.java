package com.e_commerce.miscroservice.xiaoshi_proj.order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.order.dao.EvaluateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EvaluateDaoImpl implements EvaluateDao {
    @Override
    public List<TEvaluate> selectEvaluateInOrderIds(List orderIds) {
        return MybatisOperaterUtil.getInstance().finAll(new TEvaluate(),new MybatisSqlWhereBuild(TEvaluate.class)
        .in(TEvaluate::getOrderId,orderIds)
        .eq(TEvaluate::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TEvaluate> selectEvaluateInOrderIdsAndByUserId(List orderIds, Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TEvaluate(),new MybatisSqlWhereBuild(TEvaluate.class)
                .in(TEvaluate::getOrderId,orderIds)
                .eq(TEvaluate::getUserId,userId)
                .eq(TEvaluate::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public long save(TEvaluate evaluate){
        return MybatisOperaterUtil.getInstance().save(evaluate);
    }
}
