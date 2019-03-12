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
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    /**
     * 查询指定userId发布的订单： 服务/求助
     *
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
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_REPEAT.getValue())
                    .eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue()).groupAfter().or()
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_NORMAL.getValue())
                    .in(TOrder::getStatus, AppConstant.AVAILABLE_STATUS_ARRAY)
                    .groupAfter()
                    .groupAfter()
                    .eq(TOrder::getCreateUser, userId)
                    .eq(TOrder::getType, ProductEnum.TYPE_SERVICE.getValue())
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                    .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getCreateTime).buildAsc(TOrder::getStatus))  //TODO status ASC
            );
        } else {
            result = MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_REPEAT.getValue())
                    .eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue()).groupAfter().or()
                    .groupBefore()
                    .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_NORMAL.getValue())
                    .in(TOrder::getStatus, AppConstant.AVAILABLE_STATUS_ARRAY)
                    .groupAfter()
                    .groupAfter()
                    .eq(TOrder::getCreateUser, userId)
                    .eq(TOrder::getType, ProductEnum.TYPE_SEEK_HELP.getValue())
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                    .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getCreateTime).buildAsc(TOrder::getStatus))
            );
        }
        return result;
    }

    @Override
    public Page<TOrder> pageOrder(PageOrderParamView param) {
        Page<TOrder> page = PageHelper.startPage(param.getPageNum(), param.getPageSize());
        orderMapper.pageOrder(param);
        return page;
    }

    @Override
    public List<TOrder> selectOrderByOrderIds(List<Long> orderIds) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .in(TOrder::getId, orderIds));
    }


    /**
     * 查询指定用户Id的过往服务/求助记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<TOrder> selectPastByUserId(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getCreateUser, userId)
                .eq(TOrder::getStatus, OrderEnum.STATUS_END.getValue())
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    /**
     * 根据订单id集合查找订单
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<TOrder> selectOrdersInOrderIds(List orderIds) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .in(TOrder::getId, orderIds)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    /**
     * 根据来源、状态、用户id查找订单记录
     *
     * @param sourceType
     * @param userId
     * @param availableStatusArray
     * @return
     */
    @Override
    public List<TOrder> selectBySourceAndUserIdAndStatuses(Integer sourceType, Long userId, Integer[] availableStatusArray) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getSource, sourceType)
                .eq(TOrder::getCreateUser, userId)
                .in(TOrder::getStatus, availableStatusArray)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public void updateByServiceId(Long productId, Integer status) {
        TOrder order = new TOrder();
        order.setServiceStatus(status);
        MybatisOperaterUtil.getInstance().update(order, new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, productId));
    }

    @Override
    public Long countProductOrder(Long serviceId) {
        return MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getIsValid, "1"));
    }

    @Override
    public Long countProductOrder(Long serviceId, Long startTime, Long endTime) {
        return MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                .eq(TOrder::getStartTime, startTime).eq(TOrder::getEndTime, endTime));
    }

    /**
     * 根据来源、用户id、状态、订单id集合查找
     *
     * @param source
     * @param userId
     * @param availableStatusArray
     * @param idList
     * @return
     */
    @Override
    public List<TOrder> selectBySourceAndUserIdAndStatusesInIds(Integer source, Long userId, Integer[] availableStatusArray, List<Long> idList) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getSource, source)
                .eq(TOrder::getCreateUser, userId)
                .in(TOrder::getStatus, availableStatusArray)
                .in(TOrder::getId, idList)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

	@Override
	public TOrder findOneLatestOrderByServiceId(Long serviceId) {
        // 非正常状态的订单的最新一条订单（已结束和被取消的订单）
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).neq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue())
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getStartTime)));
	}

	/**
     * 查询发布的所有记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<TOrder> selectPublishedByUserId(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getCreateUser, userId)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getCreateTime))
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc((TOrder::getStatus))));
    }
}
