package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
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

    /**
     *根据主键查询订单关系表
     * @param orderRelationshipId
     * @return
     */
    public TOrderRelationship selectByPrimaryKey(Long orderRelationshipId){
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getId,orderRelationshipId));
        return orderRelationship;
    }

    /**
     *插入订单关系表
     * @param orderRelationship
     * @return
     */
    public int insert(TOrderRelationship orderRelationship){
        int save = MybatisOperaterUtil.getInstance()
                .save(orderRelationship);
        return save;
    }

    /**
     *根据主键更新订单关系表
     * @param orderRelationship
     * @return
     */
    public int updateByPrimaryKey(TOrderRelationship orderRelationship){
        int update = MybatisOperaterUtil.getInstance().update(orderRelationship,
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getId,orderRelationship.getId()));
        return update;
    }

    /**
     * 根据日期找到参与的订单关系
     * @param startTime
     * @param endTime
     * @param serviceId
     * @param userId
     * @return
     */
    public TOrderRelationship selectByDate(Long startTime , Long endTime , Long serviceId , Long userId){
        List<Integer> orderRelationshipStatusList = participationStatusList();
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getServiceId , serviceId)
                        .gte(TOrderRelationship::getStartTime , startTime)
                        .lte(TOrderRelationship::getStartTime , endTime)
                        .eq(TOrderRelationship::getReceiptUserId , userId)
                        .in(TOrderRelationship::getStatus , orderRelationshipStatusList));
        return  orderRelationship;
    }

    /**
     * 根据订单id和用户id来查询订单关系
     * @param orderId
     * @param userId
     * @return
     */
    public TOrderRelationship selectByOrderIdAndUserId(Long orderId , Long userId){
        TOrderRelationship orderRelationship = MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId , orderId)
                        .eq(TOrderRelationship::getReceiptUserId , userId));
        return  orderRelationship;
    }
    /**
     * 根据订单id和用户idList来查询订单关系List
     * @param orderId
     * @param userIdList
     * @return
     */
    public List<TOrderRelationship> selectByOrderIdAndUserIdList(Long orderId , List<Long> userIdList){
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId , orderId)
                        .in(TOrderRelationship::getReceiptUserId , userIdList));
        return  orderRelationshipList;
    }

    /**
     * 根据用户id来查询订单关系List
     * @param userId
     * @return
     */
    public List<TOrderRelationship> selectByUserId(Long userId){
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .groupBefore().eq(TOrderRelationship::getFromUserId , userId)
                            .or().eq(TOrderRelationship::getFromUserId , userId)
                        .groupAfter()
                        .eq(TOrderRelationship::getIsValid , "1"));
        return  orderRelationshipList;
    }

    /**
     * 根据orderId和statusList来查询订单List
     * @param orderId
     * @param statusList
     * @return
     */
    public List<TOrderRelationship> selectListByStatusList(Long orderId , List<Integer> statusList){
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId , orderId)
                        .in(TOrderRelationship::getStatus , statusList)
                        .orderBy(MybatisSqlWhereBuild.ORDER.ASC,TOrderRelationship::getCreateTime));
        return  orderRelationshipList;
    }
    /**
     * 根据orderId和status来查询订单List
     * @param orderId
     * @param status
     * @return
     */
    public List<TOrderRelationship> selectListByStatus(Long orderId , int status){
        List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
                new MybatisSqlWhereBuild(TOrderRelationship.class)
                        .eq(TOrderRelationship::getOrderId , orderId)
                        .eq(TOrderRelationship::getStatus , status)
                        .orderBy(MybatisSqlWhereBuild.ORDER.ASC,TOrderRelationship::getCreateTime));
        return  orderRelationshipList;
    }
    /**
     * 根据statusList来查询订单数量
     * @param orderId
     * @param statusList
     * @return
     */
    public long selectCountByStatusList(Long orderId , List<Integer> statusList){
        long count = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
                .count(TOrderRelationship::getId)
                .eq(TOrderRelationship::getOrderId , orderId)
                .in(TOrderRelationship::getStatus , statusList));
        return  count;
    }
    /**
     * 参与的订单的状态
     * @return
     */
    private List<Integer> participationStatusList(){
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
