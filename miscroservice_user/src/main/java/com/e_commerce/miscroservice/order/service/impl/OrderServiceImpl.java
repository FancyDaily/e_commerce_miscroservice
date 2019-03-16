package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.dao.OrderRecordDao;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.service.OrderRelationService;
import com.e_commerce.miscroservice.order.service.OrderService;
import com.e_commerce.miscroservice.order.vo.*;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.e_commerce.miscroservice.product.util.DateResult;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

	@Autowired
	ProductCommonController productService;
	@Autowired
	OrderRelationshipDao orderRelationshipDao;
	@Autowired
	UserCommonController userService;
	@Autowired
	OrderRelationService orderRelationService;
	@Autowired
	OrderRecordDao orderRecordDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int saveOrder(TOrder order) {
		/*
		 * 已完成的订单是可以显示的 可见状态还是为1
		 * 正常状态为1的订单 只能有一条可见的订单 只能有一套visiable的订单 到完成时间的时候改状态并且改可见状态
		 * 查看之前是否有能可见的订单
		 * 		—— 如果有的话，进行判断哪一条的开始时间在前
		 * 				—— 数据库的那条在前，新插入的就不可见
		 * 			 	—— 新插入的时间在前，数据库那条就不可见，新插入的就可见
		 *		—— 如果没有的话，新插入的就是可见的
		 */
		TOrder visiableOrder = orderDao.selectVisiableOrder(order.getServiceId());
		if (visiableOrder != null) { //之前有可见的订单
			if (visiableOrder.getEndTime() < order.getEndTime()) {// 数据库那条在前,当前插入的为不可见
				order.setVisiableStatus(OrderEnum.VISIABLE_NO.getStringValue());
			} else { // 当前这条在前， 数据库那条可见状态改为不可见
				order.setVisiableStatus(OrderEnum.VISIABLE_YES.getStringValue());
				visiableOrder.setVisiableStatus(OrderEnum.VISIABLE_NO.getStringValue());
				orderDao.updateByPrimaryKey(visiableOrder);
			}
		} else { //新插入的是可见的
			order.setVisiableStatus(OrderEnum.VISIABLE_YES.getStringValue());
		}
		orderDao.saveOneOrder(order);
		// 只有求助并且是互助时才冻结订单
		if (order.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue()) && order.getCollectType().equals(ProductEnum.COLLECT_TYPE_EACHHELP.getValue())) {
			userService.freezeTimeCoin(order.getCreateUser(), order.getCollectTime() * order.getServicePersonnel(), order.getServiceId(), order.getServiceName());
		}
		return orderRelationService.addTorderRelationship(order);
	}

	@Override
	public QueryResult<PageOrderReturnView> list(PageOrderParamView param, TUser user) {
		QueryResult<PageOrderReturnView> result = new QueryResult<>();
		List<PageOrderReturnView> listReturn = new ArrayList<>();
		// 根据当前用户的组织查看当前用户是否有权限查看
		List<Long> companyIds = new ArrayList<>();
		if (user != null) { // 如果当前用户是登录状态，则查看当前用户加入了哪些组织
			//TODO 调用用户模块
//			companyIds = userCompanyDao.selectCompanyIdByUser(user.getId());
//			param.setCurrentUserId(user.getId());
		}
		param.setUserCompanyIds(companyIds);
		//搜索条件
		String condition = param.getCondition();
		//特殊字符替换
		if (StringUtil.isNotEmpty(condition)) {
			//英文括号需要转义
			String replaceCondition = condition.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "")
					.replaceAll("（", "").replaceAll("）", "");
			param.setCondition(replaceCondition);
		}
		// 根据条件分页所有求助服务的订单
		Page<TOrder> page = PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<TOrder> listOrder = orderDao.pageOrder(param);
		// 装载要查询的商品ID
		List<Long> serviceIds = new ArrayList<>();
		// 装载所有需要查询的用户id
		List<Long> userIds = new ArrayList<>();
		for (int i = 0; i < listOrder.size(); i++) {
			userIds.add(listOrder.get(i).getCreateUser());
			serviceIds.add(listOrder.get(i).getServiceId());
		}
		if (userIds.size() == 0 || serviceIds.size() == 0) {
			result.setResultList(listReturn);
			result.setTotalCount(0L);
			return result;
		}
		// 获取商品封面图
		Map<Long, String> productCoverPic = productService.getProductCoverPic(serviceIds);
		for (int i = 0; i < listOrder.size(); i++) {
			PageOrderReturnView returnView = new PageOrderReturnView();
			TOrder order = listOrder.get(i);
			returnView.setOrder(order);
			//设置封面图
			returnView.setImgUrl(productCoverPic.get(order.getServiceId()));
			TUser tUser = userService.getUserById(order.getCreateUser());
			BaseUserView userView = BeanUtil.copy(tUser, BaseUserView.class);
			returnView.setUser(userView);
			// 用户类型
			listReturn.add(returnView);
		}
		result.setResultList(listReturn);
		result.setTotalCount(page.getTotal());
		return result;
	}

	@Override
	public DetailOrderReturnView orderDetail(Long orderId, TUser user) {
		DetailOrderReturnView returnView = new DetailOrderReturnView();
		TOrder order = orderDao.selectByPrimaryKey(orderId);
		returnView.setOrder(order);
		Long publisherId = order.getCreateUser();
		TUser tUser = userService.getUserById(publisherId);
		BaseUserView userView = BeanUtil.copy(tUser, BaseUserView.class);
		// 求助 展示求助者评分
		if (order.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
			userView.setTotalEvaluate(tUser.getServTotalEvaluate());
			userView.setServeNum(tUser.getServeNum());
		} else {
			userView.setTotalEvaluate(tUser.getHelpTotalEvaluate());
			userView.setServeNum(tUser.getSeekHelpNum());
		}
		// TODO 获取发布者关注信息
		// 关注状态 1、显示关注 2、显示已关注
		userView.setCareStatus(1);
		returnView.setUser(userView);
		returnView.getUser().setCareStatus(1);
		//详情页面展示举报状态 收藏状态 报名状态
		if (user != null) { // 当前用户是登录状态
			TOrderRelationship tOrderRelationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, user.getId());
			if (tOrderRelationship == null) {
				tOrderRelationship.setServiceReportType(OrderRelationshipEnum.STATUS_NO_STATE.getType());
				tOrderRelationship.setStatus(OrderRelationshipEnum.STATUS_NO_STATE.getType());
				tOrderRelationship.setServiceCollectionType(OrderRelationshipEnum.SERVICE_COLLECTION_IS_NO.getType());
			}
			returnView.setOrderRelationship(tOrderRelationship);
		} else {
			TOrderRelationship orderRelationship = new TOrderRelationship();
			orderRelationship.setStatus(OrderRelationshipEnum.STATUS_NO_STATE.getType());
			orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType());
			orderRelationship.setServiceCollectionType(OrderRelationshipEnum.SERVICE_COLLECTION_IS_NO.getType());
			returnView.setOrderRelationship(orderRelationship);
		}
		List<TServiceDescribe> listDesc = productService.getProductDesc(order.getServiceId());
		//将详情汇中的封面图取出来
		for (int i = 0; i < listDesc.size(); i++) {
			if (listDesc.get(i).getIsCover().equals(IS_COVER_YES)) {
				//将封面图取出来单独存
				returnView.setCoverImgUrl(listDesc.get(i).getUrl());
			}
		}
		returnView.setListServiceDescribe(listDesc);
		return returnView;
	}

	@Override
	public QueryResult<PageEnrollAndChooseReturnView> enrollList(Integer pageNum, Integer pageSize, TUser user) {
		QueryResult<PageEnrollAndChooseReturnView> result = new QueryResult<>();
		// TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		List<PageEnrollAndChooseReturnView> listReturnView = new ArrayList<>();
		Page<TOrderRelationship> page = PageHelper.startPage(pageNum, pageSize);
		List<TOrderRelationship> pageEnrollAndChooseList = orderRelationshipDao.pageEnrollAndChooseList(pageNum, pageSize, user.getId());
		if (pageEnrollAndChooseList.size() == 0) {
			result.setResultList(new ArrayList<>());
			result.setTotalCount(0L);
			return result;
		}
		//所有的orderid
		List<Long> orderIds = new ArrayList<>();
		//所有的商品ID
		List<Long> serviceIds = new ArrayList<>();
		for (TOrderRelationship orderRelationship : pageEnrollAndChooseList) {
			orderIds.add(orderRelationship.getOrderId());
			serviceIds.add(orderRelationship.getServiceId());
		}
		//订单列表的map  key：orderid  value: order
		Map<Long, TOrder> orderMap = new HashMap<>();
		//订单列表
		List<TOrder> listOrder = orderDao.selectOrderByOrderIds(orderIds);
		for (TOrder tOrder : listOrder) {
			orderMap.put(tOrder.getId(), tOrder);
		}
		//商品封面图
		Map<Long, String> productCoverPic = productService.getProductCoverPic(serviceIds);
		//遍历组织view
		for (TOrderRelationship orderRelationship : pageEnrollAndChooseList) {
			PageEnrollAndChooseReturnView returnView = new PageEnrollAndChooseReturnView();
			//显示的订单列表
			returnView.setOrder(orderMap.get(orderRelationship.getOrderId()));
			//显示的封面url
			returnView.setPorductCoverPic(productCoverPic.get(orderRelationship.getServiceId()));
			//根据当前用户是发布者及接单者判断显示状态
			getEnrollChooseShowStatus(user, orderRelationship, returnView);
			listReturnView.add(returnView);
		}
		result.setResultList(listReturnView);
		result.setTotalCount(page.getTotal());
		return result;
	}

	@Override
	public void synOrderServiceStatus(Long productId, Integer status) {
		orderDao.updateByServiceId(productId, status);
	}

	@Override
	public QueryResult<PageOrderReturnView> listMineOrder(Integer pageNum, Integer pageSize, TUser user) {
		// TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		QueryResult<PageOrderReturnView> result = new QueryResult<>();
		List<PageOrderReturnView> listReturnView = new ArrayList<>();
		//列出当前用户的所有订单关系
		Page<TOrderRelationship> page = PageHelper.startPage(pageNum, pageSize);
		List<TOrderRelationship> listOrderRelationship = orderRelationshipDao.listRelationshipByUserId(user.getId());
		if (listOrderRelationship.size() == 0) {
			result.setResultList(new ArrayList<>());
			result.setTotalCount(0L);
			return result;
		}

		List<Long> orderIds = new ArrayList<>();
		List<Long> productIds = new ArrayList<>();
		listOrderRelationship.stream().forEach(relationship -> {
			orderIds.add(relationship.getOrderId());
			productIds.add(relationship.getServiceId());
		});
		// 获取所有封面图信息  商品 --> 封面图
		Map<Long, String> productCoverPic = productService.getProductCoverPic(productIds);
		//查询报名关系表关联的所有订单
		List<TOrder> listOrder = orderDao.selectOrderByOrderIds(orderIds);
		//获取order的map  key-> orderId  value-> order
		Map<Long, TOrder> orderMap = new HashMap<>();
		listOrder.stream().forEach(order -> orderMap.put(order.getId(), order));
		for (TOrderRelationship relationship : listOrderRelationship) {
			TOrder order = orderMap.get(relationship.getOrderId());
			PageOrderReturnView returnView = new PageOrderReturnView();
			returnView.setOrder(order);
			returnView.setImgUrl(productCoverPic.get(relationship.getServiceId()));
			listReturnView.add(returnView);
			if (relationship.getReceiptUserId() == null) { // 当前用户是发布者
				if (relationship.getServiceType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {//当前用户是求助者
					//判断订单的状态
					if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
						// 这是求助   已被选中的状态 作为发布者，显示待支付
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_PAY.getValue());
					}
				} else { // 当前用户是服务者
					//查看未签到的报名者
					if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_PAY.getValue());
					}
				}
				if (Objects.equals(11, relationship.getStatus())) { //已评价 待对方评价
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_REMARK.getValue());
				} else if (Objects.equals(12, relationship.getStatus()) || Objects.equals(9, relationship.getStatus())) { // 待评价
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_REMARK.getValue());
				} else if (Objects.equals(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType(), relationship.getStatus())
						|| Objects.equals(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType(), relationship.getStatus())) {
					//已完成
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_ALREADY_END.getValue());
				} else if (Objects.equals(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType(), relationship.getStatus())) {
					// 发布者已取消
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS__ALREADY_CANCEL.getValue());
				}
			} else { //当前用户是接单者
				//对接单者来说 发布者发布的是求助  接单者就是服务者  就是一条服务记录 反之亦然
				if (order.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
					order.setType(ProductEnum.TYPE_SERVICE.getValue());
				} else {
					order.setType(ProductEnum.TYPE_SEEK_HELP.getValue());
				}
				if (order.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {//当前用户是求助者
					//判断订单的状态
					if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_PAY.getValue());
					}
				} else { // 当前用户是服务者
					if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
						// 接单者，需要查看是否签到 签到之后是待对方支付  没有签到是待开始
						if (Objects.equals(relationship.getSignType(), OrderRelationshipEnum.SIGN_TYPE_NO.getType())) { // 未签到
							returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_BEGIN.getValue());
						} else { // 已经签到，待对方支付
							returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_PAY.getValue());
						}
					}
				}
				if (Objects.equals(11, relationship.getStatus())) { //已评价 待对方评价
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_REMARK.getValue());
				} else if (Objects.equals(12, relationship.getStatus()) || Objects.equals(9, relationship.getStatus())) { // 待评价
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_REMARK.getValue());
				} else if (Objects.equals(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType(), relationship.getStatus())
						|| Objects.equals(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType(), relationship.getStatus())) {
					//已完成
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_ALREADY_END.getValue());
				} else if (Objects.equals(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType(), relationship.getStatus())) {
					// 发布者已取消,当前是接单者 所以是被取消
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_OTHER_CANCEL.getValue());
				} else if (Objects.equals(OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType(), relationship.getStatus())) {
					// 接单者已取消， 当前是接单者 所以是已取消
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS__ALREADY_CANCEL.getValue());
				}
				//如果双方有人举报过 显示投诉中
				if (Objects.equals(relationship.getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType())
						|| Objects.equals(relationship.getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType())
						|| Objects.equals(relationship.getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType())) {
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_COMPLAINT.getValue());
				}
				//对接单者来说  这条订单对应一个人  显示人数为1
				order.setServicePersonnel(1);
			}
			if (order.getConfirmNum().equals(0)) {
				returnView.setStatus(0);
			}
		}
		result.setResultList(listReturnView);
		result.setTotalCount(page.getTotal());
		return result;
	}

	@Override
	public DetailMineOrderReturnView detailMineOrder(TUser user, Long orderId) {
		DetailMineOrderReturnView returnView = new DetailMineOrderReturnView();
		//TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		TOrderRelationship relationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, user.getId());
		TOrder tOrder = orderDao.selectByPrimaryKey(orderId);
		returnView.setOrder(tOrder);
		if (tOrder.getConfirmNum() == 0) {
			//没有一个用户  只显示订单就可以
			returnView.setStatus(0);
			returnView.setRecord(new ArrayList<>());
			returnView.setListUserView(new ArrayList<>());
			returnView.setListDesc(new ArrayList<>());
			return returnView;
		}
		// 待支付前为投诉 待支付后为举报 1、投诉 2、举报
		if (Objects.equals(relationship.getStatus(), OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType())) {
			returnView.setReportAction(1);
		} else {
			returnView.setReportAction(2);
		}
		List<TOrderRecord> listOrderRecord = orderRecordDao.selectRecordByOrderId(orderId);
		returnView.setRecord(listOrderRecord);
		// 获取详情
		List<TServiceDescribe> productDesc = productService.getProductDesc(tOrder.getServiceId());
		returnView.setListDesc(productDesc);
		if (Objects.equals(user.getId(), tOrder.getCreateUser())) { //当前用户是发布者
			//获取所有接单用户的订单关系
			List<TOrderRelationship> listRelationship = orderRelationshipDao.getReceiver(orderId);
			if (tOrder.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {//当前用户是求助者
				//判断订单的状态
				if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
					// 这是求助   已被选中的状态 作为发布者，显示待支付
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_PAY.getValue());
				}
			} else { // 当前用户是服务者
				//查看未签到的报名者
				List<TOrderRelationship> signNoRelationship = listRelationship.stream().filter(ship -> Objects.equals(ship.getSignType(), OrderRelationshipEnum.SIGN_TYPE_NO.getType()))
						.collect(Collectors.toList());
				if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
					// 用户是服务者
					// 作为发布者 查看是否有人需要开始， 有的话就显示待开始  没有的话就显示待对方支付
					if (signNoRelationship.size() == 0) { // 没有未签到的，显示待对方支付
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_PAY.getValue());
					} else { // 有未签到的,显示待开始
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_BEGIN.getValue());
					}
				}
			}
			if (Objects.equals(11, relationship.getStatus())) { //已评价 待对方评价
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_REMARK.getValue());
			} else if (Objects.equals(12, relationship.getStatus())) { // 待评价
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_REMARK.getValue());
			} else if (Objects.equals(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType(), relationship.getStatus())
					|| Objects.equals(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType(), relationship.getStatus())) {
				//已完成
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_ALREADY_END.getValue());
			} else if (Objects.equals(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType(), relationship.getStatus())) {
				// 发布者已取消
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS__ALREADY_CANCEL.getValue());
			}
			//如果只有一个人并且双方有人举报过 显示投诉中
			if (listRelationship.size() == 1 && (Objects.equals(listRelationship.get(0).getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType())
					|| Objects.equals(listRelationship.get(0).getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType())
					|| Objects.equals(listRelationship.get(0).getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType()))) {
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_COMPLAINT.getValue());
			}
			// 如果接单者只有一个人的话 需要显示他的分数
			if (listRelationship.size() == 1) {
				Long userId = listRelationship.get(0).getReceiptUserId();
				TUser receiver = userService.getUserById(userId);
				BaseUserView userView = BeanUtil.copy(receiver, BaseUserView.class);
				// TODO 查询用户的关注状态
				userView.setCareStatus(1);
				// 求助 展示求助者评分
				if (tOrder.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
					userView.setTotalEvaluate(receiver.getServTotalEvaluate());
					userView.setServeNum(receiver.getServeNum());
				} else {
					userView.setTotalEvaluate(receiver.getHelpTotalEvaluate());
					userView.setServeNum(receiver.getSeekHelpNum());
				}
				List<BaseUserView> listUserView = new ArrayList<>();
				listUserView.add(userView);
				returnView.setListUserView(listUserView);
			} else {
				List<BaseUserView> listUserView = new ArrayList<>();
				// 对接单者进行基本用户信息的映射
				for (TOrderRelationship tOrderRelationship : listRelationship) {
					TUser receiver = userService.getUserById(tOrderRelationship.getReceiptUserId());
					BaseUserView userView = BeanUtil.copy(receiver, BaseUserView.class);
					// TODO 查询用户的关注状态
					userView.setCareStatus(1);
//				userView.setPointStatus(1);
				}
				returnView.setListUserView(listUserView);
			}
		} else { //当前用户是接单者
			//对接单者来说 发布者发布的是求助  接单者就是服务者  就是一条服务记录 反之亦然
			if (tOrder.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
				tOrder.setType(ProductEnum.TYPE_SERVICE.getValue());
			} else {
				tOrder.setType(ProductEnum.TYPE_SEEK_HELP.getValue());
			}
			if (tOrder.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {//当前用户是求助者
				//判断订单的状态
				if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
					// 当前用户是求助者
					returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_PAY.getValue());
				}
			} else { // 当前用户是服务者
				if (Objects.equals(OrderRelationshipEnum.STATUS_ALREADY_CHOOSE.getType(), relationship.getStatus())) {
					// 接单者，需要查看是否签到 签到之后是待对方支付  没有签到是待开始
					if (Objects.equals(relationship.getSignType(), OrderRelationshipEnum.SIGN_TYPE_NO.getType())) { // 未签到
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_BEGIN.getValue());
					} else { // 已经签到，待对方支付
						returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_PAY.getValue());
					}
				}
			}
			if (Objects.equals(11, relationship.getStatus())) { //已评价 待对方评价
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_OTHER_REMARK.getValue());
			} else if (Objects.equals(12, relationship.getStatus())) { // 待评价
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_WAIT_REMARK.getValue());
			} else if (Objects.equals(OrderRelationshipEnum.STATUS_IS_COMPLETED.getType(), relationship.getStatus())
					|| Objects.equals(OrderRelationshipEnum.STATUS_NOT_ESTABLISHED.getType(), relationship.getStatus())) {
				//已完成
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_ALREADY_END.getValue());
			} else if (Objects.equals(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType(), relationship.getStatus())) {
				// 发布者已取消,当前是接单者 所以是被取消
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_OTHER_CANCEL.getValue());
			} else if (Objects.equals(OrderRelationshipEnum.STATUS_ENROLL_CANCEL.getType(), relationship.getStatus())) {
				// 接单者已取消， 当前是接单者 所以是已取消
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS__ALREADY_CANCEL.getValue());
			}
			//如果双方有人举报过 显示投诉中
			if (Objects.equals(relationship.getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_IS_TURE.getType())
					|| Objects.equals(relationship.getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_IS_BEREPORT.getType())
					|| Objects.equals(relationship.getOrderReportType(), OrderRelationshipEnum.ORDER_REPORT_EACH_OTHER.getType())) {
				returnView.setStatus(OrderEnum.DETAIL_SHOW_STATUS_COMPLAINT.getValue());
			}
			//对接单者来说  这条订单对应一个人  显示人数为1
			tOrder.setServicePersonnel(1);
			Long fromUserId = relationship.getFromUserId();
			TUser tUser = userService.getUserById(fromUserId);
			BaseUserView userView = BeanUtil.copy(user, BaseUserView.class);
			// TODO 查询用户的关注状态
			userView.setCareStatus(1);
			// 求助 展示求助者评分
			if (tOrder.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
				userView.setTotalEvaluate(tUser.getServTotalEvaluate());
				userView.setServeNum(tUser.getServeNum());
			} else {
				userView.setTotalEvaluate(tUser.getHelpTotalEvaluate());
				userView.setServeNum(tUser.getSeekHelpNum());
			}
			List<BaseUserView> listUserView = new ArrayList<>();
			listUserView.add(userView);
			returnView.setListUserView(listUserView);
		}
		return returnView;
	}

	@Override
	public DetailChooseReturnView chooseDetail(Long orderId, TUser user) {
		TOrder order = orderDao.selectByPrimaryKey(orderId);
		//所有的报名者
		List<TOrderRelationship> tOrderRelationships = orderRelationshipDao.selectListByStatusByEnroll(orderId, OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType());
		List<BaseUserView> listUser = new ArrayList<>();
		for (TOrderRelationship tOrderRelationship : tOrderRelationships) {
			TUser tUser = userService.getUserById(tOrderRelationship.getReceiptUserId());
			BaseUserView userView = BeanUtil.copy(tUser, BaseUserView.class);
			userView.setCareStatus(1);
			listUser.add(userView);
		}
		DetailChooseReturnView result = new DetailChooseReturnView();
		result.setOrder(order);
		result.setListUser(listUser);
		return result;

	}

	@Override
	public void changeOrderVisiableStatus(Long orderId, Integer type) {
		TOrder tOrder = orderDao.selectByPrimaryKey(orderId);
		if (type == 1) { // 盈到亏 满人到少人的时候
			TOrder order = orderDao.selectVisiableOrder(tOrder.getServiceId());
			if (order == null) { //数据库没有
				tOrder.setVisiableStatus(OrderEnum.VISIABLE_YES.getStringValue());
				orderDao.updateByPrimaryKey(tOrder);
			}
			//数据库有其他可见， 那么不做修改
		} else { //亏到盈 满人的时候
			String visiableStatus = tOrder.getVisiableStatus();
			if (Objects.equals(OrderEnum.VISIABLE_YES.getStringValue(), visiableStatus)) {
				TOrder order = orderDao.selectNearNotVisiable(tOrder.getServiceId());
			} else { // 满人的订单为不可见 现在置位可见
				tOrder.setVisiableStatus(OrderEnum.VISIABLE_YES.getStringValue());
				orderDao.updateByPrimaryKey(tOrder);
			}
		}
	}

	@Override
	public void synOrderCreateUserName(Long userId, String userName) {
		orderDao.updateUserName(userId, userName);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TOrder produceOrder(TService service, Integer type, String date) {
		TUser tUser = userService.getUserById(service.getUserId());
		if (!checkEnoughTimeCoin(tUser, service)) {
			throw new MessageException("501", "用户授信不足");
		}
		TOrder order = BeanUtil.copy(service, TOrder.class);
		order.setId(snowflakeIdWorker.nextId());
		order.setConfirmNum(0);
		order.setEnrollNum(0);
		order.setServiceId(service.getId());
		order.setStatus(OrderEnum.STATUS_NORMAL.getValue());
		order.setServiceStatus(service.getStatus());
		//重复的订单的话  根据商品的重复时间生成第一张订单
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			// 生成订单的开始结束时间
			Integer code = generateOrderTime(service, order, type, date);
//			if (Objects.equals(type, OrderEnum.PRODUCE_TYPE_SUBMIT.getValue())
//					|| Objects.equals(type, OrderEnum.PRODUCE_TYPE_UPPER.getValue())
//					|| Objects.equals(type, OrderEnum.PRODUCE_TYPE_AUTO.getValue())) {
//				productService.update(service);
//			}
			if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue())) {
				//可以成功创建订单
				saveOrder(order);
				System.out.println(order.getId() + "   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				return order;
				// TODO 订单结束定时任务
			} else if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue())) {
				// 订单已存在或者已经，不需要再派生
				logger.info("商品ID为{}，时间为 {} - {} 的订单已经存在，无法继续派生", service.getId(), order.getStartTime(), order.getEndTime());
				return order;

			} else if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_LOWER_FRAME.getValue())) {
				logger.info("商品ID为{}的商品已经超时，无法继续派生， 已做下架处理", service.getId(), order.getStartTime(), order.getEndTime());
				// 下架商品  同步所有订单状态
				productService.autoLowerFrameService(service);
				return null;

			} else if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_END.getValue())) {
				//到最后一张，但是还没到结束时间（一般是报名人满生成的）
				logger.info("商品ID为{} 的订单已经派生到最后一张，无法继续派生", service.getId());
				return null;
			}
		} else {
			saveOrder(order);
			return order;
		}
		return order;
	}

	/**
	 * 根据商品的抽象时间派生出订单具体的时间
	 *
	 * @param service 要派生的商品
	 * @param order   派生的订单
	 * @param type    派生的类型，是发布时触发的派生还是重新上架时派生的订单
	 * @param date    日期 报名或者报满时传递的日期进行派生
	 * @return
	 */
	private Integer generateOrderTime(TService service, TOrder order, int type, String date) {
		int[] weekDayNumberArray = DateUtil.getWeekDayArray(service.getDateWeekNumber());
		if (OrderEnum.PRODUCE_TYPE_SUBMIT.getValue() == type) {
			//发布时候生成的订单
			return produceOrderByPublish(service, order, weekDayNumberArray);
		} else if (OrderEnum.PRODUCE_TYPE_UPPER.getValue() == type) {
			//上架时候派生订单  感觉可以调用发布生成订单的逻辑
			return produceOrderByUpper(service, order, weekDayNumberArray);
		} else if (OrderEnum.PRODUCE_TYPE_AUTO.getValue() == type) {
			//调用发布派生的逻辑
			return produceOrderByPublish(service, order, weekDayNumberArray);
		} else if (OrderEnum.PRODUCE_TYPE_ENROLL.getValue() == type) {
			return produceOrderByEnroll(service, date, order);
		} else {
			//报名人满后派生
			return produceOrderByEnough(weekDayNumberArray, service, date, order);
		}
//		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 报名生成订单
	 *
	 * @param service    商品
	 * @param enrollDate 报名的日期
	 * @param order 新的订单  如果已经存在该订单，就将新派生的订单引用到该订单上
	 * @return
	 */
	private Integer produceOrderByEnroll(TService service, String enrollDate, TOrder order) {
		//报名派生  判断是否已经存在，已经存在，则停止派生
		/*
		 * 传递一个serviceId 和 一个订单的日期
		 * 查看数据库是否存在 ，存在则不派生
		 * 数据库不存在的话，就进行派生。如果超过商品结束时间，不做下架操作
		 */
		TService product = productService.getProductById(service.getId());
//		String startDate = "20190312";
		String startDateTime = enrollDate + product.getStartTimeS();
		String endDateTime = enrollDate + product.getEndTimeS();
		Long startDateTimeMill = DateUtil.parse(startDateTime);
		Long endDateTimeMill = DateUtil.parse(endDateTime);
		TOrder oldOrder = orderDao.findProductOrder(service.getId(), startDateTimeMill, endDateTimeMill);
		if (oldOrder != null) { //订单已存在
			order = oldOrder;
			return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
		}
		// 查看是否到结束时间，如果到结束时间，无法生成，但是不做下架处理
		String productEndDateTime = product.getEndDateS() + product.getEndTimeS();
		if (DateUtil.parse(productEndDateTime) < endDateTimeMill) {
			order = null;
			return OrderEnum.PRODUCE_RESULT_CODE_END.getValue();
		}
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 人选满了之后派生订单
	 *
	 * @param weekDayNumberArray 商品周期的星期数组
	 * @param service            要派生订单的商品
	 * @param date               选满人的订单日期
	 * @param order              要派生的订单
	 * @return code码
	 */
	private Integer produceOrderByEnough(int[] weekDayNumberArray, TService service, String date, TOrder order) {
		/*
		 * 传递一个serviceId 和 一个结束的日期
		 * 根据这个商品的周  获取下一张订单的周
		 * 加上这些天数获得新的订单的开始时间和结束时间
		 * 查看数据库是否存在 ，存在则不派生
		 * 数据库不存在的话，就进行派生。如果超过商品结束时间，不做下架操作，不提示无法生成，不创建下一张订单
		 */
//		date = "20190312";
		String startDateTime = date + service.getStartTimeS();
		String endDateTime = date + service.getEndTimeS();
		//报名人满的订单所在的周
		Integer weekDay = DateUtil.getWeekDay(startDateTime);
		int nextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, weekDay);
		int addDays = (nextWeekDay + 7 - weekDay) % 7;
		if (addDays == 0) {
			addDays = 7;
		}
		Long startDateTimeMill = DateUtil.addDays(DateUtil.parse(startDateTime), addDays);
		Long endDateTimeMill = DateUtil.addDays(DateUtil.parse(endDateTime), addDays);
		TOrder oldOrder = orderDao.findProductOrder(service.getId(), startDateTimeMill, endDateTimeMill);
		if (oldOrder != null) { //有这张订单，不需要派生
			order = oldOrder;
			return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
		}
		// 查看是否到结束时间，如果到结束时间，返回超时下架处理的错误码
		String productEndDateTime = service.getEndDateS() + service.getEndTimeS();
		if (DateUtil.parse(productEndDateTime) < endDateTimeMill) {
			order = null;
			return OrderEnum.PRODUCE_RESULT_CODE_END.getValue();
		}
		order.setStartTime(startDateTimeMill);
		order.setEndTime(endDateTimeMill);
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();

	}

	/**
	 * 上架派生订单（可与发布派生订单合用）
	 *
	 * @param service
	 * @param order
	 * @param weekDayNumberArray
	 * @return
	 */
	private Integer produceOrderByUpper(TService service, TOrder order, int[] weekDayNumberArray) {
		/*
		 * 重新上架，先找上一条已完成的订单，
		 * {
		 * 如果没有，则从商品开始时间进行派生，并进行开始时间的判断
		 * 调用发布时候派生订单的部分逻辑，派生出一张当前时间之后的订单
		 * 如果数据库已经存在正在进行的订单，则不继续派生
		 * 如果数据库存在的是取消的订单，则再生成一张新的订单
		 * 如果已经超时，则进行下架处理
		 * }
		 * 如果有最后一条结束的
		 * {
		 * 拿最后一条的时间作为开始结束时间
		 * 进行遍历匹配时间，派生出一张当前时间之后的订单
		 * 如果数据库已经存在正在进行的订单，则不继续派生
		 * 如果数据库存在的是取消的订单，则再生成一张新的订单
		 * 如果已经超时，则进行下架处理
		 * }
		 *
		 */
		return produceOrderByPublish(service, order, weekDayNumberArray);
		// 重新上架，找最后一条结束的，如果没有，停止派生
//		TOrder latestOrder = orderDao.findOneLatestOrderByServiceId(service.getId());
//		if (latestOrder == null) { // 如果为空，则不需要派生，说明有一张订单还在执行
//			//调用重新发布时派生订单
//		} else {
//			Long latestOrderStartTime = latestOrder.getStartTime();
//			Long latestOrderEndTime = latestOrder.getEndTime();
//			Long orderStartTime = 0L;
//			Long orderEndTime = 0L;
//			while (true) {
//				int latestOrderWeekDay = DateUtil.getWeekDay(latestOrderStartTime);
//				int latestOrderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, latestOrderWeekDay);
//				int addDays = (latestOrderNextWeekDay + 7 - latestOrderWeekDay) % 7;
//				if (addDays == 0) {
//					addDays = 7;
//				}
//				//订单开始的时间戳
//				orderStartTime = DateUtil.addDays(latestOrderStartTime, addDays);
//				orderEndTime = DateUtil.addDays(latestOrderEndTime, addDays);
//				if (orderEndTime >= System.currentTimeMillis()) {
//					break;
//				}
//				latestOrderStartTime = orderStartTime;
//			}
//			//查看数据库是否有该时间派生出的订单， 如果有，则停止派生
//			//是否超过结束时间 如果超过结束时间，进行商品下架处理，不再派生
//			order.setStartTime(orderStartTime);
//			order.setEndTime(orderEndTime);
//			// 查看数据库防止有这一条
//			Long count = orderDao.countProductOrder(service.getId(), orderStartTime, orderEndTime);
//			if (count != 0) {
//				return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
//			}
//			// 查看是否到结束时间，如果到结束时间，返回超时下架处理的错误码
//			String endDateTime = service.getEndDateS() + service.getEndTimeS();
//			if (DateUtil.parse(endDateTime) < orderEndTime) {
//				return OrderEnum.PRODUCE_RESULT_CODE_LOWER_FRAME.getValue();
//			}
//		}
//		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 发布派生订单
	 *
	 * @param service            要派生订单的商品
	 * @param order              派生的订单
	 * @param weekDayNumberArray 商品重复周期的星期数组
	 * @return
	 */
	private Integer produceOrderByPublish(TService service, TOrder order, int[] weekDayNumberArray) {
		/*
		 * 获取商品的开始的时间  然后获取商品的开始时间是周几
		 * 然后获取离这个周最近的一个可以发布的周，可能会是当天
		 * 如果是当天，就需要判断当天的结束时间是否在当前时间之前，如果在当前时间之前，已经过期，需要再重新寻找找下一个可发布日
		 * 循环遍历，一直到结束时间在当前时间之前，并且没有超过商品的结束时间，才可以创建出订单
		 * 如果超过商品的结束时间，则对商品进行下架处理
		 */
		//获取商品开始时间的字符串形式 201803051434
		String serviceStartTimeString = service.getStartDateS() + service.getStartTimeS();
		String serviceEndTimeString = service.getStartDateS() + service.getEndTimeS();
		// 商品开始的时间
		Long productStartTime = DateUtil.parse(serviceStartTimeString);
		//商品结束的时间
		Long productEndTime = DateUtil.parse(serviceEndTimeString);
		DateResult dr = DateUtil.getNextOrderBeginAndEndTime(productStartTime, productEndTime, weekDayNumberArray, true);
////			//获取开始的时间是星期X
//		int startWeekDay = DateUtil.getWeekDay(productStartTime);
////			//获取订单开始的时间是星期X  离商品开始星期X最近的星期Y
//		int orderWeekDay = DateUtil.getMostNearWeekDay(weekDayNumberArray, startWeekDay);
//		int addDays = (orderWeekDay + 7 - startWeekDay) % 7;
////			//订单开始的时间戳
//		Long startTimeMill = DateUtil.addDays(productStartTime, addDays);
//		Long endTimeMill = DateUtil.addDays(productEndTime, addDays);
//		//参数星期的下一个星期X(不包含这个参数星期)
////			int orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);
//		//如果重复中包含当天的订单，查看结束时间是否小于当前时间，小于当前时间就是已经过了今天的，直接发下一个星期X的
//		int orderNextWeekDay;
//		while (true) {
//			if (endTimeMill >= System.currentTimeMillis()) {
//				break;
//			}
//			orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);
//
//			addDays = (orderNextWeekDay + 7 - orderWeekDay) % 7;
//			if (addDays == 0) {
//				addDays = 7;
//			}
//			startTimeMill = DateUtil.addDays(startTimeMill, addDays);
//			endTimeMill = DateUtil.addDays(endTimeMill, addDays);
//			orderWeekDay = orderNextWeekDay;
//		}

		Long startTimeMill = dr.getStartTimeMill();
		Long endTimeMill = dr.getEndTimeMill();
		order.setStartTime(startTimeMill);
		order.setEndTime(endTimeMill);
		// 整个商品的结束时间
		String endDateTime = service.getEndDateS() + service.getEndTimeS();
		// 查看是否到结束时间，如果到结束时间，返回超时下架处理的错误码
		if (DateUtil.parse(endDateTime) < endTimeMill) {
			return OrderEnum.PRODUCE_RESULT_CODE_LOWER_FRAME.getValue();
		}
		List<String> enrollDate = new ArrayList<>();
//		enrollDate.add(DateUtil.getDate(startTimeMill));
		dr.setDays(1);
		long tempStart = startTimeMill;
		long tempEnd = endTimeMill;
		String startDate = DateUtil.getDate(startTimeMill);
		//计算第一张订单后六天的可报名时间
		DateResult tempResult;
		while (dr.getDays() <= 6) {
			tempResult = DateUtil.getNextOrderBeginAndEndTime(tempStart, tempEnd, weekDayNumberArray, false);
			if (DateUtil.parse(endDateTime) < tempResult.getEndTimeMill()) {
				break;
			}
			if (tempResult.getDays() == 0) {
				break;
			}
			dr.setDays(dr.getDays() + tempResult.getDays());
			// 查找这个时间段是否有报满人的订单，如果有报满人的订单，则不需要将这天的日期加进去  否则就将这天加入到可报名日期
			TOrder temOrder = orderDao.findProductOrderEnough(service.getId(), tempStart, tempEnd);
			if (temOrder == null) {
				enrollDate.add(DateUtil.getDate(tempStart));
			}
			tempStart = tempResult.getStartTimeMill();
			tempEnd = tempResult.getEndTimeMill();
		}
		service.setEnrollDate(StringUtils.join(enrollDate.toArray(), ","));
		// 查看数据库防止有这一条
		TOrder oldOrder = orderDao.findProductOrder(service.getId(), startTimeMill, endTimeMill);
		if (oldOrder != null) {
			order = oldOrder;
			return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
		}

		//订单开始的日期
//			String orderStartDate = DateUtil.format(cal.getTimeInMillis()).substring(0, 8);
		//订单开始时间 = 订单开始的日期 + 商品的开始时间
//			Long orderStartTime = DateUtil.parse(orderStartDate + service.getStartTimeS());
		//订单结束时间 = 订单开始的日期 + 商品的结束时间
//			Long orderEndTime = DateUtil.parse(orderStartDate + service.getEndTimeS());
//			Long countOrder = orderDao.countProductOrder(service.getId(), orderStartTime, orderEndTime);
//			if (countOrder != 0) {
//				// 不能生成订单 已有这个时间段的订单
//			}
//			order.setStartTime(startTimeMill);
//			order.setEndTime(endTimeMill);
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 获取报名选人的显示状态
	 *
	 * @param user              当前用户
	 * @param orderRelationship 订单关系（报名/选人记录）
	 * @param returnView        vo
	 */
	private void getEnrollChooseShowStatus(TUser user, TOrderRelationship orderRelationship, PageEnrollAndChooseReturnView returnView) {
		if (orderRelationship.getFromUserId().equals(user.getId())) { //当前用户是发布者
			if (orderRelationship.getEndTime() < System.currentTimeMillis()) {
				returnView.setStatus(OrderEnum.SHOW_STATUS_ENROLL_CHOOSE_ALREADY_END.getValue());
			} else if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_PUBLISH_CANCEL.getType())) {
				returnView.setStatus(OrderEnum.SHOW_STATUS_ENROLL_CHOOSE_ALREADY_CANCEL.getValue());
			} else {
				returnView.setStatus(OrderEnum.SHOW_STATUS_ENROLL_CHOOSE_WAIT_CHOOSE.getValue());
			}
		} else if (Objects.equals(orderRelationship.getReceiptUserId(), user.getId())) { //当前用户是接单者
			if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType())) {
				returnView.setStatus(OrderEnum.SHOW_STATUS_ENROLL_CHOOSE_BE_REFUSED.getValue());
			} else if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())) {
				returnView.setStatus(OrderEnum.SHOW_STATUS_ENROLL_CHOOSE_ALREADY_ENROLL.getValue());
			} else {
				returnView.setStatus(OrderEnum.SHOW_STATUS_ENROLL_CHOOSE_ALREADY_CHOOSED.getValue());
			}
		}
	}

	/**
	 * 功能描述: 查看用户时间币是否足够
	 *
	 * @param user
	 * @param service
	 * @return
	 * @author 马晓晨
	 * @date 2019/3/9 21:43
	 */
	private boolean checkEnoughTimeCoin(TUser user, TService service) {
		if (service.getType().equals(ProductEnum.TYPE_SERVICE.getValue())) {
			return true;
		}
		if (service.getCollectType().equals(ProductEnum.COLLECT_TYPE_COMMONWEAL.getValue())) {
			return true;
		}
		// 求助发布的单价
		Long collectTime = service.getCollectTime();
		// 求助需要的人员数量
		Integer servicePersonnel = service.getServicePersonnel();
		// 该求助需要的时间币
		long seekHelpPrice = collectTime * servicePersonnel;
		// 检测用户账户是否足够发布该求助
		if (user.getSurplusTime() + user.getCreditLimit() - user.getFreezeTime() < seekHelpPrice) {
			return false;
		}
		return true;
	}

}
