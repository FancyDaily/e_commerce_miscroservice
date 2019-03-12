package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.MsgResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.ErrorException;
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
//		TOrderRelationship relationship = new TOrderRelationship();
//		relationship.setId(idGenerator.nextId());
//		relationship.setOrderId(order.getId());
//		relationship.setServiceId(order.getServiceId());
//		relationship.setServiceType(order.getType());
//		relationship.setFromUserId(order.getCreateUser());
//		relationship.setServiceReportType(OrderRelationshipEnum.re);
//		relationship
//		relationship.setStatus();
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
				userView.setCareStatus(1);
				// TODO 根据用户是求助者还是服务者进行分数字段的选择
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
	public DetailChooseReturnView chooseDetail(Long orderId, TUser user) {
		//写死orderId
		orderId = 101675891532234752L;
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
	@Transactional(rollbackFor = Throwable.class)
	public void produceOrder(Long serviceId, Integer type, String date) {
		TService service = productService.getProductById(serviceId);
		MsgResult msgResult;
		TUser tUser = userService.getUserById(service.getUserId());
		if (!checkEnoughTimeCoin(tUser, service)) {
			throw new MessageException("501", "用户授信不足");
//			msgResult.setCode(501);
//			msgResult.setMessage("用户授信不足");
//			return msgResult;
		}
		TOrder order = BeanUtil.copy(service, TOrder.class);
		order.setId(snowflakeIdWorker.nextId());
		order.setConfirmNum(0);
		order.setEnrollNum(0);
		// TODO 设置主ID 都关联了serviceID 感觉可以不设置主ID了
//		order.setMainId(order.getId());
		order.setServiceId(service.getId());
		order.setStatus(OrderEnum.STATUS_NORMAL.getValue());
		//重复的订单的话  根据商品的重复时间生成第一张订单
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			// 生成订单的开始结束时间
			Integer code = generateOrderTime(service, order, type, date);
			if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue())) {
				//可以成功创建订单
				saveOrder(order);
				orderDao.saveOneOrder(order);
				// 只有求助并且是互助时才冻结订单
				if (service.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue()) && service.getCollectType().equals(ProductEnum.COLLECT_TYPE_EACHHELP.getValue())) {
					msgResult = userService.freezeTimeCoin(tUser.getId(), service.getCollectTime() * service.getServicePersonnel(), service.getId(), service.getServiceName());
					if (!msgResult.equals("200")) {
						throw new ErrorException(msgResult.getCode(), msgResult.getMessage());
					}
				}
				// TODO 订单结束定时任务
			} else if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue())) {
				// 订单已存在或者已经，不需要再派生
				logger.info("商品ID为{}，时间为 {} - {} 的订单已经存在，无法继续派生", service.getId(), order.getStartTime(), order.getEndTime());

			} else if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_LOWER_FRAME.getValue())) {
				logger.info("商品ID为{}的商品已经超时，无法继续派生， 已做下架处理", service.getId(), order.getStartTime(), order.getEndTime());
				// 下架商品  同步所有订单状态
				msgResult = productService.autolowerFrameService(service);
				if (!msgResult.getCode().equals("200")) {
					throw new ErrorException("500", "下架商品失败");
				}

			} else if (code.equals(OrderEnum.PRODUCE_RESULT_CODE_END.getValue())) {
				//到最后一张，但是还没到结束时间（一般是报名人满生成的）
				logger.info("商品ID为{} 的订单已经派生到最后一张，无法继续派生", service.getId());
			}
		} else {
			orderDao.saveOneOrder(order);
			if (service.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue()) && service.getCollectType().equals(ProductEnum.COLLECT_TYPE_EACHHELP.getValue())) {
				msgResult = userService.freezeTimeCoin(tUser.getId(), service.getCollectTime() * service.getServicePersonnel(), service.getId(), service.getServiceName());
				if (!msgResult.equals("200")) {
					throw new MessageException("500", "冻结用户金额失败");
				}
			}
		}
	}

	/**
	 * 根据商品的抽象时间派生出订单具体的时间
	 * @param service 要派生的商品
	 * @param order 派生的订单
	 * @param type 派生的类型，是发布时触发的派生还是重新上架时派生的订单
	 * @param date 日期 报名或者报满时传递的日期进行派生
	 * @return
	 */
	private Integer generateOrderTime(TService service, TOrder order, int type, String date) {
		int[] weekDayNumberArray = DateUtil.getWeekDayArray(service.getDateWeekNumber());
		if (OrderEnum.PRODUCE_TYPE_SUBMIT.getValue() == type) {
			//发布时候生成的订单
			return produceOrderByPublish(service, order, weekDayNumberArray);
		} else if (OrderEnum.PRODUCE_TYPE_UPPER.getValue() == type) {
			//上架时候派生订单  感觉可以调用发布生成订单的逻辑
			return produceOrderByUpper(service,order, weekDayNumberArray);
		} else if (OrderEnum.PRODUCE_TYPE_AUTO.getValue() == type) {
			//调用重新上架的逻辑
		} else if (OrderEnum.PRODUCE_TYPE_ENROLL.getValue() == type) {
			return produceOrderByEnroll(service, date);
		} else {
			//报名人满后派生
			return produceOrderByEnough(weekDayNumberArray, service, date, order);
		}
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 报名生成订单
	 * @param service 商品
	 * @param enrollDate 报名的日期
	 * @return
	 */
	private Integer produceOrderByEnroll(TService service, String enrollDate) {
		//报名派生  判断是否已经存在，已经存在，则停止派生
		/*
		 * 传递一个serviceId 和 一个订单的日期
		 * 查看数据库是否存在 ，存在则不派生
		 * 数据库不存在的话，就进行派生。如果超过商品结束时间，不做下架操作
		 */
		TService product = productService.getProductById(service.getId());
		//TODO 报名派生的日期
//		String startDate = "20190312";
		String startDateTime = enrollDate + product.getStartTimeS();
		String endDateTime = enrollDate + product.getEndTimeS();
		Long startDateTimeMill = DateUtil.parse(startDateTime);
		Long endDateTimeMill = DateUtil.parse(endDateTime);
		Long count = orderDao.countProductOrder(service.getId(), startDateTimeMill, endDateTimeMill);
		if (count != 0) { //订单已存在
			return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
		}
		// 查看是否到结束时间，如果到结束时间，无法生成，但是不做下架处理
		String productEndDateTime = product.getEndDateS() + product.getEndTimeS();
		if (DateUtil.parse(productEndDateTime) < endDateTimeMill) {
			return OrderEnum.PRODUCE_RESULT_CODE_END.getValue();
		}
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 人选满了之后派生订单
	 * @param weekDayNumberArray 商品周期的星期数组
	 * @param service 要派生订单的商品
	 * @param date 选满人的订单日期
	 * @param order 要派生的订单
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
		date = "20190312";
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
		Long count = orderDao.countProductOrder(service.getId(), startDateTimeMill, endDateTimeMill);
		if (count != 0) { //有这张订单，不需要派生
			return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
		}
		// 查看是否到结束时间，如果到结束时间，返回超时下架处理的错误码
		String productEndDateTime = service.getEndDateS() + service.getEndTimeS();
		if (DateUtil.parse(productEndDateTime) < endDateTimeMill) {
			return OrderEnum.PRODUCE_RESULT_CODE_END.getValue();
		}
		order.setStartTime(startDateTimeMill);
		order.setEndTime(endDateTimeMill);
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();

	}

	/**
	 * 上架派生订单（可与发布派生订单合用）
	 * @param service
	 * @param order
	 * @param weekDayNumberArray
	 * @return
	 */
	private Integer produceOrderByUpper(TService service,TOrder order, int[] weekDayNumberArray) {
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
				if (orderEndTime >= System.currentTimeMillis()) {
					break;
				}
				latestOrderStartTime = orderStartTime;
			}
			//查看数据库是否有该时间派生出的订单， 如果有，则停止派生
			//是否超过结束时间 如果超过结束时间，进行商品下架处理，不再派生
			order.setStartTime(orderStartTime);
			order.setEndTime(orderEndTime);
			// 查看数据库防止有这一条
			Long count = orderDao.countProductOrder(service.getId(), orderStartTime, orderEndTime);
			if (count != 0) {
				return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
			}
			// 查看是否到结束时间，如果到结束时间，返回超时下架处理的错误码
			String endDateTime = service.getEndDateS() + service.getEndTimeS();
			if (DateUtil.parse(endDateTime) < orderEndTime) {
				return OrderEnum.PRODUCE_RESULT_CODE_LOWER_FRAME.getValue();
			}
		}
		return OrderEnum.PRODUCE_RESULT_CODE_SUCCESS.getValue();
	}

	/**
	 * 发布派生订单
	 * @param service 要派生订单的商品
	 * @param order 派生的订单
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
		order.setStartTime(startTimeMill);
		order.setEndTime(endTimeMill);
		// 查看数据库防止有这一条
		Long count = orderDao.countProductOrder(service.getId(), startTimeMill, endTimeMill);
		if (count != 0) {
			return OrderEnum.PRODUCE_RESULT_CODE_EXISTENCE.getValue();
		}
		// 查看是否到结束时间，如果到结束时间，返回超时下架处理的错误码
		String endDateTime = service.getEndDateS() + service.getEndTimeS();
		if (DateUtil.parse(endDateTime) < endTimeMill) {
			return OrderEnum.PRODUCE_RESULT_CODE_LOWER_FRAME.getValue();
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
	/**
	 * 功能描述: 查看用户时间币是否足够
	 * @author 马晓晨
	 * @date 2019/3/9 21:43
	 * @param user
	 * @param service
	 * @return
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
