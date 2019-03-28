package com.e_commerce.miscroservice.product.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
import com.e_commerce.miscroservice.commons.enums.application.OrderEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.exception.colligate.NoEnoughCreditException;
import com.e_commerce.miscroservice.commons.util.colligate.BadWordUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.product.dao.serviceSummaryDao;
import com.e_commerce.miscroservice.product.service.ProductService;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.product.vo.DetailProductView;
import com.e_commerce.miscroservice.product.vo.PageMineReturnView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
import com.e_commerce.miscroservice.user.controller.UserCommonController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
	@Autowired
	private MessageCommonController messageService;

	@Autowired
	private serviceSummaryDao serviceSummaryDao;

	/**
	 * 功能描述:发布求助
	 * 作者:马晓晨
	 * 创建时间:2018/10/30 下午4:45
	 *
	 * @param
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void submitSeekHelp(TUser user, ServiceParamView param, String token) {
		user = userService.getUserById(user.getId());
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
			// 查询当前用户所在的组织，写入到service中
			Long companyId = userService.getOwnCompanyId(user.getId());
			param.getService().setCompanyId(companyId);
			param.getService().setSource(ProductEnum.SOURCE_GROUP.getValue());
			submitCompanySeekHelp(user, param);
		} else {// 个人发布
			param.getService().setSource(ProductEnum.SOURCE_PERSONAL.getValue());
			submitUserSeekHelp(user, param, token);
		}
	}

	/**
	 * 当前发布的产品是否合规
	 *
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
	 *
	 * @param user
	 * @param service
	 * @return
	 * @author 马晓晨
	 * @date 2019/3/9 21:43
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
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void submitService(TUser user, ServiceParamView param, String token) {
		user = userService.getUserById(user.getId());
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
		if (!user.getAuthenticationStatus().equals(AppConstant.AUTH_STATUS_YES)) {
			throw new MessageException("9527", "发布服务前请先进行实名认证");
		}
		//校验重复性服务是否合规
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			checkRepeatProductLegal(service);
		}
		// 组织发布
		boolean isCompany = user.getIsCompanyAccount().equals(IS_COMPANY_ACCOUNT_YES);
		if (isCompany) {
			Long companyId = userService.getOwnCompanyId(user.getId());
			param.getService().setCompanyId(companyId);
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

	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void lowerFrame(TUser user, Long productId) {
		user = userService.getUserById(user.getId());
		logger.info("id为{}的用户对商品id为{}进行了下架操作", user.getId(), productId);
		try {
			TService tService = productDao.selectByPrimaryKey(productId);
			boolean upperStatus = tService.getStatus().equals(ProductEnum.STATUS_WAIT_EXAMINE.getValue())
					|| tService.getStatus().equals(ProductEnum.STATUS_UPPER_FRAME.getValue());
			if (!upperStatus) {
				throw new MessageException("当前状态无法进行下架");
			}
			Long currentTime = System.currentTimeMillis();
			tService.setStatus(ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue());
			tService.setUpdateUser(user.getId());
			tService.setUpdateUserName(user.getName());
			tService.setUpdateTime(currentTime);
			productDao.updateByPrimaryKeySelective(tService);
			//将该商品派生出来的订单的service_status进行修改
			orderService.synOrderServiceStatus(productId, ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue());
			// 手动下架不需要发送通知
//			String title = "";
//			String content = "";
//			messageService.messageSave(tService.getId(), new AdminUser(), title, content, tService.getUserId(), currentTime);
			// TODO 服务通知
		} catch (Exception e) {
			logger.error(errInfo(e));
			throw new MessageException("下架失败");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void del(TUser user, Long productId) {
		logger.info("id为{}的用户对商品id为{}进行了删除操作", user.getId(), productId);
		TService tService = productDao.selectByPrimaryKey(productId);
		boolean lowerStatus = tService.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue())
				|| tService.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue())
				|| tService.getStatus().equals(ProductEnum.STATUS_EXAMINE_NOPASS.getValue());
		if (!lowerStatus) {
			throw new MessageException("当前状态无法删除");
		}
		try {
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
	@Transactional(rollbackFor = Exception.class)
	public void upperFrame(TUser user, Long productId) {
		user = userService.getUserById(user.getId());
		logger.info("id为{}的用户对商品id为{}进行了上架操作", user.getId(), productId);
		try {
			TService tService = productDao.selectByPrimaryKey(productId);
			boolean lowerStatus = tService.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue())
					|| tService.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue())
					|| tService.getStatus().equals(ProductEnum.STATUS_EXAMINE_NOPASS.getValue());
			if (!lowerStatus) {
				throw new MessageException("当前状态无法上架");
			}
			if (Objects.equals(tService.getTimeType(), ProductEnum.TIME_TYPE_FIXED.getValue())) {
				throw new MessageException("一次性的互助无法重新上架");
			}
			checkRepeatProductLegal(tService);
			tService.setStatus(ProductEnum.STATUS_UPPER_FRAME.getValue());
			tService.setUpdateUser(user.getId());
			tService.setUpdateUserName(user.getName());
			tService.setUpdateTime(System.currentTimeMillis());
			//将该商品派生出来的订单的service_status进行修改

			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					super.afterCommit();
					orderService.synOrderServiceStatus(productId, ProductEnum.STATUS_UPPER_FRAME.getValue());
				}
			});
			orderService.produceOrder(tService, OrderEnum.PRODUCE_TYPE_UPPER.getValue(), "");
			productDao.updateByPrimaryKeySelective(tService);
		} catch (NoEnoughCreditException e) {
			logger.info("没有足够的授信，无法上架 >>>>>>");
			throw new MessageException("没有足够的授信");
		} catch (Exception e) {
			logger.error(errInfo(e));
			throw new MessageException("重新上架失败");
		}
	}

	@Override
	public QueryResult<PageMineReturnView> pageMine(TUser user, Integer pageNum, Integer pageSize, Integer type) {
		user = userService.getUserById(user.getId());
		QueryResult<PageMineReturnView> result = new QueryResult<PageMineReturnView>();
		List<PageMineReturnView> listPageMineReturnView = new ArrayList<>();
		//分页插件
		Page<TService> page = PageHelper.startPage(pageNum, pageSize);
		//我发布的列表
		List<TService> listProductByUserId = productDao.getListProductByUserId(user.getId(), type);
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
			returnView.setImgUrl(coverPic.get(tService.getId()));
//			returnView.setImgUrl("");
			if (Objects.equals(tService.getStatus(), ProductEnum.STATUS_UPPER_FRAME.getValue())) {
				returnView.setStatus("上架中");
			} else if (Objects.equals(tService.getStatus(), ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue())
					|| Objects.equals(tService.getStatus(), ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue())) {
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
	@Transactional(rollbackFor = Exception.class)
	public void autoLowerFrameService(TService service, Integer type) {
		Long currentTime = System.currentTimeMillis();
		service.setStatus(ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue());
		service.setUpdateTime(currentTime);
		productDao.updateByPrimaryKeySelective(service);
		orderService.synOrderServiceStatus(service.getId(), ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue());
		String title = "%s下架通知";
		String content = null;
		if (Objects.equals(type, 1)) {//超过结束时间下架
			content = "您发布的%s“%s”，由于已超过原定结束时间，已自动下架。您可以修改时间后重新上架。";
		} else {  // 互助时不足下架
			content = "您发布的%s“%s”，由于互助时不足，无法继续生成订单，已自动下架。您可以赚取足够的互助时重新上架。";
		}
		if (Objects.equals(type, ProductEnum.TYPE_SEEK_HELP.getValue())) {
			title = String.format(title, "求助");
			content = String.format(content, "求助", service.getServiceName());
		} else {
			title = String.format(title, "服务");
			content = String.format(content, "服务", service.getServiceName());
		}
		messageService.messageSave(service.getId(), new AdminUser(), title, content, service.getUserId(), currentTime);
		// TODO 发送服务通知
	}

	@Override
	public DetailProductView detail(TUser user, Long serviceId) {
		DetailProductView productView = new DetailProductView();
		TService service = productDao.selectByPrimaryKey(serviceId);
		List<TServiceDescribe> productDesc = productDao.getProductDesc(serviceId);
		productView.setService(service);
		productView.setDesc(productDesc);
		for (TServiceDescribe tServiceDescribe : productDesc) {
			if (Objects.equals(tServiceDescribe.getIsCover(), IS_COVER_YES)) {
				productView.setImgUrl(tServiceDescribe.getUrl());
				break;
			}
		}
		return productView;
	}

	@Override
	public void updateServiceByKey(TService service) {
		productDao.updateByPrimaryKeySelective(service);
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
		productDao.insert(service);
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
		if (listServiceDescribe != null && listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		try {
			orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(), "");
		} catch (NoEnoughCreditException e) {
			throw new MessageException("没有足够的授信");
		}
		userService.addPublishTimes(user, ProductEnum.TYPE_SERVICE.getValue());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				updateServiceByKey(service);
			}
		});
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
		if (Objects.equals(user.getAuthenticationStatus(), AppConstant.AUTH_STATUS_NO)) {
			throw new MessageException("请先实名后再发布服务");
		}
		// 查询最新的一条服务是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		productDao.insert(service);
//		checkRepeat(user, service, listServiceDescribe);
		for (TServiceDescribe desc : listServiceDescribe) {
			if (StringUtil.isNotEmpty(desc.getDepict()) && BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
				throw new MessageException("服务描述中包含敏感词");
			}
			desc.setServiceId(service.getId()); // 服务id关联
			desc.setType(service.getType());
			setCommonServcieDescField(user, desc);
		}
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		try {
			orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(), "");
		} catch (NoEnoughCreditException e) {
			throw new MessageException("没有足够的授信");
		}
		userService.addPublishTimes(user, ProductEnum.TYPE_SERVICE.getValue());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				//增加成长值
				TUser tUser = userService.getUserById(user.getId());
				userService.taskComplete(tUser, GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_SERV_SEND, 1);
				updateServiceByKey(service);
			}
		});
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
		String startBeginDate;
		if (DateUtil.parse(service.getStartDateS() + service.getEndTimeS()) < System.currentTimeMillis()) {
			startBeginDate = DateUtil.getDate(System.currentTimeMillis());
		} else {
			startBeginDate = service.getStartDateS();
		}

		for (int i = 0; i < weekDayArray.length; i++) {
			int weekDay = Integer.parseInt(weekDayArray[i]);
			long countWeek = DateUtil.countWeek(startBeginDate, service.getEndDateS(), weekDay);
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
		productDao.insert(service);
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
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		try {
			orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(), "");
		} catch (NoEnoughCreditException e) {
			throw new MessageException("没有足够的授信");
		}

		//增加发布次数
		userService.addPublishTimes(user, ProductEnum.TYPE_SEEK_HELP.getValue());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				// 增加成长值
				TUser tUser = userService.getUserById(user.getId());
				userService.taskComplete(tUser, GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_HELP_SEND, 1);
				updateServiceByKey(service);
			}
		});
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
		// 查询最新的一条是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
//		checkRepeat(user, service, listServiceDescribe);
		productDao.insert(service);
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
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		try {
			orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(), "");
		} catch (NoEnoughCreditException e) {
			throw new MessageException("没有足够的授信");
		}
		userService.addPublishTimes(user, ProductEnum.TYPE_SEEK_HELP.getValue());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				updateServiceByKey(service);
			}
		});
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
		productDao.insert(service);
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
		if (listServiceDescribe.size() > 0) {
			productDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		try {
			orderService.produceOrder(service, OrderEnum.PRODUCE_TYPE_SUBMIT.getValue(), "");
		} catch (NoEnoughCreditException e) {
			throw new MessageException("没有足够的授信");
		}
		userService.addPublishTimes(user, ProductEnum.TYPE_SEEK_HELP.getValue());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCompletion(int status) {
				super.afterCompletion(status);
				updateServiceByKey(service);
			}
		});
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


	/**
	 * 发布精彩瞬间
	 *
	 * @param serviceId
	 * @param description
	 * @param url
	 * @param nowUser
	 */
	public void sendServiceSummary(Long serviceId, String description, String url, TUser nowUser) {
		long nowTime = System.currentTimeMillis();
		TServiceSummary serviceSummary = new TServiceSummary();
		serviceSummary.setServiceId(serviceId);
		serviceSummary.setDescription(description);
		serviceSummary.setUrl(url);
		serviceSummary.setCreateTime(nowTime);
		serviceSummary.setCreateUser(nowUser.getId());
		serviceSummary.setCreateUserName(nowUser.getName());
		serviceSummary.setUpdateTime(nowTime);
		serviceSummary.setUpdateUser(nowUser.getId());
		serviceSummary.setUpdateUserName(nowUser.getName());
		serviceSummaryDao.saveServiceSummary(serviceSummary);
	}

	/**
	 * 查找精彩瞬间
	 *
	 * @param serviceId
	 * @return
	 */
	public TServiceSummary findServiceSummary(Long serviceId) {
		return serviceSummaryDao.selectServiceSummaryByServiceId(serviceId);
	}

	@Override
	public Map<String, Long> getUserAvaliableMoney(TUser user) {
		user = userService.getUserById(user.getId());
		Map<String, Long> data = new HashMap<>();
		data.put("avaliableMoney", user.getSurplusTime() + user.getCreditLimit());
		return data;
	}
}
