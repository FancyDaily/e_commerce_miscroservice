package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.mapper.OrderRelationshipMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019/3/4 下午5:38
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Repository
public class OrderRelationshipDaoImpl implements OrderRelationshipDao {

    @Autowired
    OrderRelationshipMapper relationshipMapper;

    /**
     * 根据主键查询订单关系表
     *
     * @param orderRelationshipId
     * @return
     */
    public TOrderRelationship selectByPrimaryKey(Long orderRelationshipId) {
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getId, orderRelationshipId));
        return orderRelationship;
    }

    /**
     * 插入订单关系表
     *
     * @param orderRelationship
     * @return
     */
    public int insert(TOrderRelationship orderRelationship) {
        int save = MybatisOperaterUtil.getInstance()
                .save(orderRelationship);
        return save;
    }

    /**
     * 根据主键更新订单关系表
     *
     * @param orderRelationship
     * @return
     */
    public int updateByPrimaryKey(TOrderRelationship orderRelationship) {
        int update = MybatisOperaterUtil.getInstance().update(orderRelationship,
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getId, orderRelationship.getId()));
        return update;
    }

    /**
     * 根据日期找到报名者的订单关系
     *
     * @param startTime
     * @param endTime
     * @param serviceId
     * @param userId
     * @return
     */
    public TOrderRelationship selectByDateByEnrollUserId(Long startTime, Long endTime, Long serviceId, Long userId) {
        List<Integer> orderRelationshipStatusList = participationStatusList();
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getServiceId, serviceId)
                        .gte(TOrderRelationship::getStartTime, startTime)
                        .lte(TOrderRelationship::getStartTime, endTime)
                        .eq(TOrderRelationship::getReceiptUserId, userId)
                        .in(TOrderRelationship::getStatus, orderRelationshipStatusList));
        return orderRelationship;
    }

    /**
     * 根据订单id和用户id来查找订单关系
     *
     * @param orderId
     * @param userId
     * @return
     */
    public TOrderRelationship selectByOrderIdAndUserId(Long orderId, Long userId) {
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .groupBefore().eq(TOrderRelationship::getFromUserId, userId).or()
                        .eq(TOrderRelationship::getReceiptUserId, userId).groupAfter()
                        .eq(TOrderRelationship::getOrderId, orderId));
        return orderRelationship;
    }

    /**
     * 根据订单id和报名用户idList来查询订单关系List
     *
     * @param orderId
     * @param userIdList
     * @return
     */
    public List<TOrderRelationship> selectByOrderIdAndEnrollUserIdList(Long orderId, List<Long> userIdList) {
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId, orderId)
                        .in(TOrderRelationship::getReceiptUserId, userIdList));
        return orderRelationshipList;
    }

    /**
     * 根据用户id（发布者和参与者）来查询订单关系List
     *
     * @param userId
     * @return
     */
    public List<TOrderRelationship> selectByUserId(Long userId) {
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .groupBefore().eq(TOrderRelationship::getFromUserId, userId)
                        .or().eq(TOrderRelationship::getFromUserId, userId)
                        .groupAfter()
                        .eq(TOrderRelationship::getIsValid, "1"));
        return orderRelationshipList;
    }

    @Override
    public List<TOrderRelationship> pageEnrollAndChooseList(Integer pageNum, Integer pageSize, Long userId) {
        return relationshipMapper.pageEnrollAndChoose(userId);
    }

    /**
     * 查询指定接单者的订单记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<TOrderRelationship> selectOrderRelationshipByReceiptUserId(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
                .eq(TOrderRelationship::getReceiptUserId, userId)
                .eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));

    }

    /**
     * 查询用户相关订单关系记录
     *
     * @param id
     * @return
     */
    @Override
    public List<TOrderRelationship> selectCollectList(Long id) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
                .groupBefore()
                .groupBefore()
                .eq(TOrderRelationship::getFromUserId, id)
                .groupAfter().or()
                .groupBefore().eq(TOrderRelationship::getReceiptUserId, id)
                .groupAfter().groupAfter()
                .eq(TOrderRelationship::getServiceCollectionType, OrderRelationshipEnum.SERVICE_COLLECTION_IS_TURE.getType())
                .eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));

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

    /**
     * 根据订单关系id、收藏状态更新收藏状态
     *
     * @param orderRelationshipId
     * @param collectStatus
     * @return
     */
    @Override
    public int updateCollectStatus(Long orderRelationshipId, int collectStatus) {
        TOrderRelationship orderRelationship = new TOrderRelationship();
        orderRelationship.setId(orderRelationshipId);
        orderRelationship.setServiceCollectionType(collectStatus);
        return MybatisOperaterUtil.getInstance().update(orderRelationship, new MybatisSqlWhereBuild(TOrderRelationship.class));
    }

    /**
     * 根据orderId和statusList来升序查询报名者订单List
     *
     * @param orderId
     * @param statusList
     * @return
     */
    public List<TOrderRelationship> selectListByStatusListByEnroll(Long orderId, List<Integer> statusList) {
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId, orderId)
                        .in(TOrderRelationship::getStatus, statusList)
                        .isNotNull(TOrderRelationship::getReceiptUserId)
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrderRelationship::getCreateTime))
        );
        return orderRelationshipList;
    }

    /**
     * 根据orderId和status来查询未开始（签到）报名者订单List
     *
     * @param orderId
     * @param status
     * @return
     */
    public List<TOrderRelationship> selectListByStatusForNotSignByEnroll(Long orderId, int status) {
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId, orderId)
                        .eq(TOrderRelationship::getStatus, status)
                        .isNotNull(TOrderRelationship::getReceiptUserId)
                        .eq(TOrderRelationship::getSignType , OrderRelationshipEnum.SIGN_TYPE_NO.getType())
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrderRelationship::getCreateTime))
        );
        return orderRelationshipList;
    }

    /**
     * 根据orderId和status来查询报名者订单List
     *
     * @param orderId
     * @param status
     * @return
     */
    public List<TOrderRelationship> selectListByStatusByEnroll(Long orderId, int status) {
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId, orderId)
                        .eq(TOrderRelationship::getStatus, status)
                        .isNotNull(TOrderRelationship::getReceiptUserId)
                        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrderRelationship::getCreateTime))
        );
        return orderRelationshipList;
    }

    /**
     * 根据statusList来查询未投诉参与者订单数量
     * @param orderId
     * @param status
     * @return
     */
    public long selectCountByStatusByEnroll(Long orderId, Integer status) {
        long count = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
                .count(TOrderRelationship::getId)
                .eq(TOrderRelationship::getOrderId, orderId)
                .isNotNull(TOrderRelationship::getReceiptUserId)
                .groupBefore().eq(TOrderRelationship::getOrderReportType , OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType())
                .or().eq(TOrderRelationship::getOrderReportType , OrderRelationshipEnum.ORDER_REPORT_IS_SOLVE.getType()).groupAfter()
                .eq(TOrderRelationship::getStatus , status));
        return  count;
    }
    /*public long updateByOrderRelationshipList(List<TOrderRelationship> orderRelationshipList){
        long update = MybatisOperaterUtil.getInstance().update(orderRelationshipList,)
    }*/

    /**
     * @return com.e_commerce.miscroservice.order.po.TOrderRelationship
     * @Author 姜修弘
     * 功能描述:根据参与者id和orderId查询参与者订单
     * 创建时间:@Date 下午3:33 2019/3/8
     * @Param [orderId, userId]
     **/
    public TOrderRelationship selectOrderRelationshipByJoinIn(Long orderId, Long userId) {
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId, orderId)
                        .eq(TOrderRelationship::getReceiptUserId, userId)
                        .in(TOrderRelationship::getStatus, participationStatusList()));
        return orderRelationship;
    }

    /**
     * 批量更新订单关系表
     *
     * @param orderRelationshipList
     * @param orderRelationgshipIdList
     * @return
     */
    public long updateOrderRelationshipByList(List<TOrderRelationship> orderRelationshipList, List<Long> orderRelationgshipIdList) {
        long count = MybatisOperaterUtil.getInstance().update(orderRelationshipList,
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .in(TOrderRelationship::getId, orderRelationgshipIdList));
        return count;
    }

    @Override
    public List<TOrderRelationship> listRelationshipByUserId(Long userId) {
        //非订单的状态
//		List<Integer> noOrderStatus = new ArrayList<>();
//		noOrderStatus.add(OrderRelationshipEnum.STATUS_NO_STATE.getType());
//		noOrderStatus.add(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
        return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
                .groupBefore().eq(TOrderRelationship::getReceiptUserId, userId).neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_REMOVE_ENROLL.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType()).groupAfter()
                .or().groupBefore().eq(TOrderRelationship::getFromUserId, userId).groupAfter()
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrderRelationship::getCreateTime)));
    }

    @Override
    public List<TOrderRelationship> getReceiver(Long orderId) {
        return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
                .isNotNull(TOrderRelationship::getReceiptUserId).neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_REMOVE_ENROLL.getType())
                .neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType()));
    }

    /**
     * 参与的订单的状态
     *
     * @return
     */
    private List<Integer> participationStatusList() {
        List<Integer> orderRelationshipStatusList = new ArrayList<>();
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_WAIT_REMARK.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_SERVER_REMARK.getType());
        orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_HELPER_REMARK.getType());
        return orderRelationshipStatusList;
    }


}
