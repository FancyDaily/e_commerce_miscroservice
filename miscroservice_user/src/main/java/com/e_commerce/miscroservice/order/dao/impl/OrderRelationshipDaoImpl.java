package com.e_commerce.miscroservice.order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TOrderRelationship;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.mapper.OrderRelationshipMapper;
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
						.groupBefore().eq(TOrderRelationship::getReceiptUserId, userId).or()
						.groupBefore()
						.eq(TOrderRelationship::getFromUserId, userId)
						.isNull(TOrderRelationship::getReceiptUserId)
						.groupAfter()
						.groupAfter()
						.eq(TOrderRelationship::getOrderId, orderId)
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
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
						.groupBefore().eq(TOrderRelationship::getReceiptUserId, userId).or()
						.groupBefore()
						.eq(TOrderRelationship::getFromUserId, userId)
						.isNull(TOrderRelationship::getReceiptUserId)
						.groupAfter()
						.groupAfter()
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
		return orderRelationshipList;
	}

	/**
	 * 根据用户id (发布者和参与者) 来查询订单关系List
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List<TOrderRelationship> selectEndByUserId(Long userId) {
		List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
				new MybatisSqlWhereBuild(TOrderRelationship.class)
						.groupBefore().eq(TOrderRelationship::getReceiptUserId, userId).or()
						.groupBefore()
						.eq(TOrderRelationship::getFromUserId, userId)
						.isNull(TOrderRelationship::getReceiptUserId)
						.groupAfter()
						.groupAfter()
						.eq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_IS_COMPLETED.getType())
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
		return orderRelationshipList;
	}

	/**
	 * 报名选人列表
	 *
	 * @param pageNum  分页页数
	 * @param pageSize 每页数量
	 * @param userId   当前用户id
	 * @return
	 */
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
	 * 查询指定接单者,排除指定状态的订单记录
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List<TOrderRelationship> selectOrderRelationshipByReceiptUserIdNotEqStatus(Long userId, Integer status) {
		return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
				.eq(TOrderRelationship::getReceiptUserId, userId)
				.neq(TOrderRelationship::getStatus,status)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));

	}

	/**
	 * 查询用户收藏的订单关系记录
	 *
	 * @param id
	 * @return
	 */
	@Override
	public List<TOrderRelationship> selectCollectList(Long id) {
		return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
				.groupBefore().eq(TOrderRelationship::getReceiptUserId, id).or()
				.groupBefore()
				.eq(TOrderRelationship::getFromUserId, id)
				.isNull(TOrderRelationship::getReceiptUserId)
				.groupAfter()
				.groupAfter()
				.eq(TOrderRelationship::getServiceCollectionType, OrderRelationshipEnum.SERVICE_COLLECTION_IS_TURE.getType())
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
				.orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrderRelationship::getServiceCollectionTime))
		);
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
	public int updateCollectStatus(Long orderRelationshipId, int collectStatus) {//TODO 等收藏时间上来来 要更新收藏时间
		TOrderRelationship orderRelationship = new TOrderRelationship();
		orderRelationship.setId(orderRelationshipId);
		orderRelationship.setServiceCollectionType(collectStatus);
		return MybatisOperaterUtil.getInstance().update(orderRelationship, new MybatisSqlWhereBuild(TOrderRelationship.class)
				.eq(TOrderRelationship::getId, orderRelationshipId));
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
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
						.orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrderRelationship::getCreateTime))
		);
		return orderRelationshipList;
	}

	/**
	 * 根据orderId和status来查询未投诉报名者订单List
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
						.eq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType())
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
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
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
						.orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrderRelationship::getCreateTime))
		);
		return orderRelationshipList;
	}

	/**
	 * 根据status来查询未投诉参与者订单数量
	 *
	 * @param orderId
	 * @param status
	 * @return
	 */
	public long selectCountByStatusByEnroll(Long orderId, Integer status) {
		long count = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
				.count(TOrderRelationship::getId)
				.eq(TOrderRelationship::getOrderId, orderId)
				.isNotNull(TOrderRelationship::getReceiptUserId)
				.eq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType())
				.eq(TOrderRelationship::getStatus, status)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
		return count;
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
						.in(TOrderRelationship::getStatus, participationStatusList())
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
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


	/**
	 * 查看完成的（支付的）用户的数量
	 *
	 * @param orderId
	 * @return
	 */
	public long selectCompleteUserSum(Long orderId) {
		long count = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
				.count(TOrderRelationship::getId)
				.eq(TOrderRelationship::getOrderId, orderId)
				.isNotNull(TOrderRelationship::getReceiptUserId)
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_REMOVE_ENROLL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_UNDER_WAY.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_WAIT_PAY.getType())
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
		return count;
	}

	@Override
	public List<TOrderRelationship> pageChooseList(Long userId) {
		return relationshipMapper.getChooseList(userId);
//        return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
//                .eq(TOrderRelationship::getFromUserId, userId).isNull(TOrderRelationship::getReceiptUserId));
	}

	@Override
	public Long countWaitPay(Long orderId) {
		return MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
				.eq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType()));
	}

	@Override
	public List<TOrderRelationship> selectWaitPay(Long orderId) {
		return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
				.eq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType())
				.neq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType())
				.neq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType())
				.neq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType()));
	}

	/**
	 * 查询用户发布或报名的成立的订单关系
	 *
	 * @param userId
	 * @return
	 */
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
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType()).groupAfter()
				.or().groupBefore().eq(TOrderRelationship::getFromUserId, userId).isNull(TOrderRelationship::getReceiptUserId)
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType()).groupAfter()
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
				.orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrderRelationship::getCreateTime))
		);
	}

	/**
	 * 查询成立的订单关系
	 *
	 * @param orderId 订单ID
	 * @return
	 */
	@Override
	public List<TOrderRelationship> getReceiver(Long orderId) {
		return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
				.isNotNull(TOrderRelationship::getReceiptUserId)
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_REMOVE_ENROLL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType())
				.eq(TOrderRelationship::getOrderId, orderId)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
	}

	/**
	 * 可以取消订单的报名者关系表
	 *
	 * @param orderId
	 * @return
	 */
	public List<TOrderRelationship> selectCanRemoveEnrollUser(Long orderId) {
		return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
				.isNotNull(TOrderRelationship::getReceiptUserId)
				.eq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType())
				.eq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType())
				.eq(TOrderRelationship::getOrderId, orderId)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
	}

	/**
	 * 根据orderId来查询未投诉参与者订单
	 *
	 * @param orderId
	 * @return
	 */
	public List<TOrderRelationship> selectOrderRelaByStatusByEnrollNoReport(Long orderId) {
		return MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
				new MybatisSqlWhereBuild(TOrderRelationship.class)
						.eq(TOrderRelationship::getOrderId, orderId)
						.isNotNull(TOrderRelationship::getReceiptUserId)
						.eq(TOrderRelationship::getOrderReportType, OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType())
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES));
	}


	/**
	 * 查询被选择
	 *
	 * @param orderId
	 * @return
	 */
	public long selectJoinUser(Long orderId) {
		return MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
				.count(TOrderRelationship::getId)
				.isNotNull(TOrderRelationship::getReceiptUserId)
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType())
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_REMOVE_ENROLL.getType())
				.eq(TOrderRelationship::getIsValid, AppConstant.ACCREDIT_STATUS_YES));
	}


	/**
	 * 根据orderId和statusList来升序查询在userIdList里的报名者订单List
	 *
	 * @param orderId
	 * @param statusList
	 * @return
	 */
	public List<TOrderRelationship> selectListByStatusListByEnrollInUserList(Long orderId, List<Integer> statusList, List<Long> userIdList) {
		List<TOrderRelationship> orderRelationshipList = MybatisOperaterUtil.getInstance().finAll(new TOrderRelationship(),
				new MybatisSqlWhereBuild(TOrderRelationship.class)
						.eq(TOrderRelationship::getOrderId, orderId)
						.in(TOrderRelationship::getStatus, statusList)
						.in(TOrderRelationship::getReceiptUserId, userIdList)
						.isNotNull(TOrderRelationship::getReceiptUserId)
						.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
						.orderBy(MybatisSqlWhereBuild.OrderBuild.buildAsc(TOrderRelationship::getCreateTime))
		);
		return orderRelationshipList;
	}

	/**
	 * 查询报名者数量
	 *
	 * @param orderId
	 * @return
	 */
	public long selectEnrollUserCount(Long orderId) {
		return MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TOrderRelationship.class)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
				.neq(TOrderRelationship::getStatus, OrderRelationshipEnum.STATUS_NO_STATE.getType())
				.isNotNull(TOrderRelationship::getReceiptUserId));
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
		orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_IS_REMARK.getType());
		orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_BE_REMARK.getType());
		orderRelationshipStatusList.add(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType());
		return orderRelationshipStatusList;
	}

	@Override
	public TOrderRelationship selectCollectByOrderIdAndUserId(Long orderId, Long id) {
		return MybatisOperaterUtil.getInstance().findOne(new TOrderRelationship(), new MybatisSqlWhereBuild(TOrderRelationship.class)
				.groupBefore().eq(TOrderRelationship::getReceiptUserId, id).or()
				.groupBefore()
				.eq(TOrderRelationship::getFromUserId, id)
				.isNull(TOrderRelationship::getReceiptUserId)
				.groupAfter()
				.groupAfter()
				.eq(TOrderRelationship::getOrderId, orderId)
				.eq(TOrderRelationship::getIsValid, AppConstant.IS_VALID_YES)
				.orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TOrderRelationship::getServiceCollectionTime))
		);
	}


}
