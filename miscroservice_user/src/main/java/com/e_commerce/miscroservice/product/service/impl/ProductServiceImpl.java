package com.e_commerce.miscroservice.product.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BadWordUtil;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.controller.OrderController;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.dao.ProductDescDao;
import com.e_commerce.miscroservice.product.service.ProductService;
import com.e_commerce.miscroservice.product.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;


/**
 *
 */
@Service
public class ProductServiceImpl extends BaseService implements ProductService {

	@Autowired
	ProductDao productDao;
	@Autowired
	ProductDescDao serviceDescribeDao;
	@Autowired
	OrderController orderController;

	/**
	 * 功能描述:分页查询求助和服务   为兼容PC版本，做了加入访问组织权限的处理
	 * 作者:马晓晨
	 * 创建时间:2018年11月1日 上午10:39:14
	 *
	 * @param param
	 * @return
	 */
	@Override
	public QueryResult<PageServiceReturnView> list(PageServiceParamView param, TUser user) {
		QueryResult<PageServiceReturnView> result = new QueryResult<>();
		List<PageServiceReturnView> listReturn = new ArrayList<>();
		// 为了兼容PC端组织发布的权限显示问题，根据当前用户的组织查看当前用户是否有权限访问
		List<Long> companyIds = new ArrayList<>();
		if (user != null) { // 如果当前用户是登录状态，则查看当前用户加入了哪些组织
			companyIds = userCompanyDao.selectCompanyIdByUser(user.getId());
			param.setCurrentUserId(user.getId());
		}
		if (companyIds.size() == 0) {
			// 如果用户没有登录或者用户没有加入组织，则为了数据库查询的in语句不出错，加入一个1值。但是会出现效率问题
			companyIds.add(1L);
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
		Page<TService> page = PageHelper.startPage(param.getPageNum(), param.getPageSize());
		// 根据条件分页所有求助服务
		List<TService> listService = serviceDao.pageService(param);

		// 装载需要查询人数的求助服务id
		List<Long> serviceIds = new ArrayList<>();
		// 装载所有需要查询的用户id
		List<Long> userIds = new ArrayList<>();
		for (int i = 0; i < listService.size(); i++) {
			userIds.add(listService.get(i).getUserId());
			serviceIds.add(listService.get(i).getId());
		}
		// 查询出所有发布者
		if (userIds.size() == 0) {
			result.setResultList(listReturn);
			result.setTotalCount(0L);
			return result;
		}
		// 获取封面图
		Map<Long, TServiceDescribe> coverImgMap = getCoverImgMap(serviceIds);
		//组装 serviceId - num 的map  获取服务求助的报名人数
//		Map<Long, Long> serviceEnrollNum = getServiceEnrollNum(serviceIds);
		//获取发布人信息
		Map<Long, TUser> usersInfo = getUserInfo(userIds);
		//获取service类型
		Map<String, String> serviceTypeMap = getServiceTypeMap();
		// 遍历列表service，组装返回view
		for (int i = 0; i < listService.size(); i++) {
			PageServiceReturnView returnView = new PageServiceReturnView();
			TService service = listService.get(i);
			// 得到报名人数
			Long num = serviceEnrollNum.get(service.getId());
			//如果查不到报名人数，则为0
			num = num == null ? 0 : num;
			returnView.setEnrollPeopleNum(num);
			String serviceType = serviceTypeMap.get(service.getServiceTypeId() + "");
			if (serviceType == null) {
				returnView.setServiceType("");
			} else {
				returnView.setServiceType(serviceType);
			}
			returnView.setService(service);
			//设置封面图
			if (coverImgMap.get(service.getId()) != null) {
				returnView.setImgUrl(coverImgMap.get(service.getId()).getUrl());
			}
			TUser tUser = usersInfo.get(service.getUserId());
			// 进行部分字段映射
			BaseUserView userView = BeanUtil.copy(tUser, BaseUserView.class);
			returnView.setUser(userView);
			listReturn.add(returnView);
		}
		result.setResultList(listReturn);
		result.setTotalCount(page.getTotal());
		return result;
	}

	/**
	 * 功能描述:获取封面图map  serviceId -- serviceDesc
	 * 作者:马晓晨
	 * 创建时间:2019年2月26日 下午7:18:21
	 *
	 * @param serviceIds
	 * @return
	 */
	private Map<Long, TServiceDescribe> getCoverImgMap(List<Long> serviceIds) {
		//查询所有service的封面图,并以 serviceId  serviceDesc 的形式放到map中
		TServiceDescribeExample serviceDescExample = new TServiceDescribeExample();
		serviceDescExample.createCriteria().andServiceIdIn(serviceIds).andIsValidEqualTo(IS_VALID_YES).andIsCoverEqualTo(IS_COVER_YES);
		List<TServiceDescribe> listCoverImg = serviceDescribeDao.selectByExample(serviceDescExample);
		Map<Long, TServiceDescribe> coverImgMap = new HashMap<>();
		listCoverImg.forEach(coverImg -> coverImgMap.put(coverImg.getServiceId(), coverImg));
		return coverImgMap;
	}

	/**
	 * 功能描述:获取发布人信息 userId -- user
	 * 作者:马晓晨
	 * 创建时间:2019年2月26日 下午7:14:45
	 *
	 * @param userIds
	 * @return
	 */
	private Map<Long, TUser> getUserInfo(List<Long> userIds) {
		TUserExample userExample = new TUserExample();
		TUserExample.Criteria userCriteria = userExample.createCriteria();
		userCriteria.andIdIn(userIds);
		List<TUser> userList = userDao.selectByExample(userExample);
		Map<Long, TUser> usersInfo = new HashMap<>();
		for (int i = 0; i < userList.size(); i++) {
			usersInfo.put(userList.get(i).getId(), userList.get(i));
		}
		return usersInfo;
	}


	/**
	 * 功能描述: 获取服务求助的报名人数，如果没有人报名，则返回的map不存在该key和value
	 * 作者:马晓晨
	 * 创建时间:2019年2月15日 下午4:36:31
	 * @param serviceIds
	 * @return Map<Long   ,       Integer> key: serviceId  value: num
	 */
//	private Map<Long, Long> getServiceEnrollNum(List<Long> serviceIds) {
//		Map<Long, Long> serviceEnrollPeopleNumMap = new HashMap<Long, Long>();
//		//查询这些求助或服务的报名人数
//		if (serviceIds != null && serviceIds.size() != 0) {
//			//查询这些求助或服务的报名人数
//			List<MapEntity<Long, Long>> countEnrollPeopleByService = typeDictionariesDao.findListServiceEnrollNum(serviceIds);
//			//组装 serviceId - num 的map
//			for (int i = 0; i < countEnrollPeopleByService.size(); i++) {
//				MapEntity<Long, Long> mapEntity = countEnrollPeopleByService.get(i);
//				serviceEnrollPeopleNumMap.put(mapEntity.getKey(), mapEntity.getValue());
//			}
//		}
//		return serviceEnrollPeopleNumMap;
//	}


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
			Long companyId = getOwnCompanyId(user.getId());
			param.getService().setCompanyId(companyId);
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
	 * @param user
	 * @param param
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	@Override
	public void submitService(TUser user, ServiceParamView param, String token) {
		// 组织发布
		if (param.getService().getSource() != null
				&& param.getService().getSource().equals(ProductEnum.SOURCE_GROUP.getValue())) {
			// 查询当前用户所在的组织，写入到service中  TODO 从用户模块那里调用
	/*		Long companyId = getOwnCompanyId(user.getId());
			param.getService().setCompanyId(companyId);*/
			submitCompanyService(user, param);
		} else {// 个人发布
			submitUserService(user, param, token);
		}
	}

	/**
	 * 功能描述:组织用来发布服务
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午9:47:30
	 *
	 * @param user
	 * @param param
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
		service.setStatus(ProductEnum.STATUS_CAN_SIGN_UP.getValue());
		service.setUserId(user.getId());
		// 来源为组织
//		service.setSource(SERVICE_SOURCE_PERSON);// 来源为个人
		service.setType(SERVICE_TYPE_SERVICE); // 类型为服务
		// 互助时, 组织只能发公益时的求助，不能发公益时的服务
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
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe desc = listServiceDescribe.get(i);
			if (StringUtil.isNotEmpty(desc.getDepict())) {
				if (BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
					throw new MessageException("服务描述中包含敏感词");
				}
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 服务id关联
			desc.setType(SERVICE_TYPE_SERVICE);
			desc.setCreateUser(user.getId());
			setCommonServcieDescField(user, desc);
		}
		// 检测用户是否实名，没有实名的话就无法发布服务
		// 组织发布服务需要实名检测
		if (!userService.ifAlreadyCert(user.getId())) {
			throw new MessageException("9527", "发布服务前请先进行实名认证");
		}
		// 查询最新的一条服务是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		serviceDao.insert(service);
		if (listServiceDescribe != null && listServiceDescribe.size() > 0) {
			serviceDescribeDao.batchInsert(listServiceDescribe);
		}
	}

	/**
	 * 功能描述:个人发布服务（小程序的发布服务）
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午9:41:27
	 *
	 * @param user
	 * @param param
	 * @param token
	 */
	private void submitUserService(TUser user, ServiceParamView param, String token) {
//		user = userDao.selectByPrimaryKey(user.getId());
		// 该用户没有被邀请
/*		if (param.getService().getTimeType() == 1 && param.getService().getServicePersonnel() > 1) {
			throw new MessageException("暂时无法发布一对多的重复性服务，请修改时间或人数后重新尝试");
		}*/
		long nowTime = System.currentTimeMillis();
		TService service = param.getService();
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_FIXED) && service.getEndTime() < nowTime) {
			throw new MessageException("一次性服务的服务结束时间不能小于当前时间");
		}
		// 进行标题敏感词检测
		if (BadWordUtil.isContaintBadWord(service.getServiceName(), 2)) {
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
		service.setCollectType(1); // 互助时
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
		for (int i = 0; i < listServiceDescribe.size(); i++) {
			TServiceDescribe desc = listServiceDescribe.get(i);
			if (StringUtil.isNotEmpty(desc.getDepict()) && BadWordUtil.isContaintBadWord(desc.getDepict(), 2)) {
				throw new MessageException("服务描述中包含敏感词");
			}
			desc.setId(snowflakeIdWorker.nextId());
			desc.setServiceId(service.getId()); // 服务id关联
//			desc.setType(SERVICE_TYPE_SERVICE);
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
			serviceDescribeDao.batchInsert(listServiceDescribe);
		}
		//TODO 调用订单模块 生成订单
		boolean isProduceOrder = produceOrder(service);
		// 增加成长值  TODO  调用user模块
//		TUser addGrowthValue = growthValueService.addGrowthValue(user,
//				GrowthValueEnum.GROWTH_TYPE_PUBLISH_SERV_SERVICE.getCode());
//		userService.flushRedisUser(token, addGrowthValue);
	}

	/**
	 * 根据service派生订单
	 * @param service
	 * @return
	 */
	private boolean produceOrder(TService service) {
		TOrder order = BeanUtil.copy(service, TOrder.class);
		order.setId(snowflakeIdWorker.nextId());
		order.setConfirmNum(0);
		order.setEnrollNum(0);
		order.setServiceId(service.getId());
		//重复的订单的话  根据商品的重复时间生成第一张订单
		if (service.getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {

		}
	}

	/**
	 * 功能描述:服务求助详情
	 * 作者:马晓晨 创建时间:
	 * 2018年11月4日 下午3:33:25
	 *
	 * @param serviceId
	 * @param user
	 * @return
	 */
	@Override
	public DetailServiceReturnView serviceDetail(Long serviceId, TUser user) {
		DetailServiceReturnView result = new DetailServiceReturnView();
		// 查询出service TODO 调用公共方法先从redis获取
//		TService service = getServiceById(serviceId);
		if (serviceId == null) {
			logger.error("求助服务详情 serviceId 不能为空");
			throw new MessageException("详情的必填参数  serviceId 不能为空");
		}
		TService service = serviceDao.selectByPrimaryKey(serviceId);
		// 根据serviceId，查询出所有关联的service详情
		TServiceDescribeExample serviceDescExample = new TServiceDescribeExample();
		TServiceDescribeExample.Criteria serviceDescCriteria = serviceDescExample.createCriteria();
		serviceDescCriteria.andServiceIdEqualTo(serviceId);
		serviceDescCriteria.andIsValidEqualTo(IS_VALID_YES);
		List<TServiceDescribe> listServiceDesc = serviceDescribeDao.selectByExample(serviceDescExample);
		// 查询出发布人
		Long publisherId = service.getUserId();
		TUser publisher = userDao.selectByPrimaryKey(publisherId);
		// 如果从redis中传过来的当前用户为null，说明没有登录，显示用户可以报名和点赞和收藏的信息
		if (user == null) {
			result.setCareStatus(result.CARE_STATUS_SHOW_CARE);
			result.setCollectStatus(result.COLLECT_STATUS_NOT_COLLECT);
			result.setShowHelpStatus(result.SHOW_HELP_STATUS_SHOW_HELP);
		} else {
			// 查看发布人与当前用户的关系，进行关注显示状态的判断
			// 当前用户id
			Long currentUserId = user.getId();
			// 不显示关注
			if (currentUserId.equals(publisherId)) {
				result.setCareStatus(result.CARE_STATUS_NOT_SHOW);
			} else {
				// 查询当前用户是否关注发布者
				boolean isCare = isCare(currentUserId, publisherId);
				if (!isCare) { // 用户没有关注发布者，显示关注图标
					result.setCareStatus(result.CARE_STATUS_SHOW_CARE);
				} else { // 有记录，用户关注发布者，显示已关注图标
					result.setCareStatus(result.CARE_STATUS_SHOW_ALREADY_CARE);
				}
			}
			// 查看该求助服务与当前用户的关系，进行收藏状态的判断
			TUserCollectionExample userCollectionExample = new TUserCollectionExample();
			TUserCollectionExample.Criteria userCollectionCriteria = userCollectionExample.createCriteria();
			userCollectionCriteria.andUserIdEqualTo(currentUserId);
			userCollectionCriteria.andServiceIdEqualTo(serviceId);
			userCollectionCriteria.andIsValidEqualTo(IS_VALID_YES);
			long count = userCollectionDao.countByExample(userCollectionExample);
			if (count == 0) { // 如果没有记录，用户没有收藏该求助服务
				result.setCollectStatus(result.COLLECT_STATUS_NOT_COLLECT);
			} else {// 有记录，用户已经收藏该求助服务
				result.setCollectStatus(result.COLLECT_STATUS_ALREADY_COLLECT);
			}
		}
		// 将发布人信息添加到view中
		result.setService(service);
		DetailSeekHelpUserView userView = BeanUtil.copy(publisher, DetailSeekHelpUserView.class);
		result.setUser(userView);
		result.setListServiceDescribe(listServiceDesc);
		// TODO 从redis中获取标签id对应的值，如果为null，再从数据库中查询
		// 取出键为allType的json
		String value = getValueByKey();
		// 解析json
		try {
			List<AllTypeJsonEntity> listType = objectMapper.readValue(value,
					new TypeReference<List<AllTypeJsonEntity>>() {
					});
			for (int i = 0; i < listType.size(); i++) {
				AllTypeJsonEntity jsonEntity = listType.get(i);
				if (Long.parseLong(jsonEntity.getId()) == service.getServiceTypeId()) {
					result.setServiceType(jsonEntity.getTitle());
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("解析字典表allType关键字的json出错，" + errInfo(e));
			result.setServiceType("");
		}
		if (user != null) {
			// 求助服务不在列表中显示，则显示下架
			if (service.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_NOT_VISIBILITY.getValue())
					|| service.getStatus().equals(ProductEnum.STATUS_ALREADY_END.getValue())
					|| service.getStatus().equals(ProductEnum.STATUS_EXAMINE_NOPASS.getValue())
					|| service.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue())
					|| service.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue())
					|| service.getStatus().equals(ProductEnum.STATUS_WAIT_EXAMINE.getValue())) {
				result.setShowHelpStatus(result.SHOW_HELP_STATUS_SHOW_LOWERFRAME);
			} else {
				// 当前用户与发布者是同一个人
				if (user.getId() == service.getUserId().longValue()) {
					result.setShowHelpStatus(result.SHOW_HELP_STATUS_NOT_SHOW);
				} else if (service.getType() == SERVICE_TYPE_SEEK_HELP && (isHasEnrollInDict(serviceId, user.getId())
						|| isHasEnrollInReceipt(serviceId, user.getId()))) {
					// 求助显示已帮助状态
					result.setShowHelpStatus(result.SHOW_HELP_STATUS_SHOW_HELPED);
				} else if (service.getType() == SERVICE_TYPE_SERVICE) {
					// 服务显示已报名状态
					if (service.getTimeType() == 0 && (isHasEnrollInDict(serviceId, user.getId())
							|| isHasEnrollInReceipt(serviceId, user.getId()))) {
						result.setShowHelpStatus(result.SHOW_HELP_STATUS_SHOW_HELPED);
					} else if (service.getTimeType() == 1 && isHasEnrollInDict(serviceId, user.getId())) {
						result.setShowHelpStatus(result.SHOW_HELP_STATUS_SHOW_HELPED);
					}
				}
			}

			if (service.getType() == SERVICE_TYPE_SEEK_HELP) {
				if (service.getStatus().equals(ProductEnum.STATUS_ALREADY_END.getValue())) {
					result.setShowHelpStatus(result.SHOW_HELP_STATUS_COMPLETED);
				} else if (service.getStatus().equals(ProductEnum.STATUS_IS_RUNNING.getValue())) {
					result.setShowHelpStatus(result.SHOW_HELP_STATUS_RUNNING);
				} else if (service.getStatus().equals(ProductEnum.STATUS_LOWER_FRAME_NOT_VISIBILITY.getValue())) {
					result.setShowHelpStatus(result.SHOW_HELP_STATUS_SHOW_LOWERFRAME);
				}
			}
		}
		return result;
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
	 * 功能描述:列出服务评价或个人主页评价
	 * 作者:马晓晨
	 * 创建时间:2018年11月8日 下午9:52:25
	 *
	 * @param user
	 * @param serviceId
	 * @param lastTime
	 * @param pageSize
	 */
	@Override
	public QueryResult<PageRemarkView> listRemark(Long userId, Long serviceId, Integer pageNum, Integer pageSize) {
		QueryResult<PageRemarkView> result = new QueryResult<>();
		List<PageRemarkView> listPageView = new ArrayList<>();
		// 查询出所有评价
		TEvaluateExample evaluateExample = new TEvaluateExample();
		TEvaluateExample.Criteria evaluateCriteria = evaluateExample.createCriteria();
		evaluateCriteria.andIsValidEqualTo(IS_VALID_YES);
		if (serviceId != null) { // 服务的评价
			TService tService = serviceDao.selectByPrimaryKey(serviceId);
			// ******查询出该服务的组，评价表中记录的是组id ******
			/*
			 * List<Long> receiptIds = selectReceiptIdsByServiceId(serviceId); if
			 * (receiptIds.size() == 0) { receiptIds.add(0L); }
			 */
			List<Long> parentIds = selectParentIdsByServiceId(serviceId);
			if (parentIds.size() == 0) {
				logger.info("该服务“{}”还没生成订单，也没有评价。服务ID为：{}", tService.getServiceName(), tService.getId());
				result.setResultList(listPageView);
				result.setTotalCount(0L);
				return result;
			}
			evaluateCriteria.andServiceIdIn((parentIds));
			evaluateCriteria.andUserIdEqualTo(tService.getUserId());
//			evaluateCriteria.andServiceIdEqualTo(serviceId);
		} else { // 个人主页的评价
			if (userId == null) {
				logger.error("个人主页评价列表的必填参数  userId 不能为空");
				throw new MessageException("列出个人主页评价的必填参数  userId 不能为空");
			}
			evaluateCriteria.andUserIdEqualTo(userId);
			evaluateCriteria.andCreditEvaluateIsNotNull(); // 只查看作为服务者的评价， 即带分数的评价
		}
		Page<TEvaluate> page = PageHelper.startPage(pageNum, pageSize);
		List<TEvaluate> listEvaluate = evaluateDao.selectByExample(evaluateExample);
		if (listEvaluate.size() == 0) {
			logger.info("没有找到一条评价记录 serviceId 为 {},userId为 {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", serviceId, userId);
			result.setResultList(listPageView);
			result.setTotalCount(0L);
			return result;
		}
		// 获取所有评价人id
		List<Long> userIds = new ArrayList<>();
		for (int i = 0; i < listEvaluate.size(); i++) {
			TEvaluate evaluate = listEvaluate.get(i);
			userIds.add(evaluate.getEvaluateUserId());
//			map.put(evaluate.getEvaluateUserId(), evaluate);
		}
		List<TUser> listUser = getListUser(userIds);
		// 评价人ID 评价人 的键值map
		Map<Long, TUser> map = new HashMap<>();
		for (int i = 0; i < listUser.size(); i++) {
			TUser tUser = listUser.get(i);
			if (!map.containsKey(tUser.getId())) {
				map.put(tUser.getId(), tUser);
			}
		}
		for (int i = 0; i < listEvaluate.size(); i++) {
			PageRemarkView pageView = new PageRemarkView();
			TEvaluate evaluate = listEvaluate.get(i);
			TUser evalUser = map.get(evaluate.getEvaluateUserId());
			BaseUserView userView = BeanUtil.copy(evalUser, BaseUserView.class);
			pageView.setUser(userView);
			pageView.setEvaluate(evaluate);
			// 根据组id获取serviceId
			Long serviceIdByParent = getServiceIdByParent(evaluate.getServiceId());
			TService service = serviceDao.selectByPrimaryKey(serviceIdByParent);
			pageView.setService(service);
			listPageView.add(pageView);
		}
		result.setResultList(listPageView);
		result.setTotalCount(page.getTotal());
		return result;
	}

	/**
	 * 功能描述:搜索服务功能
	 * 作者:马晓晨
	 * 创建时间:2018年11月20日 下午3:28:24
	 *
	 * @param condition
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	/*@Override
	public QueryResult<PageServiceReturnView> searchService(String condition, Integer pageNum, Integer pageSize,
															TUser tUser) {
		// 将条件中的逗号剔除
		condition = condition.replaceAll(",", "");
		QueryResult<PageServiceReturnView> result = new QueryResult<>();
		List<PageServiceReturnView> listReturn = new ArrayList<>();
		// 为了兼容PC端组织发布的权限显示问题，根据当前用户的组织查看当前用户是否有权限访问
		List<Long> companyIds = new ArrayList<>();
		if (tUser != null) { // 如果当前用户是登录状态，则查看当前用户加入了哪些组织
			companyIds = userCompanyDao.selectCompanyIdByUser(tUser.getId());
		}
		if (companyIds.size() == 0) {
			// 如果用户没有登录或者用户没有加入组织，则为了数据库查询的in语句不出错，加入一个1值。但是会出现效率问题
			companyIds.add(1L);
		}
		Page<TService> page = PageHelper.startPage(pageNum, pageSize);
		// 根据条件分页所有服务
		List<TService> listService = serviceDao.searchService(condition, companyIds);
		// 装载所有需要查询的用户id
		List<Long> userIds = new ArrayList<>();
		for (int i = 0; i < listService.size(); i++) {
			userIds.add(listService.get(i).getUserId());
		}
		// 查询出所有发布者
		if (userIds.size() == 0) {
			result.setResultList(listReturn);
			result.setTotalCount(0L);
			return result;
		}
		Map<Long, TUser> usersInfo = getUserInfo(userIds);
		// 遍历列表service，组装返回view
		for (int i = 0; i < listService.size(); i++) {
			PageServiceReturnView returnView = new PageServiceReturnView();
			TService service = listService.get(i);
			returnView.setService(service);
			TUser user = usersInfo.get(service.getUserId());
			// 进行部分字段映射
			PageServiceUserView userView = BeanUtil.copy(user, PageServiceUserView.class);
			returnView.setUser(userView);
			listReturn.add(returnView);
		}
		result.setResultList(listReturn);
		result.setTotalCount(page.getTotal());
		return result;
	}*/

	/**
	 * 功能描述:是否两个求助服务相同
	 * 作者:马晓晨
	 * 创建时间:2018年11月15日 下午12:00:28
	 *
	 * @param service
	 * @param listServiceDescribe
	 * @param myNewService
	 * @param listNewServiceDesc
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
			serviceDescribeDao.batchInsert(listServiceDescribe);
		}
		//派生出第一张订单
		TOrder
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
	 * 功能描述:组织用户发布求助
	 * 作者:马晓晨
	 * 创建时间:2019年1月17日 下午4:47:53
	 *
	 * @param user
	 * @param param
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
	 * 功能描述:在订单表里面是否有报名信息
	 * 作者:马晓晨
	 * 创建时间:2018年11月16日 上午11:15:34
	 *
	 * @param service
	 * @param user
	 * @return
	 */
	private boolean isHasEnrollInReceipt(Long serviceId, Long userId) {
		List<Integer> statusList = new ArrayList<>();
		statusList.add(2);
		statusList.add(7);
		statusList.add(8);
		statusList.add(9);
		statusList.add(10);
		statusList.add(11);
		statusList.add(12);
		// 如果是求助，那么要判断其是否参加过，已经参加就不能报名了
		TServiceReceiptExample serviceReceiptExample = new TServiceReceiptExample();
		TServiceReceiptExample.Criteria condition = serviceReceiptExample.createCriteria();
		condition.andServiceIdEqualTo(serviceId);
		condition.andReceiptUserIdEqualTo(userId);
		condition.andStatusIn(statusList);
		condition.andIsValidEqualTo("1");
		List<TServiceReceipt> serviceReceipts = serviceReceiptDao.selectByExample(serviceReceiptExample);
		return (serviceReceipts != null && serviceReceipts.size() > 0);
	}


	/**
	 * 功能描述:组织发布公益时求助
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午3:38:56
	 *
	 * @param user
	 * @param param
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
		if (service.getId() != null) {
			delService(service);
		}
		service.setId(snowflakeIdWorker.nextId());
		// 待审核 TODO 暂时不做审核限制，直接是待开始
		// service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_CAN_SIGN_UP.getValue());
		service.setUserId(user.getId());
		// 来源为组织，从前端传递，不需要设置
		// service.setSource(ProductEnum.SOURCE_PERSONAL.getValue());// 来源为个人
		service.setType(SERVICE_TYPE_SEEK_HELP); // 类型为求助
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
			desc.setType(SERVICE_TYPE_SEEK_HELP);
			setCommonServcieDescField(user, desc);
		}
		// 查询最新的一条是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		serviceDao.insert(service);
		if (listServiceDescribe != null && listServiceDescribe.size() > 0) {
			serviceDescribeDao.batchInsert(listServiceDescribe);
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
		String serviceName = service.getServiceName();
		if (BadWordUtil.isContaintBadWord(serviceName, 2)) {
			throw new MessageException("求助名称包含敏感词");
		}
		// 如果是上架,先将之前那条记录改为状态8删除
		if (service.getId() != null) {
			delService(service);
		}
		service.setId(snowflakeIdWorker.nextId());
		// 待审核 TODO 暂时不做审核限制，直接是待开始
//			service.setStatus(ProductEnum.STATUS_WAIT_EXAMINE.getValue());
		service.setStatus(ProductEnum.STATUS_CAN_SIGN_UP.getValue());
		service.setUserId(user.getId());
		// 来源为组织，从前端传递，不需要设置
//			service.setSource(ProductEnum.SOURCE_PERSONAL.getValue());// 来源为个人
		service.setType(SERVICE_TYPE_SEEK_HELP); // 类型为求助
		setServiceCommonField(user, service);
		// 组织发布互助时还是公益时由前端传递参数
//			service.setCollectType(1);
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
//			desc.setType(SERVICE_TYPE_SEEK_HELP);
			setCommonServcieDescField(user, desc);
		}
		// 查询最新的一条是否和当前发布的重叠，如果重叠的话就给提示不让发布(抛出异常)
		checkRepeat(user, service, listServiceDescribe);
		serviceDao.insert(service);
		if (listServiceDescribe != null && listServiceDescribe.size() > 0) {
			serviceDescribeDao.batchInsert(listServiceDescribe);
		}
		// 3、扣除用户时间币，生成交易流水
		// 将用户时间币冻结
		user.setFreezeTime(freezeTime + seekHelpPrice);
		user.setUpdateTime(currentTime);
		userDao.updateByPrimaryKeySelective(user);
		// 生成冻结记录
		TUserFreeze userFreeze = new TUserFreeze();
		userFreeze.setId(snowflakeIdWorker.nextId());
		userFreeze.setUserId(user.getId());
		userFreeze.setServiceId(service.getId());
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
	}

	/**
	 * 功能描述:检查是否和最新的一条重复
	 * 作者:马晓晨
	 * 创建时间:2019年1月18日 下午2:40:14
	 *
	 * @param user
	 * @param service
	 * @param listServiceDescribe
	 */
	private void checkRepeat(TUser user, TService service, List<TServiceDescribe> listServiceDescribe) {
		TService myNewService = serviceDao.selectUserNewOneRecord(user.getId(), service.getType());
		if (myNewService != null) {
			List<TServiceDescribe> listNewServiceDesc = serviceDescribeDao.selectDescByServiceId(myNewService.getId());
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
//		Long id = service.getId();
//		TService lowerFrameService = serviceDao.selectByPrimaryKey(id);
//		// 将之前的service状态改为 8 下架不可见（已重新发布的）
//		Integer status = lowerFrameService.getStatus();
//		if (status != ProductEnum.STATUS_LOWER_FRAME_MANUAL.getValue()
//				&& status != ProductEnum.STATUS_LOWER_FRAME_TIME_OUT.getValue()
//				&& status != ProductEnum.STATUS_EXAMINE_NOPASS.getValue()) {
//			throw new MessageException("当前状态不能上架，请重新尝试");
//		}
//		lowerFrameService.setStatus(ProductEnum.STATUS_LOWER_FRAME_NOT_VISIBILITY.getValue());
//		serviceDao.updateByPrimaryKeySelective(lowerFrameService);
	}

}
