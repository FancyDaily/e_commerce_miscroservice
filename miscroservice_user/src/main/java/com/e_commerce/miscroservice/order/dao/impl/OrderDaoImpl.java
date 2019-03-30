package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.order.mapper.OrderMapper;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    /**
     * 查询指定userId发布的订单： 服务/求助
     *
     * @param userId
     * @param isService
     * @return
     */
    @Override
    public List<TOrder> selectPublishedByUserId(Long userId, boolean isService, TUser beenViewer) {
        List<TOrder> result = null;
        String[] split = new String[0];
        String beenViewerCompanyIds = beenViewer.getCompanyIds();
        if (beenViewerCompanyIds != null) {
            split = beenViewerCompanyIds.split(",");
        }
        List<Integer> companyIds = new ArrayList<>();
        if (split.length > 0) {
            for (String str : split) {
                companyIds.add(Integer.valueOf(str));
            }
        }
        if (isService) {    //TODO 权限
            if (!companyIds.isEmpty()) {
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
                        .and()
                        .groupBefore()
                        .in(TOrder::getCompanyId, companyIds)
                        .or()
                        .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                        .or()
                        .isNull(TOrder::getCompanyId)
                        .groupAfter()
                        .eq(TOrder::getCreateUser, userId)
                        .eq(TOrder::getType, ProductEnum.TYPE_SERVICE.getValue())
                        .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getStartTime), MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrder::getStatus))  //TODO status ASC
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
                        .and()
                        .groupBefore()
                        .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                        .or()
                        .isNull(TOrder::getCompanyId)
                        .groupAfter()
                        .eq(TOrder::getCreateUser, userId)
                        .eq(TOrder::getType, ProductEnum.TYPE_SERVICE.getValue())
                        .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getStartTime), MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrder::getStatus))  //TODO status ASC
                );
            }
        } else {
            if (!companyIds.isEmpty()) {
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
                        .and()
                        .groupBefore()
                        .in(TOrder::getCompanyId, companyIds)
                        .or()
                        .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                        .or()
                        .isNull(TOrder::getCompanyId)
                        .groupAfter()
                        .eq(TOrder::getCreateUser, userId)
                        .eq(TOrder::getType, ProductEnum.TYPE_SEEK_HELP.getValue())
                        .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getStartTime), MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrder::getStatus))
                );
            } else {    //TODO sql拼接有点问题
                result = MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                        .groupBefore()
                        .groupBefore()
                        .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_REPEAT.getValue())
                        .eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue()).
                                groupAfter()
                        .or()
                        .groupBefore()
                        .eq(TOrder::getTimeType, OrderEnum.TIME_TYPE_NORMAL.getValue())
                        .in(TOrder::getStatus, AppConstant.AVAILABLE_STATUS_ARRAY)
                        .groupAfter()
                        .groupAfter()
                        .and()
                        .groupBefore()
                        .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                        .or()
                        .isNull(TOrder::getCompanyId)
                        .groupAfter()
                        .eq(TOrder::getCreateUser, userId)
                        .eq(TOrder::getType, ProductEnum.TYPE_SEEK_HELP.getValue())
                        .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrder::getStartTime), MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrder::getStatus))
                );
            }
        }
        return result;
    }

    @Override
    public List<TOrder> pageOrder(PageOrderParamView param) {
        return orderMapper.pageOrder(param);
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
    public List<TOrder> selectPastByUserId(Long userId, TUser user) {
        String[] split = new String[0];
        String beenViewerCompanyIds = user.getCompanyIds();
        if (beenViewerCompanyIds != null) {
            split = beenViewerCompanyIds.split(",");
        }
        List<Integer> companyIds = new ArrayList<>();
        if (split.length > 0) {
            for (String str : split) {
                companyIds.add(Integer.valueOf(str));
            }
        }
        if (!companyIds.isEmpty()) {
            return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .in(TOrder::getCompanyId, companyIds)
                    .or()
                    .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                    .or()
                    .isNull(TOrder::getCompanyId)
                    .groupAfter()
                    .eq(TOrder::getCreateUser, userId)
                    .eq(TOrder::getStatus, OrderEnum.STATUS_END.getValue())
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
        } else {
            return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .isNull(TOrder::getCompanyId)
                    .or()
                    .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                    .groupAfter()
                    .eq(TOrder::getCreateUser, userId)
                    .eq(TOrder::getStatus, OrderEnum.STATUS_END.getValue())
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
        }
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
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TOrder findProductOrder(Long serviceId, Long startTime, Long endTime) {
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                .eq(TOrder::getStartTime, startTime).eq(TOrder::getEndTime, endTime).eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue()));
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

    @Override
    public TOrder selectVisiableOrder(Long serviceId) {
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue())
                .eq(TOrder::getVisiableStatus, OrderEnum.VISIABLE_YES.getValue()).eq(TOrder::getConfirmNum, TOrder::getServicePersonnel));
    }

    @Override
    public TOrder selectNearNotVisiable(Long serviceId) {
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue())
                .eq(TOrder::getVisiableStatus, OrderEnum.VISIABLE_NO.getValue()).orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrder::getEndTime)));
    }

    @Override
    public void updateUserName(Long userId, String userName) {
        TOrder order = new TOrder();
        order.setCreateUserName(userName);
        MybatisOperaterUtil.getInstance().update(order, new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getCreateUser, userId));
    }

    @Override
    public TOrder findProductOrderEnough(Long serviceId, long startTime, long endTime) {
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getServiceId, serviceId).eq(TOrder::getIsValid, AppConstant.IS_VALID_YES)
                .eq(TOrder::getStartTime, startTime).eq(TOrder::getEndTime, endTime)
                .eq(TOrder::getStatus, OrderEnum.STATUS_NORMAL.getValue())
                .eq(TOrder::getServicePersonnel, TOrder::getConfirmNum));
    }

    @Override
    public TOrder selectById(Long orderId) {
        return MybatisOperaterUtil.getInstance().findOne(new TOrder(), new MybatisSqlWhereBuild((TOrder.class))
                .eq(TOrder::getId,orderId)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TOrder> selectOrdersInOrderIdsByViewer(List<Long> orderIds, TUser viewer) {
        String[] split = new String[0];
        String beenViewerCompanyIds = viewer.getCompanyIds();
        if (beenViewerCompanyIds != null) {
            split = beenViewerCompanyIds.split(",");
        }
        List<Integer> companyIds = new ArrayList<>();
        if (split.length > 0) {
            for (String str : split) {
                companyIds.add(Integer.valueOf(str));
            }
        }
        if (!companyIds.isEmpty()) {
            return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .in(TOrder::getCompanyId, companyIds)
                    .or()
                    .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                    .or()
                    .isNull(TOrder::getCompanyId)
                    .groupAfter()
                    .in(TOrder::getId, orderIds)
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
        } else {
            return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                    .groupBefore()
                    .isNull(TOrder::getCompanyId)
                    .or()
                    .eq(TOrder::getOpenAuth, ProductEnum.OPEN_AUTH_OPEN.getValue())
                    .groupAfter()
                    .in(TOrder::getId, orderIds)
                    .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
        }
    }

    /**
     * 查询今日相关订单，指定创建者
     * @param userId
     * @return
     */
    @Override
    public List<TOrder> selectDailyOrders(Long userId) {
        // 获取今日起止时间戳
        long currentTimeMillis = System.currentTimeMillis();
        long startStamp = DateUtil.getStartStamp(currentTimeMillis);
        long endStamp = DateUtil.getEndStamp(currentTimeMillis);
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(),new MybatisSqlWhereBuild(TOrder.class)
        .eq(TOrder::getCreateUser,userId)
                .lte(TOrder::getStartTime,endStamp)
                .gte(TOrder::getEndTime,startStamp)
                .neq(TOrder::getStatus,OrderEnum.STATUS_CANCEL.getValue())
                .eq(TOrder::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 查询所有今日创建的订单，指定创建者
     * @param userId
     * @return
     */
    @Override
    public List<TOrder> selectDailyCreatedOrders(Long userId) {
        // 获取今日起止时间戳
        long currentTimeMillis = System.currentTimeMillis();
        long startStamp = DateUtil.getStartStamp(currentTimeMillis);
        long endStamp = DateUtil.getEndStamp(currentTimeMillis);
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(),new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getCreateUser,userId)
                .between(TOrder::getCreateTime,startStamp,endStamp)
                .neq(TOrder::getStatus,OrderEnum.STATUS_CANCEL.getValue())
                .eq(TOrder::getIsValid,AppConstant.IS_VALID_YES));
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

    /**
     * 查看一个人的所有服务订单
     * @param userId
     * @return
     */
    public List<TOrder> selectUserServ(Long userId){
        return MybatisOperaterUtil.getInstance().finAll(new TOrder() , new MybatisSqlWhereBuild(TOrder.class)
                .eq(TOrder::getCreateUser , userId)
                .eq(TOrder::getIsValid , AppConstant.IS_VALID_YES)
                .eq(TOrder::getType , ProductEnum.TYPE_SERVICE.getValue()));
    }

    /**
     * 批量更新订单
     * @param orderList
     * @param orderIdList
     * @return
     */
    public long updateOrderByList(List<TOrder> orderList, List<Long> orderIdList) {
        long count = MybatisOperaterUtil.getInstance().update(orderList,
                new MybatisSqlWhereBuild(TOrder.class)
                        .in(TOrder::getId, orderIdList));
        return count;
    }

    /**
     * 根据订单id、订单状态查找所有订单记录
     *
     * @param idList
     * @param collectionAvailableStatusArray
     * @return
     */
    @Override
    public List<TOrder> selectOrdersInOrderIdsInStatus(List<Long> idList, Integer... collectionAvailableStatusArray) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrder(), new MybatisSqlWhereBuild(TOrder.class)
                .in(TOrder::getId, idList)
                .in(TOrder::getStatus, collectionAvailableStatusArray)
                .eq(TOrder::getIsValid, AppConstant.IS_VALID_YES));
    }
}
