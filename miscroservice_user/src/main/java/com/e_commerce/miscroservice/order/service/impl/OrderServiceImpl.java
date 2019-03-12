package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.dao.OrderRelationshipDao;
import com.e_commerce.miscroservice.order.service.OrderService;
import com.e_commerce.miscroservice.order.vo.*;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.e_commerce.miscroservice.product.util.DateUtil;
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
			order.setNameAudioUrl("https://timebank-test-img.oss-cn-hangzhou.aliyuncs.com/oneHour%28v3.0%29/otherImg/155072265764919.mp3");
			PageOrderReturnView returnView = new PageOrderReturnView();
			returnView.setOrder(order);
			//TODO 获取order封面图
			if (order.getId().equals(101675590041468928L)) {
				returnView.setImgUrl("https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/organize/VCG21ba75fd122.jpg");
			}
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void produceOrder(TService service) {
		TOrder order = BeanUtil.copy(service, TOrder.class);
		order.setId(snowflakeIdWorker.nextId());
		order.setConfirmNum(0);
		order.setEnrollNum(0);
//		order.setMainId(order.getId());
		order.setServiceId(service.getId());
		order.setStatus(OrderEnum.STATUS_NORMAL.getValue());
		//重复的订单的话  根据商品的重复时间生成第一张订单
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			generateOrderTime(service, order);
		}
		orderDao.saveOneOrder(order);
	}

	/**
	 * 将字符串数组转换为int数组
	 *
	 * @param weekDayArray 字符串数值数组
	 * @return int数组
	 * @author 马晓晨
	 */
	private int[] getIntArray(String[] weekDayArray) {
		int[] WeekDayNumberArray = new int[weekDayArray.length];
		for (int i = 0; i < weekDayArray.length; i++) {
			Integer weekDay = Integer.parseInt(weekDayArray[i]);
			WeekDayNumberArray[i] = weekDay;
		}
		return WeekDayNumberArray;
	}

	/**
	 * 计算订单的开始结束时间毫秒值
	 *
	 * @param service 商品
	 * @param order   订单
	 */
	private void generateOrderTime(TService service, TOrder order) {
		String[] weekDayArray = service.getDateWeekNumber().split(",");
		int[] WeekDayNumberArray = getIntArray(weekDayArray);
		//对星期进行升序排序
		Arrays.sort(WeekDayNumberArray);
		//查看该商品是否已经派生过订单
		Long countOrder = orderDao.countProductOrder(service.getId());
		if (countOrder == 0) { //该商品没有派生过订单
			//获取商品开始时间的字符串形式 201803051434
			String serviceStartTimeString = service.getStartDateS() + service.getStartTimeS();
			Long startTime = DateUtil.parse(serviceStartTimeString);
//			getOrderDay(service, order, WeekDayNumberArray, startTime, true);
		} else { // 该商品之前派生过订单
			TOrder tOrder = orderDao.findOneLatestOrderByServiceId(service.getId());
			Long startTime = tOrder.getStartTime();

			//获取上一张订单开始的时间是星期X
			int startWeekDay = DateUtil.getWeekDay(startTime);
			//获取订单开始的时间是星期X  离商品开始星期X最近的星期Y
			int orderWeekDay = DateUtil.getMostNearWeekDay(WeekDayNumberArray, startWeekDay);
			//需要增加的天数
			int addDays = (orderWeekDay + 7 - startWeekDay) % 7;
			if (addDays == 0) {
				addDays = 7;
			}
			String startDate = DateUtil.getDate(startTime);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(startTime);
			cal.add(Calendar.DAY_OF_YEAR, addDays);
			//校验商品是否超时
			String endDateTime = service.getEndDateS() + service.getEndTimeS();
			if (DateUtil.parse(endDateTime) < cal.getTimeInMillis()) {
				throw new MessageException("订单生成超时，请修改时间后重新发布");
			}
			//订单开始的日期
			String orderStartDate = DateUtil.format(cal.getTimeInMillis()).substring(0, 8);
			//订单开始时间 = 订单开始的日期 + 商品的开始时间
			order.setStartTime(DateUtil.parse(orderStartDate + service.getStartTimeS()));
			//订单结束时间 = 订单开始的日期 + 商品的结束时间
			order.setEndTime(DateUtil.parse(orderStartDate + service.getEndTimeS()));
		}
	}

	private void getOrderDay(TService service, TOrder order, int[] weekDayNumberArray, int type) {

		String[] weekDayArray = service.getDateWeekNumber().split(",");
		int[] WeekDayNumberArray = getIntArray(weekDayArray);
		//对星期进行升序排序
		Arrays.sort(WeekDayNumberArray);
		//获取开始的时间是星期X
//		int startWeekDay = DateUtil.getWeekDay(startTime);
//		//获取订单开始的时间是星期X  离商品开始星期X最近的星期Y
//		int orderWeekDay = DateUtil.getMostNearWeekDay(weekDayNumberArray, startWeekDay);
		//需要增加的天数
//		int addDays = (orderWeekDay + 7 - startWeekDay) % 7;
		if (OrderEnum.PRODUCE_TYPE_SUBMIT.getValue() == type) { //发布时候生成的订单
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
//			//获取开始的时间是星期X
			int startWeekDay = DateUtil.getWeekDay(productStartTime);
//			//获取订单开始的时间是星期X  离商品开始星期X最近的星期Y
			int orderWeekDay = DateUtil.getMostNearWeekDay(weekDayNumberArray, startWeekDay);
			int addDays = (orderWeekDay + 7 - startWeekDay) % 7;
//			//订单开始的时间戳
			Long startTimeMill = DateUtil.addDays(productStartTime, addDays);
			Long endTimeMill = DateUtil.addDays(productEndTime, addDays);
			//参数星期的下一个星期X(不包含这个参数星期)
//			int orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);
			//如果重复中包含当天的订单，查看结束时间是否小于当前时间，小于当前时间就是已经过了今天的，直接发下一个星期X的
			int orderNextWeekDay;
			while (true) {
				if (endTimeMill >= System.currentTimeMillis()) {
					break;
				}
				orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);

				addDays = (orderNextWeekDay + 7 - orderWeekDay) % 7;
				if (addDays == 0) {
					addDays = 7;
				}
				startTimeMill = DateUtil.addDays(startTimeMill, addDays);
				endTimeMill = DateUtil.addDays(endTimeMill, addDays);
				orderWeekDay = orderNextWeekDay;
			}
			String endDateTime = service.getEndDateS() + service.getEndTimeS();
			if (DateUtil.parse(endDateTime) < endTimeMill) {
				throw new MessageException("订单生成超时，请修改时间后重新发布");
			}
			//TODO 查看数据库防止有这一条
			// TODO 查看是否到结束时间，如果到结束时间，下架掉
			order.setStartTime(startTimeMill);
			order.setEndTime(endTimeMill);
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
//

		} else if (OrderEnum.PRODUCE_TYPE_UPPER.getValue() == type) { //上架时候派生订单
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
			// 重新上架，找最后一条结束的，如果没有，停止派生
			TOrder latestOrder = orderDao.findOneLatestOrderByServiceId(service.getId());
			if (latestOrder == null) { // 如果为空，则不需要派生，说明有一张订单还在执行
				//调用重新发布时派生订单
			} else {
				Long latestOrderStartTime = latestOrder.getStartTime();
				Long latestOrderEndTime = latestOrder.getEndTime();
				Long orderStartTime = 0L;
				Long orderEndTime = 0L;
				while (true) {
					int latestOrderWeekDay = DateUtil.getWeekDay(latestOrderStartTime);
					int latestOrderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, latestOrderWeekDay);
					int addDays = (latestOrderNextWeekDay + 7 - latestOrderWeekDay) % 7;
					if (addDays == 0) {
						addDays = 7;
					}
					//订单开始的时间戳
					orderStartTime = DateUtil.addDays(latestOrderStartTime, addDays);
					orderEndTime = DateUtil.addDays(latestOrderEndTime, addDays);
//					String orderStartDate = DateUtil.format(cal.getTimeInMillis()).substring(0, 8);
//					orderStartTime = DateUtil.parse(orderStartDate + service.getStartTimeS());
//					orderEndTime = DateUtil.parse(orderStartDate + service.getEndTimeS());
					if (orderEndTime >= System.currentTimeMillis()) {
						break;
					}
					latestOrderStartTime = orderStartTime;
				}
				//查看数据库是否有该时间派生出的订单， 如果有，则停止派生
				//是否超过结束时间 如果超过结束时间，进行商品下架处理，不再派生
				String endDateTime = service.getEndDateS() + service.getEndTimeS();
				if (DateUtil.parse(endDateTime) < orderEndTime) {
					throw new MessageException("订单生成超时，请修改时间后重新发布");
				}
			}
//		} else if (OrderEnum.PRODUCE_TYPE_AUTO.getValue() == type) {
//			//订单任务上一张订单完成后派生下一张订单
//			int orderNextWeekDay = DateUtil.getNextWeekDay(weekDayNumberArray, orderWeekDay);
//			addDays = (orderNextWeekDay + 7 - startWeekDay) % 7;
//			if (addDays == 0) {
//				addDays = 7;
//			}
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(startTime);
//			cal.add(Calendar.DAY_OF_YEAR, addDays);
//			// 判断数据库是否有这条记录， 如果有，说明生成过 不再派生
//			// 判断结束时间是否超过当前时间，超过，则继续往下派单
//			//订单开始的日期
//			String orderStartDate = DateUtil.format(cal.getTimeInMillis()).substring(0, 8);
//			//订单开始时间 = 订单开始的日期 + 商品的开始时间
//			Long orderStartTime = DateUtil.parse(orderStartDate + service.getStartTimeS());
//			//订单结束时间 = 订单开始的日期 + 商品的结束时间
//			Long orderEndTime = DateUtil.parse(orderStartDate + service.getEndTimeS());
//			Long countOrder = orderDao.countProductOrder(service.getId(), orderStartTime, orderEndTime);
//			if (countOrder != 0) {
//				// 不能生成订单 已有这个时间段的订单
//			}
//
//			order.setEndTime(orderEndTime);
//			String endDateTime = service.getEndDateS() + service.getEndTimeS();
//			if (DateUtil.parse(endDateTime) < cal.getTimeInMillis()) {
//				throw new MessageException("订单生成超时，请修改时间后重新发布");
//			}

		} else {
			//报名派生  判断是否已经存在，已经存在，则停止派生
		}
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
			} else if (orderRelationship.getStatus().equals(OrderRelationshipEnum.STATUS_ENROLLER_CANCEL.getType())) {
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
}
