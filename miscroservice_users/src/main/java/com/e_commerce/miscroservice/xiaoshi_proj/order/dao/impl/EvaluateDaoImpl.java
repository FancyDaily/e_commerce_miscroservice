package com.e_commerce.miscroservice.xiaoshi_proj.order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.order.dao.EvaluateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EvaluateDaoImpl implements EvaluateDao {
    @Override
    public List<TEvaluate> selectEvaluateInOrderIds(List orderIds) {
        return MybatisPlus.getInstance().finAll(new TEvaluate(),new MybatisPlusBuild(TEvaluate.class)
        .in(TEvaluate::getOrderId,orderIds)
        .eq(TEvaluate::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TEvaluate> selectEvaluateInOrderIdsAndByUserId(List orderIds, Long userId) {
        return MybatisPlus.getInstance().finAll(new TEvaluate(),new MybatisPlusBuild(TEvaluate.class)
                .in(TEvaluate::getOrderId,orderIds)
                .eq(TEvaluate::getUserId,userId)
                .eq(TEvaluate::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public long save(TEvaluate evaluate){
        return MybatisPlus.getInstance().save(evaluate);
    }
}
