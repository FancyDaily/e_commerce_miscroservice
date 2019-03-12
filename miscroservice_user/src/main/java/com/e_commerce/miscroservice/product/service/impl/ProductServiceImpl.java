package com.e_commerce.miscroservice.product.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.MsgResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BadWordUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.product.service.ProductService;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.product.vo.PageMineReturnView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
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
public class ProductServiceImpl extends BaseService implements ProductService {

	@Autowired
	private OrderCommonController orderService;
	@Autowired
	private UserCommonController userService;

	/**
	 * 功能描述:发布求助
	 * 作者:马晓晨
	 * 创建时间:2018/10/30 下午4:45
	 *
	 * @param
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void submitSeekHelp(TUser user, ServiceParamView param, String token) {
		user = userService.getUserById(68813260748488704L);
		//校验是否合规
		checkProductLegal(user, param);
		TService service = param.getService();
		//互助时检测用余额是否足够
		if (Objects.equals(service.getCollectType(), ProductEnum.COLLECT_TYPE_EACHHELP.getValue())) {
			checkEnoughTimeCoin(user, service);
		}
		boolean isCompany = user.getIsCompanyAccount().equals(IS_COMPANY_ACCOUNT_YES);
		// 组织发布
		if (isCompany) {
			// TODO 从用户模块调用
			// 查询当前用户所在的组织，写入到service中
//			Long companyId = getOwnCompanyId(user.getId());
//			param.getService().setCompanyId(companyId);+
			param.getService().setSource(ProductEnum.SOURCE_GROUP.getValue());
			submitCompanySeekHelp(user, param);
		} else {// 个人发布
			param.getService().setSource(ProductEnum.SOURCE_PERSONAL.getValue());
			submitUserSeekHelp(user, param, token);
		}
	}

	/**
	 * 当前发布的产品是否合规
	 * @param user
	 * @param param
	 */
	private void checkProductLegal(TUser user, ServiceParamView param) {
		TService service = param.getService();
		//检测重复性
		checkRepeat(user, service, param.getListServiceDescribe());
		//校验重复性求助服务是否合规
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			checkRepeatProductLegal(param.getService());
		}
		//单次的求助服务是否合规
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_FIXED.getValue()) && service.getEndTime() < System.currentTimeMillis()) {
			throw new MessageException("一次性求助的求助结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		if (BadWordUtil.isContaintBadWord(service.getServiceName(), 2)) {
			throw new MessageException("求助名称包含敏感词");
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
	private void checkEnoughTimeCoin(TUser user, TService service) {
		// 求助发布的单价
		Long collectTime = service.getCollectTime();
		// 求助需要的人员数量
		Integer servicePersonnel = service.getServicePersonnel();
		// 该求助需要的时间币
		long seekHelpPrice = collectTime * servicePersonnel;
		// 检测用户账户是否足够发布该求助
		if (user.getSurplusTime() + user.getCreditLimit() - user.getFreezeTime() < seekHelpPrice) {
			throw new MessageException("用户授信余额不足");
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
		//校验重复时间
		if (param.getService().getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			checkRepeatProductLegal(param.getService());
		}
		TService service = param.getService();
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_FIXED.getValue()) && service.getEndTime() < System.currentTimeMillis()) {
			throw new MessageException("一次性服务的服务结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		if (BadWordUtil.isContaintBadWord(service.getServiceName(), 2)) {
			throw new MessageException("服务名称包含敏感词");
		}
		// TODO 调用user模块
/*		if (!userService.ifAlreadyCert(user.getId())) {
			throw new MessageException("9527", "发布服务前请先进行实名认证");
		}*/
		//校验重复性服务是否合规
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			checkRepeatProductLegal(service);
		}
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

	@Override
	public List<TService> getListProduct(List<Long> productIds) {
		return productDao.selectListByIds(productIds);
	}

	@Override
	public List<TServiceDescribe> getListProductDesc(List<Long> productIds) {
		return productDao.getListProductDesc(productIds);
	}

	@Override
	public List<TServiceDescribe> getProductDesc(Long serviceId) {
		return productDao.getProductDesc(serviceId);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	@Override
	public void lowerFrame(TUser user, Long productId) {
		user = userService.getUserById(68813260748488704L);
		logger.info("id为{}的用户对商品id为{}进行了下架操作", user.getId(), productId);
		try {
			TService tService = productDao.selectByPrimaryKey(productId);
			tService.setStatus(ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue());
			tService.setUpdateUser(user.getId());
			tService.setUpdateUserName(user.getName());
			tService.setUpdateTime(System.currentTimeMillis());
			productDao.updateByPrimaryKeySelective(tService);
			//将该商品派生出来的订单的service_status进行修改
			orderService.synOrderServiceStatus(productId, ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue());
		} catch (Exception e) {
			logger.error(errInfo(e));
			throw new MessageException("下架失败");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void del(TUser user, Long productId) {
		user = userService.getUserById(68813260748488704L);
		logger.error("id为{}的用户对商品id为{}进行了删除操作", user.getId(), productId);
		try {
			TService tService = productDao.selectByPrimaryKey(productId);
			tService.setStatus(ProductEnum.STATUS_DELETE.getValue());
			tService.setUpdateUser(user.getId());
			tService.setUpdateUserName(user.getName());
			tService.setUpdateTime(System.currentTimeMillis());
			productDao.updateByPrimaryKeySelective(tService);
			//将该商品派生出来的订单的service_status进行修改
			orderService.synOrderServiceStatus(productId, ProductEnum.STATUS_DELETE.getValue());
		} catch (Exception e) {
			logger.error(errInfo(e));
			throw new MessageException("删除失败");
		}
	}

	@Override
	public void upperFrame(TUser user, Long productId) {
		// TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		logger.error("id为{}的用户对商品id为{}进行了上架操作", user.getId(), productId);
		try {
			TService tService = productDao.selectByPrimaryKey(productId);
			// TODO 判断时间段是否可以上架  上架后是否需要派生新的订单  进行状态判断
			tService.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
			tService.setUpdateUser(user.getId());
			tService.setUpdateUserName(user.getName());
			tService.setUpdateTime(System.currentTimeMillis());
			productDao.updateByPrimaryKeySelective(tService);
			//将该商品派生出来的订单的service_status进行修改
			orderService.synOrderServiceStatus(productId, ProductEnum.STATUS_UPPER_FRAME.getValue());
		} catch (Exception e) {
			logger.error(errInfo(e));
			throw new MessageException("重新上架失败");
		}
	}

	@Override
	public QueryResult<PageMineReturnView> pageMine(TUser user, Integer pageNum, Integer pageSize, Integer type) {
		// TODO 写死用户
		user = userService.getUserById(68813260748488704L);
		QueryResult<PageMineReturnView> result = new QueryResult<PageMineReturnView>();
		List<PageMineReturnView> listPageMineReturnView = new ArrayList<>();
		//分页插件
		Page<TService> page = PageHelper.startPage(pageNum, pageSize);
		//我发布的列表
		List<TService> listProductByUserId = productDao.getListProductByUserId(user.getId(), pageNum, pageSize, type);
		if (listProductByUserId.size() == 0) {
			result.setResultList(new ArrayList<>());
			result.setTotalCount(0L);
			return result;
		}
		List<Long> serviceIds = new ArrayList<>();
		listProductByUserId.stream().forEach(product -> serviceIds.add(product.getId()));
//		for (TService product : listProductByUserId) {
//			serviceIds.add(product.getId());
//		}
		//获取封面图  serviceId -> 封面图
		Map<Long, String> coverPic = new HashMap<>();
		List<TServiceDescribe> listProductDesc = productDao.getListProductDesc(serviceIds);
		if (listProductDesc != null && listProductDesc.size() > 0) {
			listProductDesc.stream().filter(serviceDesc -> serviceDesc.getIsCover().equals(IS_COVER_YES))
					.forEach(serviceDesc -> coverPic.put(serviceDesc.getServiceId(), serviceDesc.getUrl()));
		}
		for (TService tService : listProductByUserId) {
			PageMineReturnView returnView = new PageMineReturnView();
			returnView.setService(tService);
			tService.setNameAudioUrl("");
//			returnView.setImgUrl(coverPic.get(tService.getId()));
			returnView.setImgUrl("");
			if (Objects.equals(tService.getStatus(), ProductEnum.STATUS_UPPER_FRAME.getValue())) {
				returnView.setStatus("上架中");
			} else if (Objects.equals(tService.getStatus(), ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue()) || Objects.equals(tService.getStatus(), ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue())) {
				returnView.setStatus("已下架");
			}
			listPageMineReturnView.add(returnView);
		}
		result.setResultList(listPageMineReturnView);
		result.setTotalCount(page.getTotal());
		return result;
	}

	@Override
	public TService getProductById(Long serviceId) {
		return productDao.selectByPrimaryKey(serviceId);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void autoLowerFrameService(TService service) {
		productDao.updateByPrimaryKeySelective(service);
		orderService.synOrderServiceStatus(service.getId(), ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue());
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
		// 做一层校验
		if (param.getService().getCollectType() == null
				|| param.getService().getCollectTime().equals(ProductEnum.COLLECT_TYPE_COMMONWEAL.getValue())) {
			logger.error("组织发布的服务传递时间币的类型错误或组织不能发布公益时的服务 {}", param.getService().getCollectType());
			throw new MessageException("组织职能发布互助时的服务");
		}
		TService service = param.getService();
		setServiceCommonField(user, service);
		// 服务的总分和总次数
		// 总分
		int evaSum = user.getServTotalEvaluate();
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
			desc.setServiceId(service.getId()); // 服务id关联
			desc.setType(service.getType());
			setCommonServcieDescField(user, desc);
		}
		// 查询最新的一条服务是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe != null && listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		MsgResult msgResult = null;
		//派生出第一张订单
		msgResult = orderService.produceOrder(service.getId(), OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(),"");
		if (!msgResult.getCode().equals("200")) {
			throw new MessageException("派生订单失败");
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
		TService service = param.getService();
		service.setCollectType(ProductEnum.COLLECT_TYPE_EACHHELP.getValue()); // 互助时
		setServiceCommonField(user, service);
		// 总分
		if (user.getServeNum() == 0) { // 0次服务
			service.setTotalEvaluate(0); // 用户的总分
		} else { // 多次服务选平均
			double average = (double) user.getServTotalEvaluate() / user.getServeNum();
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
			setCommonServcieDescField(user, desc);
		}
		// 检测用户是否实名，没有实名的话就无法发布服务

		// 查询最新的一条服务是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		MsgResult msgResult = null;
		//派生出第一张订单
		msgResult = orderService.produceOrder(service.getId(), OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(),"");
		if (!msgResult.getCode().equals("200")) {
			throw new MessageException("派生订单失败");
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
		String[] weekDayArray = service.getDateWeekNumber().split(",");
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
	 * 功能描述:个人用户发布求助
	 * 作者:马晓晨
	 * 创建时间:2019年1月17日 下午3:52:52
	 *
	 * @param user
	 * @param param
	 * @param token
	 */
	private void submitUserSeekHelp(TUser user, ServiceParamView param, String token) {
		TService service = param.getService();
		service.setCollectType(ProductEnum.COLLECT_TYPE_EACHHELP.getValue());
		setServiceCommonField(user, service);
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (TServiceDescribe desc : listServiceDescribe) {
			if (StringUtil.isNotEmpty(desc.getDepict()) && BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
				throw new MessageException("求助描述中包含敏感词");
			}
			desc.setType(service.getType());
			desc.setServiceId(service.getId()); // 求助id关联
			setCommonServcieDescField(user, desc);
		}
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		MsgResult msgResult = null;
		//派生出第一张订单
		msgResult = orderService.produceOrder(service.getId(), OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(),"");
		if (!msgResult.getCode().equals("200")) {
			throw new MessageException("派生订单失败");
		}
		// TODO 使用用户模块
		// 增加成长值 刷新缓冲
//		TUser addGrowthUser = growthValueService.addGrowthValue(user,
//				GrowthValueEnum.GROWTH_TYPE_PUBLISH_SERV_REQUIRE.getCode());
//		userService.flushRedisUser(token, addGrowthUser);
	}


	/**
	 * 组织发布求助
	 *
	 * @param user  组织用户
	 * @param param 发布参数view
	 */
	private void submitCompanySeekHelp(TUser user, ServiceParamView param) {
		// 做一层校验
//		if (param.getService().getCollectType() == null) {
//			logger.error("组织发布的求助传递时间币的类型错误");
//			throw new MessageException("请选择是互助时还是公益时");
//		}
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
		TService service = param.getService();
		if (!user.getUserType().equals(IS_PUBLIC_WELFARE_YES)) {
			throw new MessageException("您不是公益组织，没有权限发布公益时的项目");
		}
		setServiceCommonField(user, service);
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe desc = listServiceDescribe.get(i);
			if (StringUtil.isNotEmpty(desc.getDepict())) {
				if (BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
					throw new MessageException("求助描述中包含敏感词");
				}
			}
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
		MsgResult msgResult = null;
		//派生出第一张订单
		msgResult = orderService.produceOrder(service.getId(), OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(),"");
		if (!msgResult.getCode().equals("200")) {
			throw new MessageException("派生订单失败");
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
		TService service = param.getService();
		setServiceCommonField(user, service);
		// 插入求助服务图片及描述
		List<TServiceDescribe> listServiceDescribe = param.getListServiceDescribe();
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe desc = listServiceDescribe.get(i);
			if (StringUtil.isNotEmpty(desc.getDepict()) && BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
				throw new MessageException("求助描述中包含敏感词");
			}
			desc.setServiceId(service.getId()); // 求助id关联
			desc.setType(service.getType());
			setCommonServcieDescField(user, desc);
		}
		productDao.insert(service);
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		MsgResult msgResult = null;
		//派生出第一张订单
		msgResult = orderService.produceOrder(service.getId(), OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(),"");
		if (!msgResult.getCode().equals("200")) {
			throw new MessageException("派生订单失败");
		}
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

}
