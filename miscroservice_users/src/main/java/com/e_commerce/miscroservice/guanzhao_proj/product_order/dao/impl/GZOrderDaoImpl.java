package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZOrderDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzOrder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GZOrderDaoImpl implements GZOrderDao {

    @Override
    public List<TGzOrder> findMyOrderList(Integer id) {
        List<TGzOrder> list = MybatisPlus.getInstance().finAll(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
                .eq(TGzOrder::getUserId,id));
        return list;
    }

    @Override
    public void saveOrder(TGzOrder payPo) {
        MybatisPlus.getInstance().save(payPo);
    }

    @Override
    public TGzOrder findByOrderNo(String out_trade_no) {
        return MybatisPlus.getInstance().findOne(new TGzOrder(),new MybatisPlusBuild(TGzOrder.class).eq(TGzOrder::getTgzOrderNo,out_trade_no));
    }

    @Override
    public void updateOrder(TGzOrder order) {
        MybatisPlus.getInstance().update(order,new MybatisPlusBuild(TGzOrder.class).eq(TGzOrder::getTgzOrderNo,order.getTgzOrderNo()));
    }

    @Override
    public TGzOrder findByOrderId(String orderId) {

        return MybatisPlus.getInstance().findOne(new TGzOrder(),new MybatisPlusBuild(TGzOrder.class).eq(TGzOrder::getId,orderId));
    }

    @Override
    public List<TGzOrder> selectByUserIdAndSubjectIdAndStatusCreateTimeDesc(Long userId, Long subjectId, int status) {
       return  MybatisPlus.getInstance().finAll(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
        .eq(TGzOrder::getUserId, userId)
        .eq(TGzOrder::getSubjectId, subjectId)
        .eq(TGzOrder::getStatus, status)
        .eq(TGzOrder::getIsValid, AppConstant.IS_VALID_YES)
        .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TGzOrder::getCreateTime)));
    }
}
