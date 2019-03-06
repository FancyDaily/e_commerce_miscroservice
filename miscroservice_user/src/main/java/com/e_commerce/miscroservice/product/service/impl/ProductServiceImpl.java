package com.e_commerce.miscroservice.product.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BadWordUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.controller.OrderController;
import com.e_commerce.miscroservice.product.service.ProductService;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 *
 */
@Service
public class ProductServiceImpl extends BaseService implements ProductService {

	@Autowired
	OrderController orderService;
	/**
	 * 功能描述:发布求助
	 * 作者:马晓晨
	 * 创建时间:2018/10/30 下午4:45
	 *
	 * @param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	@Override
	public void submitSeekHelp(TUser user, ServiceParamView param, String token) {
/*		boolean isCompany = param.getService().getSource() != null
				&& param.getService().getSource().equals(ProductEnum.SOURCE_GROUP.getValue());*/
		boolean isCompany = user.getIsCompanyAccount().equals(IS_COMPANY_ACCOUNT_YES);
		// 组织发布
		if (isCompany) {
			// TODO 从用户模块调用
			// 查询当前用户所在的组织，写入到service中
//			Long companyId = getOwnCompanyId(user.getId());
//			param.getService().setCompanyId(companyId);
			//这一层可以判断出是组织，将source字段值写为组织
			param.getService().setSource(ProductEnum.SOURCE_GROUP.getValue());
			submitCompanySeekHelp(user, param);
		} else {// 个人发布
			//这一层可判断是个人发布，将source字段值写为个人
			param.getService().setSource(ProductEnum.SOURCE_PERSONAL.getValue());
			submitUserSeekHelp(user, param, token);
		}
	}

	/**
	 * 功能描述:发布服务
	 * 作者:马晓晨
	 * 创建时间:2018年10月31日 下午3:02:35
	 *
	 * @param user  当前发布用户
	 * @param param 发布服务所需要的参数
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	@Override
	public void submitService(TUser user, ServiceParamView param, String token) {
		// 组织发布
		boolean isCompany = user.getIsCompanyAccount().equals(IS_COMPANY_ACCOUNT_YES);
		if (isCompany) {
			//  TODO 从用户模块那里调用
//			Long companyId = getOwnCompanyId(user.getId());
//			param.getService().setCompanyId(companyId);
			//这一层可以判断出是组织，将source字段值写为组织
			param.getService().setSource(ProductEnum.SOURCE_GROUP.getValue());
			submitCompanyService(user, param);
		} else {// 个人发布
			param.getService().setSource(ProductEnum.SOURCE_PERSONAL.getValue());
			submitUserService(user, param, token);
		}
	}

	/**
	 * 功能描述:组织用来发布服务
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午9:47:30
	 *
	 * @param user  当前的组织用户
	 * @param param 发布服务的参数
	 */
	private void submitCompanyService(TUser user, ServiceParamView param) {
//		user = userDao.selectByPrimaryKey(user.getId());
		// 这里做一层检测 检测用户是否是组织用户
		if (!Objects.equals(user.getIsCompanyAccount(), 1)) {
			logger.error("非法用户登录组织，用户ID为{}", user.getId());
			throw new MessageException("用户不是组织用户，请重新登录");
		}
		// 做一层校验
		if (param.getService().getCollectType() == null
				|| param.getService().getCollectTime().equals(ProductEnum.COLLECT_TYPE_COMMONWEAL.getValue())) {
			logger.error("组织发布的服务传递时间币的类型错误或组织不能发布公益时的服务 {}", param.getService().getCollectType());
			throw new MessageException("组织职能发布互助时的服务");
		}
		// TODO 组织发布服务
/*		if (param.getService().getTimeType() == 1 && param.getService().getServicePersonnel() > 1) {
			throw new MessageException("暂时无法发布一对多的重复性服务，请修改时间或人数后重新尝试");
		}*/
		long nowTime = System.currentTimeMillis();
		TService service = param.getService();
		if (service.getEndTime() < nowTime && service.getTimeType().equals(0)) {
			throw new MessageException("一次性服务的服务结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		String serviceName = service.getServiceName();
		if (BadWordUtil.isContaintBadWord(serviceName, 2)) {
			throw new MessageException("服务名称包含敏感词");
		}
		// 如果是上架,先将之前那条记录改为状态8手动下架
		if (service.getId() != null && service.getId() != 0) {
			delService(service);
		}
		service.setId(snowflakeIdWorker.nextId());
		// TODO 审核暂时先拿掉
//		service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
		service.setUserId(user.getId());
		service.setCollectType(ProductEnum.COLLECT_TYPE_EACHHELP.getValue());
		setServiceCommonField(user, service);
		// 服务的总分和总次数
		// 总分
		int evaSum = user.getTotalEvaluate();
		// 总次数
		int servSum = user.getServeNum();
		if (servSum == 0) { // 0次服务
			service.setTotalEvaluate(0); // 用户的总分
		} else { // 多次服务选平均
			double average = (double) evaSum / servSum;
			average = average * 10000;
			int round = (int) Math.round(average);
			service.setTotalEvaluate(round); // 用户的总分
		}
		service.setServeNum(user.getServeNum()); // 用户的服务数量
		service.setIsValid(IS_VALID_YES);
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (TServiceDescribe desc : listServiceDescribe) {
			if (StringUtil.isNotEmpty(desc.getDepict())) {
				if (BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
					throw new MessageException("服务描述中包含敏感词");
				}
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 服务id关联
			desc.setType(service.getType());
			desc.setCreateUser(user.getId());
			setCommonServcieDescField(user, desc);
		}
		// 检测用户是否实名，没有实名的话就无法发布服务
		// 组织发布服务需要实名检测
		// TODO 调用用户模块
/*		if (!userService.ifAlreadyCert(user.getId())) {
			throw new MessageException("9527", "发布服务前请先进行实名认证");
		}*/
		// 查询最新的一条服务是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe != null && listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
	}

	/**
	 * 功能描述:个人发布服务（小程序的发布服务）
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午9:41:27
	 *
	 * @param user  当前用户
	 * @param param 发布服务的参数
	 * @param token 当前用户的token
	 */
	private void submitUserService(TUser user, ServiceParamView param, String token) {
//		user = userDao.selectByPrimaryKey(user.getId());
		// 该用户没有被邀请
/*		if (param.getService().getTimeType() == 1 && param.getService().getServicePersonnel() > 1) {
			throw new MessageException("暂时无法发布一对多的重复性服务，请修改时间或人数后重新尝试");
		}*/
		long nowTime = System.currentTimeMillis();
		TService service = param.getService();
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_FIXED.getValue()) && service.getEndTime() < nowTime) {
			throw new MessageException("一次性服务的服务结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		if (BadWordUtil.isContaintBadWord(service.getServiceName(), 2)) {
			throw new MessageException("服务名称包含敏感词");
		}
		//校验重复性服务是否合规
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			checkRepeatProductLegal(service);
		}
		// TODO 上架新逻辑
		// 如果是上架,先将之前那条记录改为状态8手动下架
//		if (service.getId() != null && service.getId() != 0) {
//			delService(service);
//		}
		service.setId(snowflakeIdWorker.nextId());
		// TODO 审核暂时先拿掉
//		service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
		service.setUserId(user.getId());
		service.setCollectType(ProductEnum.COLLECT_TYPE_EACHHELP.getValue()); // 互助时
		setServiceCommonField(user, service);
		// 总分
		if (user.getServeNum() == 0) { // 0次服务
			service.setTotalEvaluate(0); // 用户的总分
		} else { // 多次服务选平均
			double average = (double) user.getTotalEvaluate() / user.getServeNum();
			average = average * 10000;
			int round = (int) Math.round(average);
			service.setTotalEvaluate(round); // 用户的总分
		}
		service.setServeNum(user.getServeNum()); // 用户的服务数量
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (TServiceDescribe desc : listServiceDescribe) {
			if (StringUtil.isNotEmpty(desc.getDepict()) && BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
				throw new MessageException("服务描述中包含敏感词");
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 服务id关联
			desc.setType(service.getType());
			desc.setCreateUser(user.getId());
			setCommonServcieDescField(user, desc);
		}
		// 检测用户是否实名，没有实名的话就无法发布服务
		// TODO 调用user模块
/*		if (!userService.ifAlreadyCert(user.getId())) {
			throw new MessageException("9527", "发布服务前请先进行实名认证");
		}*/
		// 查询最新的一条服务是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//TODO 调用订单模块 生成订单
		if (!orderService.produceOrder(service)) {
			logger.error("调用订单模块失败>>>>>>");
			throw new MessageException("发布超时，请重新发布");
		}
		// 增加成长值  TODO  调用user模块
//		TUser addGrowthValue = growthValueService.addGrowthValue(user,
//				GrowthValueEnum.GROWTH_TYPE_PUBLISH_SERV_SERVICE.getCode());
//		userService.flushRedisUser(token, addGrowthValue);
	}

	/**
	 * 检验重复性商品是否合规
	 *
	 * @param service 商品
	 */
	private void checkRepeatProductLegal(TService service) {
		String endDateTime = service.getEndDateS() + service.getEndTimeS();
		if (DateUtil.parse(endDateTime) < System.currentTimeMillis()) {
			throw new MessageException("重复的结束时间不能小于当前时间");
		}
		//是否包含用户选择的周期
		boolean isContainWeek = false;
		String[] weekDayArray = service.getDateWeek().split(",");
		for (int i = 0; i < weekDayArray.length; i++) {
			int weekDay = Integer.parseInt(weekDayArray[i]);
			long countWeek = DateUtil.countWeek(service.getStartDateS(), service.getEndDateS(), weekDay);
			if (countWeek > 0) {
				isContainWeek = true;
			}
		}
		if (!isContainWeek) {
			throw new MessageException("发布的重复时间段内不包含您选择的星期");
		}
	}




	/**
	 * 功能描述: 将service列表加载到map中 形成 map<用户id，该用户id下的service列表>
	 * 作者:马晓晨
	 * 创建时间:2018年11月6日 下午5:08:14
	 *
	 * @param listService
	 */
	private Map<Long, List<TService>> loadUserService(List<TService> listService) {
		Map<Long, List<TService>> userServiceInfo = new HashMap<>();
		for (int i = 0; i < listService.size(); i++) {
			// 在map中对用户进行分组
			TService service = listService.get(i);
			if (userServiceInfo.get(service.getUserId()) == null) {
				List<TService> list = new ArrayList<>();
				list.add(service);
				userServiceInfo.put(service.getUserId(), list);
			} else {
				List<TService> list = userServiceInfo.get(service.getUserId());
				list.add(service);
			}
		}
		return userServiceInfo;
	}


	/**
	 * 功能描述:是否两个求助服务相同
	 * 作者:马晓晨
	 * 创建时间:2018年11月15日 下午12:00:28
	 *
	 * @param service             前一条求助服务
	 * @param listServiceDescribe 前一条求助服务的详情
	 * @param myNewService        刚发的求助服务
	 * @param listNewServiceDesc  刚发的求助服务的详情
	 * @return
	 */
	private boolean isServiceEqual(TService service, List<TServiceDescribe> listServiceDescribe, TService myNewService,
								   List<TServiceDescribe> listNewServiceDesc) {
		// 类型不同
		if (service.getServiceTypeId() != myNewService.getServiceTypeId().longValue()) {
			return false;
		}
		// 来源不同
		if (service.getSource() != myNewService.getSource()) {
			return false;
		}
		// 名称不同
		if (!service.getServiceName().trim().equals(myNewService.getServiceName().trim())) {
			return false;
		}
		// 人数不同
		if (service.getServicePersonnel() != myNewService.getServicePersonnel().intValue()) {
			return false;
		}
		// 开始时间不同
		if (service.getStartTime() != myNewService.getStartTime().longValue()) {
			return false;
		}
		// 结束时间不同
		if (service.getEndTime() != myNewService.getEndTime().longValue()) {
			return false;
		}
		// 线上线下不同
		if (service.getServicePlace() != myNewService.getServicePlace()) {
			return false;
		}
		// 标签不同
		if (!service.getLabels().trim().equals(myNewService.getLabels().trim())) {
			return false;
		}
		// 如果是线下需求
		if (service.getServicePlace() == ProductEnum.PLACE_UNDERLINE.getValue()) {
			// 地址
			if (!service.getAddressName().trim().equals(myNewService.getAddressName().trim())) {
				return false;
			}
			// 经度
			if (service.getLongitude() != myNewService.getLongitude().longValue()) {
				return false;
			}
			// 纬度
			if (service.getLatitude() != myNewService.getLatitude().longValue()) {
				return false;
			}
		}
		// 公益时和互助时类型不同
		if (!Objects.equals(service.getCollectType(), myNewService.getCollectType())) {
			return false;
		}
		// 时间币不同
		if (service.getCollectTime() != myNewService.getCollectTime().longValue()) {
			return false;
		}
		// 详情列表长度是否相同
		if (listServiceDescribe.size() != listNewServiceDesc.size()) {
			return false;
		}
		// 详情是否相同
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe serviceDescribe = listServiceDescribe.get(i);
			TServiceDescribe newServiceDescribe = listNewServiceDesc.get(i);
			// 内容是否相同
			if (!serviceDescribe.getDepict().trim().equals(newServiceDescribe.getDepict().trim())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 功能描述:个人用户发布求助
	 * 作者:马晓晨
	 * 创建时间:2019年1月17日 下午3:52:52
	 *
	 * @param user
	 * @param param
	 * @param token
	 */
	private void submitUserSeekHelp(TUser user, ServiceParamView param, String token) {
		// 用户剩余时间币
		long currentTime = System.currentTimeMillis();
		// 可用时间币 = 用户余额 + 授信额度 - 冻结时间
		Long availableTime = user.getSurplusTime() + user.getCreditLimit() - user.getFreezeTime();
		// 求助发布需要的时间币
		TService service = param.getService();
		// 该求助需要的时间币  求助单价 * 求助人数
		long seekHelpPrice = service.getCollectTime() * service.getServicePersonnel();
		// 检测用户账户是否足够发布该求助
		if (availableTime < seekHelpPrice) {
			throw new MessageException("用户余额不足");
		}
		if (service.getEndTime() < currentTime) {
			throw new MessageException("求助结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		if (BadWordUtil.isContaintBadWord(service.getServiceName(), 2)) {
			throw new MessageException("求助名称包含敏感词");
		}
		// 如果是上架,先将之前那条记录改为状态8删除
		if (service.getId() != null) {
			delService(service);
		}
		service.setId(snowflakeIdWorker.nextId());
		// 待审核 TODO 暂时不做审核限制，直接是待开始
//		service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
		service.setUserId(user.getId());
		service.setCollectType(ProductEnum.COLLECT_TYPE_EACHHELP.getValue());
		setServiceCommonField(user, service);
//		service.setTimeType(0); // 区分固定时间还是重复周期 所有求助都是固定时间，所以为0
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (TServiceDescribe desc : listServiceDescribe) {
			if (StringUtil.isNotEmpty(desc.getDepict())) {
				if (BadWordUtil.isContaintBadWord(desc.getDepict(), 2)){
					throw new MessageException("求助描述中包含敏感词");
				}
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 求助id关联
			setCommonServcieDescField(user, desc);
		}
		// 查询最新的一条是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		orderService.produceOrder(service);
		// 3、扣除用户时间币，生成交易流水
		// 将用户时间币冻结
		user.setFreezeTime(user.getFreezeTime() + seekHelpPrice);
		user.setUpdateTime(System.currentTimeMillis());
		//TODO 使用用户模块
//		userDao.updateByPrimaryKeySelective(user);
		// 生成冻结记录
		// TODO 使用用户模块
	/*	TUserFreeze userFreeze = new TUserFreeze();
		userFreeze.setId(snowflakeIdWorker.nextId());
		userFreeze.setUserId(user.getId());
		userFreeze.setOrderId(service.getId());
		userFreeze.setServiceName(service.getServiceName());
		userFreeze.setFreezeTime(seekHelpPrice); // 冻结金额
		userFreeze.setCreateTime(System.currentTimeMillis());
		userFreeze.setCreateUser(user.getId());
		userFreeze.setCreateUserName(user.getName());
		userFreeze.setUpdateTime(System.currentTimeMillis());
		userFreeze.setUpdateUser(user.getId());
		userFreeze.setUpdateUserName(user.getName());
		userFreeze.setIsValid(IS_VALID_YES);
		userFreezeDao.insert(userFreeze);
		// 增加成长值 刷新缓冲
		TUser addGrowthUser = growthValueService.addGrowthValue(user,
				GrowthValueEnum.GROWTH_TYPE_PUBLISH_SERV_REQUIRE.getCode());
		userService.flushRedisUser(token, addGrowthUser);*/
	}


	/**
	 * 组织发布求助
	 *
	 * @param user  组织用户
	 * @param param 发布参数view
	 */
	private void submitCompanySeekHelp(TUser user, ServiceParamView param) {
//		user = userDao.selectByPrimaryKey(user.getId());
		// 这里做一层检测 检测用户是否是组织用户
/*		if (!Objects.equals(user.getIsCompanyAccount(), 1)) {
			logger.error("非法用户登录，用户ID为{}", user.getId());
			throw new MessageException("用户不是组织用户，请重新登录");
		}*/
		// 做一层校验
		if (param.getService().getCollectType() == null) {
			logger.error("组织发布的求助传递时间币的类型错误");
			throw new MessageException("请选择是互助时还是公益时");
		}
		// 时间币类型为互助时
		if (param.getService().getCollectType().equals(ProductEnum.COLLECT_TYPE_EACHHELP.getValue())) {
			companySubmitEachHelp(user, param);
		} else { // 时间币类型为公益时
			companySubmitCommonweal(user, param);
		}
	}


	/**
	 * 组织发布公益时求助
	 *
	 * @param user  当前组织用户
	 * @param param 发布的参数view
	 */
	private void companySubmitCommonweal(TUser user, ServiceParamView param) {
		/*
		 * 公益时组织可以随便发，不需要检测组织是否有足够公益时 公益时不需要创建冻结流水
		 */
		long currentTime = System.currentTimeMillis();
		TService service = param.getService();
		if (service.getTimeType() == 0 && service.getEndTime() < currentTime) {
			throw new MessageException("求助结束时间不能小于当前时间");
		}

		// 进行标题敏感词检测
		String serviceName = service.getServiceName();
		if (BadWordUtil.isContaintBadWord(serviceName, 2)) {
			throw new MessageException("求助名称包含敏感词");
		}
		// 如果是上架,先将之前那条记录改为状态8删除
		// TODO 删除逻辑再定
//		if (service.getId() != null) {
//			delService(service);
//		}
		service.setId(snowflakeIdWorker.nextId());
		// TODO 暂时不做审核限制，直接是待开始
		// service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
		service.setUserId(user.getId());
		setServiceCommonField(user, service);
		// 组织发布互助时还是公益时由前端传递参数
		// service.setCollectType(1);
		service.setTimeType(0); // 区分固定时间还是重复周期 所有求助都是固定时间，所以为0
		service.setIsValid(IS_VALID_YES);
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe desc = listServiceDescribe.get(i);
			if (StringUtil.isNotEmpty(desc.getDepict())) {
				if (BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
					throw new MessageException("求助描述中包含敏感词");
				}
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 求助id关联
			desc.setType(service.getType());
			setCommonServcieDescField(user, desc);
		}
		// 查询最新的一条是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
	}

	/**
	 * 功能描述:组织发布的互助时
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午3:28:53
	 *
	 * @param user
	 * @param param
	 */
	private void companySubmitEachHelp(TUser user, ServiceParamView param) {
		// 1、检查用户的时间币是否足够
		long currentTime = System.currentTimeMillis();
		// 用户剩余时间币
		Long surplusTime = user.getSurplusTime();
		// 用户冻结时间币
		Long freezeTime = user.getFreezeTime();
		// 求助发布需要的时间币
		TService service = param.getService();
		// 求助发布的单价
		Long collectTime = service.getCollectTime();
		// 求助需要的人员数量
		Integer servicePersonnel = service.getServicePersonnel();
		// 该求助需要的时间币
		long seekHelpPrice = collectTime * servicePersonnel;
		// 检测用户账户是否足够发布该求助
		if (surplusTime - freezeTime < seekHelpPrice) {
			throw new MessageException("用户余额不足");
		}
		// 一次性求助需要判断时间
		if (Objects.equals(service.getTimeType(), ProductEnum.TIME_TYPE_FIXED.getValue())
				&& service.getEndTime() < currentTime) {
			throw new MessageException("求助结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		if (BadWordUtil.isContaintBadWord(service.getServiceName(), 2)) {
			throw new MessageException("求助名称包含敏感词");
		}
		// 如果是上架,先将之前那条记录改为状态8删除
		// TODO 删除逻辑再定
//		if (service.getId() != null) {
//			delService(service);
//		}
		service.setId(snowflakeIdWorker.nextId());
		// 待审核 TODO 暂时不做审核限制，直接是待开始
//			service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
		service.setUserId(user.getId());
		setServiceCommonField(user, service);
		service.setIsValid(IS_VALID_YES);
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe desc = listServiceDescribe.get(i);
			if (StringUtil.isNotEmpty(desc.getDepict()) && BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
				throw new MessageException("求助描述中包含敏感词");
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 求助id关联
			setCommonServcieDescField(user, desc);
		}
		// 查询最新的一条是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		// TODO 调用用户模块
//		// 3、扣除用户时间币，生成交易流水
//		// 将用户时间币冻结
//		user.setFreezeTime(freezeTime + seekHelpPrice);
//		user.setUpdateTime(currentTime);
//		userDao.updateByPrimaryKeySelective(user);
//		// 生成冻结记录
//		TUserFreeze userFreeze = new TUserFreeze();
//		userFreeze.setId(snowflakeIdWorker.nextId());
//		userFreeze.setUserId(user.getId());
//		userFreeze.setServiceId(service.getId());
//		userFreeze.setServiceName(service.getServiceName());
//		userFreeze.setFreezeTime(seekHelpPrice); // 冻结金额
//		userFreeze.setCreateTime(System.currentTimeMillis());
//		userFreeze.setCreateUser(user.getId());
//		userFreeze.setCreateUserName(user.getName());
//		userFreeze.setUpdateTime(System.currentTimeMillis());
//		userFreeze.setUpdateUser(user.getId());
//		userFreeze.setUpdateUserName(user.getName());
//		userFreeze.setIsValid(IS_VALID_YES);
//		userFreezeDao.insert(userFreeze);
	}

	/**
	 * 功能描述:检查是否和最新的一条重复
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午2:40:14
	 *
	 * @param user                当前用户
	 * @param service             当前的service
	 * @param listServiceDescribe 当前的service详情
	 */
	private void checkRepeat(TUser user, TService service, List<TServiceDescribe> listServiceDescribe) {
		TService myNewService = productDao.selectUserNewOneRecord(user.getId(), service.getType());
		if (myNewService != null) {
			List<TServiceDescribe> listNewServiceDesc = productDescribeDao.selectDescByServiceId(myNewService.getId());
			boolean isServiceEqual = isServiceEqual(service, listServiceDescribe, myNewService, listNewServiceDesc);
			if (isServiceEqual) { //改为一样的提示
				if (service.getType().equals(ProductEnum.TYPE_SEEK_HELP.getValue())) {
					throw new MessageException("您已发布相同的求助，请将之前的求助下架后再重新发布");
				} else {
					throw new MessageException("您已发布相同的服务，请将之前的服务下架后再重新发布");
				}
			}
		}
	}

	/**
	 * 功能描述:
	 * 作者:马晓晨
	 * 创建时间:2019年1月17日 下午6:21:17
	 *
	 * @param service
	 */
	private void delService(TService service) {
		Long id = service.getId();
		TService lowerFrameService = productDao.selectByPrimaryKey(id);
		// 将之前的service状态改为 8 下架不可见（已重新发布的）
		Integer status = lowerFrameService.getStatus();
		if (status != ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue()
				&& status != ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue()
				&& status != ProductEnum.STATUS_EXAMINE_NOPASS.getValue()) {
			throw new MessageException("当前状态不能上架，请重新尝试");
		}
		lowerFrameService.setStatus(ProductEnum.STATUS_DELETE.getValue());
		productDao.updateByPrimaryKeySelective(lowerFrameService);
	}

}
