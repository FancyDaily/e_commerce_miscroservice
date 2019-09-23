package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.NumberUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDataBIVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqLineDiagramData;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description TODO
 * @ClassName CsqPaymentServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-17 15:15
 * @Version 1.0
 */
@Service
public class CsqPaymentServiceImpl implements CsqPaymentService {

	@Autowired
	private CsqKeyValueDao csqKeyValueDao;
	@Autowired
	private CsqPaymentDao csqPaymentDao;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private CsqUserDao csqUserDao;
	@Autowired
	private CsqFundDao csqFundDao;
	@Autowired
	private CsqServiceDao csqServiceDao;
	@Autowired
	private CsqUserPaymentDao csqUserPaymentDao;
	@Autowired
	private CsqOrderDao csqOrderDao;
	@Autowired
	private CsqUserService csqUserService;
	@Value("${page.person}")
	private String PERSON_PAGE;

	@Override
	public QueryResult<CsqUserPaymentRecordVo> findWaters(Integer pageNum, Integer pageSize, Long userId, Integer option) {
		List<TCsqUserPaymentRecord> records = getWaters(pageNum, pageSize, userId, option);
		long total = IdUtil.getTotal();

		List<CsqUserPaymentRecordVo> collect = records.stream()
			.map(a -> {
				dealWithPaymentRecords(a);
				return a.copyUserPaymentRecordVo();
			}).collect(Collectors.toList());

		return PageUtil.buildQueryResult(collect, total);
	}

	private void dealWithPaymentRecords(TCsqUserPaymentRecord a) {
		a.setDate(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "MM/dd"));
		String description = a.getDescription();
		if (!StringUtil.isEmpty(description)) {
			if (description.startsWith("向")) {
				a.setDescription(description + "捐款");
			} else {
				a.setDescription("捐赠到爱心账户");    //TODO 替换描述 -> 捐赠到爱心账户
			}
		}
	}

	@Override
	public Object findWatersAndTotal(Integer pageNum, Integer pageSize, Long userId, Integer option, boolean isGroupingByYears) {
		if (isGroupingByYears) {
			return findWatersGroupingByYear(pageNum, pageSize, userId, option);
		}
		return findWaters(pageNum, pageSize, userId, option);
	}

	@Override
	public QueryResult<Map<String, Object>> findWatersGroupingByYear(Integer pageNum, Integer pageSize, Long userId, Integer option) {
//		Page<Object> page = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> records = getWaters(pageNum, pageSize, userId, option);
		long total = IdUtil.getTotal();

		List<Map<String, Object>> mapList = getMapList(records);

		QueryResult<Map<String, Object>> queryResult = new QueryResult<>();
		queryResult.setResultList(mapList);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	private List<TCsqUserPaymentRecord> getWaters(Integer pageNum, Integer pageSize, Long userId, Integer option) {
		List<TCsqUserPaymentRecord> records;
		if (option == null) {
//			records = csqPaymentDao.selectByUserIdDescPage(userId, pageNum, pageSize);
			records = csqPaymentDao.selectByUserIdAndNeqEntityTypeDescPage(userId, pageNum, pageSize, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode());
		} else {
			records = csqPaymentDao.selectByUserIdAndInOrOutAndNeqEntityDescPage(pageNum, pageSize, userId, option, CsqEntityTypeEnum.TYPE_FUND.toCode(), CsqEntityTypeEnum.TYPE_SERVICE.toCode());
		}
		return records;
	}

	private List<Map<String, Object>> getMapList(List<TCsqUserPaymentRecord> records) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String, List<CsqUserPaymentRecordVo>> currentMap = new HashMap<>();

		records.stream()
			.forEach(a -> {
				String year = DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "yyyy");
				List<CsqUserPaymentRecordVo> userPaymentRecords = currentMap.get(year);
				if (userPaymentRecords == null) {
					userPaymentRecords = new ArrayList<>();
				}
				dealWithPaymentRecords(a);
				userPaymentRecords.add(a.copyUserPaymentRecordVo());
				currentMap.put(year, userPaymentRecords);
			});

		currentMap.forEach((key, value) -> {
			Map<String, Object> yearMap = new HashMap<>();
			yearMap.put("year", key);
			yearMap.put("payments", value);
			mapList.add(yearMap);    //向mapList放入一个含有年份信息的map
		});
		return mapList;
	}

	private Map<String, Object> getTotalInOutNum(Long userId) {
		Map<String, Object> map = new HashMap<>();
		//所有收支记录
		List<TCsqUserPaymentRecord> userPaymentRecords = csqPaymentDao.selectByUserId(userId);
		Double in = userPaymentRecords.stream()
			.filter(a -> CsqUserPaymentEnum.INOUT_IN.toCode() == a.getInOrOut())
			.map(TCsqUserPaymentRecord::getMoney)
			.reduce(0d, Double::sum);
		Double out = userPaymentRecords.stream()
			.filter(a -> CsqUserPaymentEnum.INOUT_OUT.toCode() == a.getInOrOut())
			.map(TCsqUserPaymentRecord::getMoney)
			.reduce(0d, Double::sum);
		map.put("in", in);
		map.put("out", out);
		return map;
	}

	@Override
	public Map<String, Object> findMyCertificate(String orderNo, Long recordId, Long userId) {
		Map<String, Object> map = new HashMap<>();
		//获得orderId
		Long orderId = null;
		if (!StringUtil.isEmpty(orderNo)) {
			TCsqOrder tCsqOrder = csqOrderDao.selectByOrderNo(orderNo);
			if (tCsqOrder == null) {
				throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单号有误！");
			}
			orderId = tCsqOrder.getId();
		}
		TCsqUserPaymentRecord record = orderId == null ? csqPaymentDao.findWaterById(recordId) : csqPaymentDao.selectByOrderNoAndUserIdAndInOut(orderId, userId, CsqUserPaymentEnum.INOUT_OUT.toCode());

		if (record == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该记录已不存在!");
		}
//		TCsqUserPaymentRecord theOtherTypeRecord = findTheOtherTypeRecord(record);

//		Double accountMoney = csqPaymentDao.countMoney(userId, 1);
		Double accountMoney = csqPaymentDao.countMoney(userId, 1, record.getCreateTime().getTime());
		//获取serviceName
		String serviceName = "";

		orderId = orderId == null ? record.getOrderId() : orderId;
		boolean isSpecial = false;
		if (orderId == null) { //表明是平台插入
			serviceName = record.getDescription();
			isSpecial = true;
		}

		TCsqOrder tCsqOrder = csqOrderDao.selectByPrimaryKey(orderId);
		Long toId = tCsqOrder.getToId();
		Integer toType = tCsqOrder.getToType();
		if (CsqEntityTypeEnum.TYPE_FUND.toCode() == toType) {
			TCsqFund tCsqFund = csqFundDao.selectByPrimaryKey(toId);
			serviceName = tCsqFund.getName();
		} else if (CsqEntityTypeEnum.TYPE_SERVICE.toCode() == toType) {
			TCsqService csqService = csqServiceDao.selectByPrimaryKey(toId);
			serviceName = csqService.getName();
		}
		/*Long entityId = theOtherTypeRecord.getEntityId();
		Integer entityType = theOtherTypeRecord.getEntityType();
		String serviceName = "";
		if (CsqEntityTypeEnum.TYPE_FUND.toCode() == entityType) {
			TCsqFund csqFund = csqFundDao.selectByPrimaryKey(entityId);
			serviceName = csqFund == null ? null : csqFund.getName() + "基金会";
		} else if (CsqEntityTypeEnum.TYPE_SERVICE.toCode() == entityType) {
			TCsqService csqService = csqServiceDao.selectByPrimaryKey(entityId);
			serviceName = csqService == null ? null : csqService.getName() + "项目";
		}*/

		//获取name
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		Double money = record.getMoney();
		map.put("serviceName", serviceName);
		map.put("name", csqUser.getName());
		map.put("money", money);
		accountMoney = NumberUtil.keep2Places(accountMoney);
		map.put("countMoney", accountMoney);
		/*//page
		String page = PERSON_PAGE;
		TCsqKeyValue build = TCsqKeyValue.builder()
			.type(CsqKeyValueEnum.TYPE_SCENE.getCode())
			.mainKey(userId)
			.theValue(String.valueOf(record.getId()))
			.build();
		csqKeyValueDao.save(build);
		String sceneKey = build.getId().toString();
		String qrCode = wechatService.genQRCode(sceneKey, page, UploadPathEnum.innerEnum.CSQ_CERTIFICATE);*/
		Map<String, Object> shareMap = csqUserService.share(userId, userId, 0);
		String qrCode = (String) shareMap.get("qrCode");

//		qrCode = "https://timebank-test-img.oss-cn-hangzhou.aliyuncs.com/person/QR0201905161712443084870123470880.jpg";	// 写死的二维码地址
		map.put("code", qrCode);
		map.put("time", DateUtil.timeStamp2Date(record.getCreateTime().getTime()));
		String date = DateUtil.timeStamp2Date(record.getCreateTime().getTime(), "yyyy-MM-dd HH:mm");
		List<String> list1 = Arrays.asList(date.split(" "));
		String ymd = list1.get(0);
		List<String> ymdList = Arrays.asList(ymd.split("-"));
		String year = ymdList.get(0);
		String month = ymdList.get(1);
		String day = ymdList.get(2);
		String hm = list1.get(1);
		List<String> hmList = Arrays.asList(hm.split(":"));
		String hour = hmList.get(0);
		String minute = hmList.get(1);
		map.put("date", date);
		/*String description = "感谢您为" + serviceName + "捐赠" + money + "元，截止" + year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分，您在浙江省爱心事业基金会累计捐款" + accountMoney + "元，扫描下面的二维码可以及时获取我们的项目执行反馈情况。\n" +
			"特发此证，以资感谢！";*/
		String description = "感谢您对公益事业的支持!\n"
			+ "您捐赠的" + money + "元，我们将遵照您的意愿，\n"
			+ "用于" + serviceName + "。\n"
			+ "截止当前，您已累计通过丛善桥捐赠" + accountMoney + "元。\n"
			+ "\n"
			+ "谨以此证向您表示最真诚的的感谢！";
		if (isSpecial) {
			/*description = "感谢您此次捐赠" + money + "元，截止" + year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分，您在浙江省爱心事业基金会累计捐款" + accountMoney + "元，扫描下面的二维码可以及时获取我们的项目执行反馈情况。\n" +
				"特发此证，以资感谢！";*/
			description = "感谢您对公益事业的支持!\n"
				+ "您捐赠的" + money + "元，我们将遵照您的意愿，\n"
				+ "用于" + "公益事业" + "。\n"
				+ "截止当前，您已累计通过丛善桥捐赠" + accountMoney + "元。\n"
				+ "\n"
				+ "谨以此证向您表示最真诚的的感谢！";
		}
		String dateDesc = year + "年" + month + "月" + day + "日";
		map.put("date", dateDesc);
		map.put("description", description);
		return map;
	}

	private TCsqUserPaymentRecord findTheOtherTypeRecord(TCsqUserPaymentRecord record) {
		Long recordId = record.getId();
		Long orderId = record.getOrderId();
		return csqPaymentDao.selectByOrderIdAndNeqId(orderId, recordId);
	}

	@Override
	public Double countMoney(Long userId, Integer inOut) {
		return csqPaymentDao.countMoney(userId, inOut);
	}

	@Override
	public void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId) {
		savePaymentRecord(userId, fromType, fromId, toType, toId, amount, orderId, null);
	}

	@Override
	public void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId, String description) {
		savePaymentRecord(userId, fromType, fromId, toType, toId, amount, orderId, description, null);
	}

	@Override
	public void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId, String description, Timestamp timestamp) {
		boolean isFromHuman = CsqEntityTypeEnum.TYPE_HUMAN.toCode() == fromType;
		String demoIncomeDesc = "充值";
		TCsqUserPaymentRecord build3 = null;

		if (isFromHuman) {    //如果是现金支付，涉及第三条流水
			build3 = TCsqUserPaymentRecord.builder()
				.userId(userId)
				.orderId(orderId)
				.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())    //收入
				.description(demoIncomeDesc)
				.entityId(userId)
				.entityType(CsqEntityTypeEnum.TYPE_ACCOUNT.toCode())    //现金充值类型
				.money(amount)
				.orderId(orderId).build();
			build3.setCreateTime(timestamp);
			if (CsqEntityTypeEnum.TYPE_ACCOUNT.toCode() == toType) {    //仅向爱心账户充值
				csqUserPaymentDao.insert(build3);
				return;
			}
		}
		//获取受益人编号
		Map<String, Object> beneficiaryMap = getBeneficiaryMap(toType, toId);
		Long beneficiaryId = (Long) beneficiaryMap.get("beneficiaryId");
		String beneficiaryName = (String) beneficiaryMap.get("beneficiaryName");
		StringBuilder builder = new StringBuilder();
		if (description == null) {
			description = builder.append("向").append(beneficiaryName)
//				.append("捐款")
				.toString();
		}
		TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
			.userId(userId)
			.orderId(orderId)
			.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode())    //支出
			.description(description)
			.entityId(fromId)
			.entityType(fromType)
			.money(amount)
			.orderId(orderId).build();
		build1.setCreateTime(timestamp);

		TCsqUserPaymentRecord build2 = TCsqUserPaymentRecord.builder()
			.userId(beneficiaryId)
			.orderId(orderId)
			.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())
			.description(demoIncomeDesc)
			.entityId(toId)
			.entityType(toType)
			.money(amount)
			.orderId(orderId).build();
		build2.setCreateTime(timestamp);
		csqUserPaymentDao.multiInsert(build3 == null ? Arrays.asList(build1, build2) : Arrays.asList(build1, build2, build3));
	}

	@Override
	public void savePaymentRecord(TCsqOrder tCsqOrder) {
		savePaymentRecord(tCsqOrder.getUserId(), tCsqOrder.getFromType(), tCsqOrder.getFromId(), tCsqOrder.getToType(), tCsqOrder.getToId(), tCsqOrder.getPrice(), tCsqOrder.getId(), null, tCsqOrder.getCreateTime());
	}

	@Override
	public Map<String, Object> getBeneficiaryMap(Integer toType, Long toId) {
		Map<String, Object> resultMap = new HashMap<>();
		Long resultId = null;
		String resultName = "";
		StringBuilder builder = new StringBuilder();
		switch (CsqEntityTypeEnum.getEnum(toType)) {    //获取到相应枚举类型
			case TYPE_ACCOUNT:
				TCsqUser csqUser = csqUserDao.selectByPrimaryKey(toId);
				resultId = csqUser.getId();
				builder.append("爱心账户");
				break;
			case TYPE_FUND:
				TCsqFund csqFund = csqFundDao.selectByPrimaryKey(toId);
				resultId = csqFund.getUserId();
				if (StringUtil.isEmpty(csqFund.getName())) {
					builder.append("我的");
				} else {
					builder.append("\"");
					builder.append(csqFund.getName());
					builder.append("\"");
				}
				builder.append("基金");
				break;
			case TYPE_SERVICE:
				TCsqService csqService = csqServiceDao.selectByPrimaryKey(toId);
				resultId = csqService.getUserId();
				builder.append("\"");
				builder.append(csqService.getName());
				builder.append("\"");
				builder.append("项目");
				break;
		}
		resultName = builder.toString();
		resultMap.put("beneficiaryId", resultId);
		resultMap.put("beneficiaryName", resultName);
		return resultMap;
	}

	@Override
	public List<Long> getPaymentRelatedOrderIds(Long entityId) {
		return getPaymentRelatedOrderIds(entityId, CsqEntityTypeEnum.TYPE_SERVICE.toCode());
	}

	@Override
	public List<Long> getPaymentRelatedOrderIds(Long entityId, Integer entityType) {
		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectByEntityIdAndEntityTypeAndInOutDesc(entityId, entityType, CsqUserPaymentEnum.INOUT_IN.toCode());    //TODO 分页
		return tCsqUserPaymentRecords.stream()
			.map(TCsqUserPaymentRecord::getOrderId)
			.distinct().collect(Collectors.toList());
	}

	@Override
	public List<CsqBasicUserVo> getTopDonaters(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords, List<Long> orderIds) {
		//组建user-order的map
		List<TCsqOrder> tCsqOrders = orderIds.isEmpty() ? new ArrayList<>() : csqOrderDao.selectInIds(orderIds);
		Map<Long, List<TCsqOrder>> userOrderMap = tCsqOrders.stream()
			.collect(Collectors.groupingBy(TCsqOrder::getUserId));

		//统计捐款数，获取top3
		Map<Long, List<TCsqUserPaymentRecord>> unsortedMap = tCsqUserPaymentRecords.stream()
			.collect(Collectors.groupingBy(TCsqUserPaymentRecord::getUserId));
		Map<Long, Double> donaterMoneyMap = new HashMap<>();
		List<Long> donaterIds = new ArrayList<>();
		List<TCsqUser> donaters;
		Set<Map.Entry<Long, List<TCsqUserPaymentRecord>>> entries = unsortedMap.entrySet();
		Stream<Map.Entry<Long, List<TCsqUserPaymentRecord>>> stream = entries.stream();
		stream = stream.sorted(Collections.reverseOrder(Comparator.comparing(a -> {    //排序(若元素数量为1会跳过sort函数
			List<TCsqUserPaymentRecord> value = a.getValue();
			Double totalDonate = value.stream()
				//TODO 若匿名捐赠不参与排行top3统计则加入filter筛选 -> 仅筛选非匿名(需要提前构造好payment-order map)
				.map(TCsqUserPaymentRecord::getMoney)
				.reduce(0d, Double::sum);
			donaterMoneyMap.put(a.getKey(), NumberUtil.keep2Places(totalDonate));
			return totalDonate;
		})));
		if (entries.size() == 1) {    //若为一个元素，则收集数据
			unsortedMap.forEach((k, v) -> donaterMoneyMap.put(k, NumberUtil.keep2Places(v.stream()
				.map(TCsqUserPaymentRecord::getMoney)
				.reduce(0d, Double::sum))));
		}
		stream.forEachOrdered(a -> {
				donaterIds.add(a.getKey());    //排序后的集合
			}
		);

		donaters = donaterIds.isEmpty() ? new ArrayList<>() : csqUserDao.selectInIds(donaterIds);
		//纠正排序
		Map<Long, TCsqUser> donaterMap = new HashMap<>();
		donaters.stream()
			.forEach(donater -> donaterMap.put(donater.getId(), donater));
		donaters = donaterIds.stream()        //通过顺序的ids获取到donaters(按捐款数额排序)
			.map(a -> donaterMap.get(a)).collect(Collectors.toList());

		donaters = donaters.stream()
			.map(a -> {
				Double totalDonate = donaterMoneyMap.get(a.getId());
				if (totalDonate != null) {
					a.setTotalDonate(totalDonate);
				}
				return a;
			}).collect(Collectors.toList());
		//去敏感化
		List<CsqBasicUserVo> donaterList = donaters.stream()
			.map(a -> {
				CsqBasicUserVo csqBasicUserVo = a.copyCsqBasicUserVo();
				return csqBasicUserVo;
			}).collect(Collectors.toList());
		//限制为前3条
		donaterList = donaterList.stream()
			.limit(3).collect(Collectors.toList());

		//判断是否匿名
		donaterList = donaterList.stream()
			.map(a -> {
				Long id = a.getId();
				List<TCsqOrder> orders = userOrderMap.get(id);
				if (orders != null
					&& orders.stream()
					.allMatch(b -> CsqOrderEnum.IS_ANONYMOUS_TRUE.getCode().equals(b.getIsAnonymous()))) {    //判定为匿名者

					a.setName(CsqUserEnum.DEFAULT_ANONYMOUS_NAME);
					a.setUserHeadPortraitPath(CsqUserEnum.DEFAULT_ANONYMOUS_HEADPORTRAITUREPATH);
				}
				return a;
			}).collect(Collectors.toList());
		return donaterList;
	}

	@Override
	public Map<String, Object> findWatersAndTotal(String searchParma, Integer pageNum, Integer pageSize, Boolean isFuzzySearch) {
		Page page = PageUtil.prePage(pageNum, pageSize);
		pageNum = page.getPageNum();
		pageSize = page.getPageSize();
		boolean isSearch = !StringUtil.isEmpty(searchParma);
		List<TCsqUser> csqUsers = new ArrayList<>();
		if (isSearch) {
			csqUsers = csqUserDao.selectByName(searchParma, isFuzzySearch);
		}
		List<Long> userIds = csqUsers.stream()
			.map(a -> a.getId()).collect(Collectors.toList());

		MybatisPlusBuild baseBuild = csqUserPaymentDao.baseBuild();
		baseBuild
			.eq(TCsqUserPaymentRecord::getEntityType, CsqEntityTypeEnum.TYPE_ACCOUNT.toCode())    //账户充值
			.eq(TCsqUserPaymentRecord::getInOrOut, CsqUserPaymentEnum.INOUT_IN.toCode())    //收入
		;

		baseBuild =
			isSearch ?
				userIds.isEmpty()? baseBuild: baseBuild
				.in(TCsqUserPaymentRecord::getUserId, userIds)    //搜索参数得到的用户编号
				: baseBuild;

		List<TCsqUserPaymentRecord> userPaymentRecordList = csqUserPaymentDao.selectWithBuildPage(baseBuild, pageNum, pageSize);
		//统计
		Double totalAmount = userPaymentRecordList.stream()
			.map(TCsqUserPaymentRecord::getMoney)
			.reduce(0d, Double::sum);
		//结果集
		List<CsqUserPaymentRecordVo> vos = userPaymentRecordList.stream()
			.map(a -> a.copyUserPaymentRecordVo()).collect(Collectors.toList());

		long total = IdUtil.getTotal();

		QueryResult queryResult = PageUtil.buildQueryResult(vos, total);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("queryResult", queryResult);
		resultMap.put("totalAmount", totalAmount);

		return resultMap;
	}

	@Override
	public HashMap<String, Object> donateRecordList(Long userIds, String searchParam, Page page, boolean isFuzzySearch) {
		page = PageUtil.prePage(page);

		MybatisPlusBuild baseBuild = csqUserPaymentDao.baseBuild();

		//构建目标
		baseBuild = baseBuild
			.in(TCsqUserPaymentRecord::getEntityType, Arrays.asList(CsqEntityTypeEnum.TYPE_SERVICE.toCode(), CsqEntityTypeEnum.TYPE_FUND.toCode()))
			.eq(TCsqUserPaymentRecord::getInOrOut, CsqUserPaymentEnum.INOUT_IN.toCode());
		List<TCsqUserPaymentRecord> orderIdHoder = csqUserPaymentDao.selectWithBuild(baseBuild);
		List<Long> orderIds = orderIdHoder.stream()
			.map(TCsqUserPaymentRecord::getOrderId)
			.distinct().collect(Collectors.toList());
		//统计捐款比数、捐赠总金额
		Double donateTotalAmount = orderIdHoder.stream()
			.map(TCsqUserPaymentRecord::getMoney).reduce(0d, Double::sum);
		int donateCnt = orderIdHoder.size();

		//初始化
		baseBuild = csqUserPaymentDao.baseBuild();

		//主要构建
		baseBuild
			.in(TCsqUserPaymentRecord::getOrderId, orderIds)
			.eq(TCsqUserPaymentRecord::getInOrOut, CsqUserPaymentEnum.INOUT_IN.toCode());

		//用户昵称到用户编号
		List<TCsqUser> csqUsers = csqUserDao.selectByName(searchParam, isFuzzySearch);
		List<Long> csqUserIds = csqUsers.stream()
			.map(TCsqUser::getId).collect(Collectors.toList());

		baseBuild = csqUserIds.isEmpty()? baseBuild:baseBuild.in(TCsqUserPaymentRecord::getUserId, csqUserIds);

		List<TCsqUserPaymentRecord> tCsqUserPaymentRecords = csqUserPaymentDao.selectWithBuildPage(baseBuild, page.getPageNum(), page.getPageSize());
		List<CsqUserPaymentRecordVo> vos = tCsqUserPaymentRecords.stream()
			.map(a -> a.copyUserPaymentRecordVo()).collect(Collectors.toList());

		//结果集
		QueryResult result = PageUtil.buildQueryResult(vos, IdUtil.getTotal());

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("donateCnt", donateCnt);
		resultMap.put("donateTotalAmount", donateTotalAmount);
		resultMap.put("queryResult", result);

		return resultMap;
	}

	@Override
	public QueryResult platformDataStatistics(Long userIds, String searchParam, String startDate, String endDate, Integer pageNum, Integer pageSize, Boolean isFuzzySearch, Boolean isServiceOnly) {
		List<CsqDataBIVo> csqDataBIVos = new ArrayList<>();
		//基本构建
		MybatisPlusBuild baseBuild = csqPaymentDao.baseBuild();
		long total =0L;

		List<TCsqUserPaymentRecord> unHandledList = new ArrayList<>();
		//处理通用信息
		//处理日期
		if(StringUtil.isEmpty(startDate)) {
			Long startTime = Long.valueOf(DateUtil.dateToStamp(startDate));
			baseBuild.gte(TCsqUserPaymentRecord::getCreateTime, new Timestamp(startTime).toString());
		}

		if(StringUtil.isEmpty((endDate))) {
			Long endTime = Long.valueOf(DateUtil.dateToStamp(endDate));
			baseBuild.lte(TCsqUserPaymentRecord::getCreateTime, new Timestamp(endTime).toString());
		}

		if(isServiceOnly) {
			List<TCsqService> csqServices = new ArrayList<>();

			if(!StringUtil.isEmpty(searchParam)) {
				String pattern = "^[1-9]\\d*$";
				boolean isNum = Pattern.matches(pattern, searchParam);
				if(isNum) {	//按编号查找
					TCsqService csqService = csqServiceDao.selectByPrimaryKey(Long.valueOf(searchParam));
					csqServices.add(csqService);
				} else { //按名字查找
					csqServices = csqServiceDao.selectByNamePage(searchParam, isFuzzySearch, pageNum, pageSize);
				}
			} else {
				csqServices = csqServiceDao.selectAllPage(pageNum, pageSize);
			}
			total = IdUtil.getTotal();

			List<Long> fundIds = csqServices.stream()
				.filter(a -> CsqEntityTypeEnum.TYPE_FUND.toCode() == a.getType())
				.map(TCsqService::getFundId).collect(Collectors.toList());
			List<Long> serviceIds = csqServices.stream()
				.filter(a -> CsqEntityTypeEnum.TYPE_SERVICE.toCode() == a.getType())
				.map(TCsqService::getId).collect(Collectors.toList());

			boolean fundIdsEmpty = fundIds.isEmpty();
			boolean serviceIdsEmpty = serviceIds.isEmpty();
			if(!fundIdsEmpty || !serviceIdsEmpty) {
				if(!fundIdsEmpty) {
					baseBuild = baseBuild.eq(TCsqUserPaymentRecord::getEntityType, CsqEntityTypeEnum.TYPE_FUND.toCode())
									.in(TCsqUserPaymentRecord::getEntityId, fundIds);
					if(!serviceIdsEmpty) {
						baseBuild.or();
					}
				}
				baseBuild = !serviceIdsEmpty?
					baseBuild.eq(TCsqUserPaymentRecord::getEntityType, CsqEntityTypeEnum.TYPE_SERVICE.toCode())
								.in(TCsqUserPaymentRecord::getEntityId, serviceIds) :baseBuild;
			}

			unHandledList = csqUserPaymentDao.selectWithBuild(baseBuild);

			//把查找的数据进行组装
			//如果是serviceOnly，需要根据entity_id分组, 两者的结果实体类型可能相同、可能不同

			//数据构建
			Map<Long, List<TCsqUserPaymentRecord>> unhandledMap = unHandledList.stream()
				.collect(Collectors.groupingBy(TCsqUserPaymentRecord::getEntityId));

			unhandledMap.forEach((k,v) -> {
				Integer entityType = v.get(0).getEntityType();
				HashMap<String, List<CsqLineDiagramData>> diagramMap = getDiagramMap(v);

				CsqDataBIVo build = CsqDataBIVo.builder()
					.entityId(k)
					.entityType(entityType)
					.name(null)    //名字后续处理
					.diagramMap(diagramMap).build();
				csqDataBIVos.add(build);

				//处理这些项目或者是基金的名字,甚至给予没有什么用的编号
			});
		} else {	//平台总数据
			//查询提现的数据，加入到基金的筛选条件
			List<TCsqOrder> csqOrderList = csqOrderDao.selectByToType(CsqEntityTypeEnum.TYPE_HUMAN.toCode());
			List<Long> outOrderIds = csqOrderList.stream()
				.map(TCsqOrder::getId).collect(Collectors.toList());

			//收入(即微信充值对应的order_id）
			List<TCsqOrder> csqOrders = csqOrderDao.selectByFromTypeAndStatus(CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
			List<Long> inOrderIds = csqOrders.stream()
				.map(TCsqOrder::getToId).collect(Collectors.toList());

			//支出
			baseBuild = baseBuild
				.eq(TCsqUserPaymentRecord::getInOrOut, CsqUserPaymentEnum.INOUT_OUT.toCode())
				.and()
				.groupBefore()
				.eq(TCsqUserPaymentRecord::getEntityType, CsqEntityTypeEnum.TYPE_SERVICE.toCode())
				.or()
				.eq(TCsqUserPaymentRecord::getEntityType, CsqEntityTypeEnum.TYPE_FUND.toCode())
				.and()
				.groupBefore()
				.isNull(TCsqUserPaymentRecord::getOrderId)
				.or()
				.in(TCsqUserPaymentRecord::getOrderId, outOrderIds)
				.groupAfter()
				.groupAfter();

			//总
			baseBuild
				.or()
				.in(TCsqUserPaymentRecord::getOrderId, inOrderIds);

			unHandledList = csqUserPaymentDao.selectWithBuild(baseBuild);
			//把查找的数据进行组装
			HashMap<String, List<CsqLineDiagramData>> diagramMap = getDiagramMap(unHandledList);

			CsqDataBIVo build = CsqDataBIVo.builder()
				.entityId(null)
				.entityType(null)
				.name(null)
				.diagramMap(diagramMap).build();
			csqDataBIVos.add(build);
		}

		return PageUtil.buildQueryResult(csqDataBIVos, total);
	}

	@Override
	public Double getPlatFromInCome() {
		List<TCsqOrder> tCsqOrders = csqOrderDao.selectByFromTypeAndStatus(CsqEntityTypeEnum.TYPE_HUMAN.toCode(), CsqOrderEnum.STATUS_ALREADY_PAY.getCode());
		return tCsqOrders.stream()
			.map(TCsqOrder::getPrice).reduce(0d, Double::sum);
	}

	private HashMap<String, List<CsqLineDiagramData>> getDiagramMap(List<TCsqUserPaymentRecord> v) {
		List<TCsqUserPaymentRecord> inList = v.stream()
			.filter(a -> CsqUserPaymentEnum.INOUT_IN.toCode() == a.getInOrOut()).collect(Collectors.toList());
		List<TCsqUserPaymentRecord> outList = v.stream()
			.filter(a -> CsqUserPaymentEnum.INOUT_OUT.toCode() == a.getInOrOut()).collect(Collectors.toList());
		//构建折线图
		//1.根据划定的时间段，确定x轴(即日期)有几个时间点
		//将createTime去掉Hms部分，然后按日期分组。
		Map<String, List<TCsqUserPaymentRecord>> inDateRecordMap = getDateRecordMap(inList);
		Map<String, List<TCsqUserPaymentRecord>> outDateRecordMap = getDateRecordMap(outList);

		//TODO 这里有一点，当日没有发生交易的日期在这里是不存在的，也就不会返给前端，需要作出说明
		List<CsqLineDiagramData> inDiagramDataList = getDiagramDataList(inDateRecordMap);
		List<CsqLineDiagramData> outDiagramDataList = getDiagramDataList(outDateRecordMap);
		HashMap<String, List<CsqLineDiagramData>> diagramMap = new HashMap<>();
		diagramMap.put("in", inDiagramDataList);
		diagramMap.put("out", outDiagramDataList);
		return diagramMap;
	}

	private ArrayList<CsqLineDiagramData> getDiagramDataList(Map<String, List<TCsqUserPaymentRecord>> inDateRecordMap) {
		ArrayList<CsqLineDiagramData> inDiagramDataList = new ArrayList<>();

		inDateRecordMap.forEach((date, dataList) -> {
			Double amount = dataList.stream()
				.map(TCsqUserPaymentRecord::getMoney).reduce(0d, Double::sum);
			CsqLineDiagramData lineDiagramData = CsqLineDiagramData.builder()
				.date(date)
				.amount(amount).build();
			inDiagramDataList.add(lineDiagramData);
		});
		return inDiagramDataList;
	}

	private Map<String, List<TCsqUserPaymentRecord>> getDateRecordMap(List<TCsqUserPaymentRecord> inList) {
		return inList.stream()
			.map(a -> {
				Timestamp createTime = a.getCreateTime();
				String date = Arrays.asList(createTime.toString().split(" ")).get(0);
				a.setDate(date);
				return a;
			}).collect(Collectors.groupingBy(TCsqUserPaymentRecord::getDate));
	}

	public static void main(String[] args) {
		/*for(;;) {
			System.out.println("Are you coughing?");
			Scanner scanner = new Scanner(System.in);
			scanner.next();
			if() {

			}
		}*/
	}

}
