package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.service.OrderService;
import com.e_commerce.miscroservice.order.vo.*;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int saveOrder(TOrder order) {
		return orderDao.saveOneOrder(order);
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
//		if (companyIds.size() == 0) {
//			// 如果用户没有登录或者用户没有加入组织，则为了数据库查询的in语句不出错，加入一个1值。但是会出现效率问题
//			companyIds.add(1L);
//		}
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
		Page<TOrder> page = orderDao.pageOrder(param);
		List<TOrder> listOrder = page.getResult();
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
			// TODO
			order.setNameAudioUrl("");
			//设置封面图
			// TODO 封面图先给前端显示出字段
			returnView.setImgUrl("");
//			returnView.setImgUrl(productCoverPic.get(order.getServiceId()));
			//TODO 获取发布者的信息
//			TUser tUser = usersInfo.get(service.getUserId());
			// TODO 先将用户信息new 出来
			BaseUserView userView = new BaseUserView();
			userView.setUserHeadPortraitPath("");
			userView.setName("");
			returnView.setUser(userView);
//			// 进行部分字段映射
//			PageServiceUserView userView = BeanUtil.copy(tUser, PageServiceUserView.class);
//			returnView.setUser(userView);
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
		order.setNameAudioUrl("");
		returnView.setOrder(order);
		// TODO 获取发布者信息
		BaseUserView userView = new BaseUserView();
		userView.setId(0L);
		userView.setName("");
		returnView.setUser(userView);
		// TODO 获取发布者关注信息
		// 关注状态 1、显示关注 2、显示已关注
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
			orderRelationship.setStatus(0);
			orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType());
			orderRelationship.setServiceCollectionType(OrderRelationshipEnum.SERVICE_COLLECTION_IS_NO.getType());
		}
		List<TServiceDescribe> listDesc = productService.getProductDesc(order.getServiceId());
		//将详情汇中的封面图取出来
		for (int i = 0; i < listDesc.size(); i++) {
			if (listDesc.get(i).getIsCover().equals(IS_COVER_YES)) {
				//将封面图取出来单独存
				returnView.setCoverImgUrl(listDesc.get(i).getUrl());
				listDesc.remove(i);
			}
		}
		returnView.setListServiceDescribe(listDesc);
		return returnView;
	}

	@Override
	public List<PageEnrollAndChooseReturnView> enrollList(Integer pageNum, Integer pageSize, TUser user) {
		// TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		List<PageEnrollAndChooseReturnView> listReturnView = new ArrayList<>();
		List<TOrderRelationship> pageEnrollAndChooseList = orderRelationshipDao.pageEnrollAndChooseList(pageNum, pageSize, user.getId());
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
		return listReturnView;
	}

	@Override
	public void SynOrderServiceStatus(Long productId, Integer status) {
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
		//查询报名关系表关联的所有订单
		List<Long> orderIds = new ArrayList<>();
		listOrderRelationship.stream().forEach(relationship -> orderIds.add(relationship.getOrderId()));
		List<TOrder> listOrder = orderDao.selectOrderByOrderIds(orderIds);
		//获取order的map  key-> orderId  value-> order
		Map<Long, TOrder> orderMap = new HashMap<>();
		listOrder.stream().forEach(order -> orderMap.put(order.getId(), order));
		for (TOrderRelationship relationship : listOrderRelationship) {
			TOrder order = orderMap.get(relationship.getOrderId());
			PageOrderReturnView returnView = new PageOrderReturnView();
			returnView.setOrder(order);
			//TODO 获取order封面图
			returnView.setImgUrl("");
			//TODO 判断要显示的状态
			returnView.setStatus("状态");
			listReturnView.add(returnView);
		}
		result.setResultList(listReturnView);
		result.setTotalCount(page.getTotal());
		return result;
	}

	@Override
	public DetailMineOrderReturnView detailMineOrder(TUser user, Long orderId) {
		// TODO 写死订单ID
		orderId = 101675590041468928L;
		DetailMineOrderReturnView returnView = new DetailMineOrderReturnView();
		//TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		// TODO 根据relationship的状态显示回去 及当前用户的身份（发布者还是接单者）
		TOrderRelationship relationship = orderRelationshipDao.selectByOrderIdAndUserId(orderId, user.getId());
		returnView.setStatus("待结束");
		TOrder tOrder = orderDao.selectByPrimaryKey(orderId);
		returnView.setOrder(tOrder);
		// TODO 查询当前订单的订单记录
		List<TOrderRecord> list = new ArrayList<>();
		TOrderRecord tOrderRecord = new TOrderRecord();
		tOrderRecord.setCreatTime(System.currentTimeMillis());
		tOrderRecord.setContent("记录");
		list.add(tOrderRecord);
		returnView.setRecord(list);
		// 获取详情
		List<TServiceDescribe> productDesc = productService.getProductDesc(tOrder.getServiceId());
		returnView.setListDesc(productDesc);
		if (Objects.equals(user.getId(), tOrder.getCreateUser())) { //当前用户是发布者
			//获取所有接单者信息
			List<TOrderRelationship> listRelationship = orderRelationshipDao.getReceiver(orderId);
			List<BaseUserView> listUserView = new ArrayList<>();
			// 对接单者进行基本用户信息的映射
			for (TOrderRelationship tOrderRelationship : listRelationship) {
				TUser receiver = userService.getUserById(tOrderRelationship.getReceiptUserId());
				BaseUserView userView = BeanUtil.copy(receiver, BaseUserView.class);
				listUserView.add(userView);
				// TODO 异常状态
				userView.setPointStatus(1);
			}
			returnView.setListUserView(listUserView);
		} else { //当前用户是接单者
			//对接单者来说 发布者发布的是求助  接单者就是服务者  就是一条服务记录 反之亦然
			if (tOrder.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
				tOrder.setType(ProductEnum.TYPE_SERVICE.getValue());
			} else {
				tOrder.setType(ProductEnum.TYPE_SEEK_HELP.getValue());
			}
			//对接单者来说  这条订单对应一个人  显示人数为1
			tOrder.setServicePersonnel(1);
		}
		// 关注状态 只有一个人的时候会显示 1、显示关注 2、显示已关注
		if (returnView.getListUserView().size() == 1) {
			returnView.getListUserView().get(0).setCareStatus(1);
		}
		return returnView;
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
				returnView.setStatus("已结束");
			} else if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_ENROLLER_CANCEL.getType())) {
				returnView.setStatus("已取消");
			} else {
				returnView.setStatus("待选人");
			}
		} else if (Objects.equals(orderRelationship.getReceiptUserId(), user.getId())) { //当前用户是接单者
			if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_NOT_CHOOSE.getType())) {
				returnView.setStatus("被拒绝");
			} else if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_WAIT_CHOOSE.getType())) {
				returnView.setStatus("已报名");
			} else {
				returnView.setStatus("已入选");
			}
		}
	}
}
