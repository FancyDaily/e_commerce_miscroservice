package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.order.mapper.OrderMapper;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 马晓晨
 * @date 2019/3/5
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public int saveOneOrder(TOrder order) {
        return MybatisOperaterUtil.getInstance().save(order);
    }

    @Override
    public TOrder selectByPrimaryKey(Long id) {
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getId, id));
    }

    @Override
    public int updateByPrimaryKey(TOrder order) {
        return MybatisOperaterUtil.getInstance().update(order, new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getId, order.getId()));
    }

    @Override
    public List<TOrder> pageOrder(PageOrderParamView param) {
        return orderMapper.pageOrder(param);
    }

    /**
     * 查询指定userId发布的订单： 服务/求助
     * @param userId
     * @param isService
     * @return
     */
    @Override
    public List<TOrder> selectPublishedByUserId(Long userId, boolean isService) {
        List<TOrder> result = null;
        if (isService) {
            result = MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_REPEAT)
                    .and().eq(TOrder::getStatus, OrderEnum.STATUS_END).groupAfter().or()
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_NORMAL)
                    .and().in(TOrder::getStatus, AppConstant.AVAILABLE_STATUS_ARRAY)
                    .groupAfter().and()
                    .eq(TOrder::getCreateUser, userId)
                    .eq(TOrder::getType, ProductEnum.TYPE_SERVICE)
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                    .orderBy(MybatisSqlWhereBuild.ORDER.DESC, TOrder::getCreateTime) //TODO status ASC
            );
        } else {
            result = MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_REPEAT)
                    .and().eq(TOrder::getStatus, OrderEnum.STATUS_END).groupAfter().or()
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_NORMAL)
                    .and().in(TOrder::getStatus, AppConstant.AVAILABLE_STATUS_ARRAY)
                    .groupAfter().and()
                    .eq(TOrder::getCreateUser, userId)
                    .eq(TOrder::getType, ProductEnum.TYPE_SERVICE)
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                    .orderBy(MybatisSqlWhereBuild.ORDER.DESC, TOrder::getCreateTime)
            );
        }
        return result;
}

    /**
     * 查询指定用户Id的过往服务/求助记录
     * @param userId
     * @return
     */
    @Override
    public List<TOrder> selectPastByUserId(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(),new MybatisSqlWhereBuild(TOrder.class)
        .eq(TOrder::getCreateUser,userId)
        .eq(TOrder::getStatus,OrderEnum.STATUS_END)
        .eq(TOrder::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 查询发布的所有记录
     * @param userId
     * @return
     */
    @Override
    public List<TOrder> selectPublishedByUserId(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getCreateUser, userId)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.ORDER.DESC, TOrder::getCreateTime));
    }
}
