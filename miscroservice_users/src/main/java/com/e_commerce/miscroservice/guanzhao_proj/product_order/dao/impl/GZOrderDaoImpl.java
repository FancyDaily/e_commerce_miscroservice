package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.GZOrderEnum;
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
                .eq(TGzOrder::getUserId,id)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TGzOrder::getCreateTime)));
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
    public void updateByPrimaryKey(TGzOrder order) {
        MybatisPlus.getInstance().update(order, new MybatisPlusBuild(TGzOrder.class)
        .eq(TGzOrder::getId, order.getId())
        .eq(TGzOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzOrder findByOrderId(String orderId) {

        return MybatisPlus.getInstance().findOne(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class).eq(TGzOrder::getId,orderId));
    }

    @Override
    public List<TGzOrder> selectBySubjectIdAndStatus(Long id, Integer payStatus) {
        return MybatisPlus.getInstance().finAll(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
        .eq(TGzOrder::getSubjectId, id)
                .eq(TGzOrder::getStatus, payStatus)
        .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TGzOrder::getCreateTime)));
    }

    @Override
    public List<TGzOrder> selectByPrice(double price, Integer Status) {
        return MybatisPlus.getInstance().finAll(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
        .eq(TGzOrder::getPrice, price)
        .eq(TGzOrder::getStatus, Status)
        .eq(TGzOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzOrder> selectByUserIdExpired(Long userId) {
        long currentTimeMillis = System.currentTimeMillis();
        return MybatisPlus.getInstance().finAll(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
        .eq(TGzOrder::getUserId, userId)
        .lt(TGzOrder::getOrderTime, currentTimeMillis - GZOrderEnum.INTERVAL)
        .eq(TGzOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzOrder> selectByUserIdExpiredUsedVoucher(Long userId) {
        long currentTimeMillis = System.currentTimeMillis();
        return MybatisPlus.getInstance().finAll(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
                .isNotNull(TGzOrder::getVoucherId)
                .eq(TGzOrder::getUserId, userId)
                .lt(TGzOrder::getOrderTime, currentTimeMillis - GZOrderEnum.INTERVAL)
                .eq(TGzOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

	@Override
	public TGzOrder selectByOrderNo(String orderNo) {
		return MybatisPlus.getInstance().findOne(new TGzOrder(), new MybatisPlusBuild(TGzOrder.class)
		.eq(TGzOrder::getTgzOrderNo, orderNo)
		.eq(TGzOrder::getIsValid, AppConstant.IS_VALID_YES));
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
