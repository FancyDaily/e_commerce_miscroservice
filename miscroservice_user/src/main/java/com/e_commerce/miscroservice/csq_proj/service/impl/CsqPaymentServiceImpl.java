package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.NumberUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
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
				a.setDate(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "MM/dd"));
				return a.copyUserPaymentRecordVo();
			}).collect(Collectors.toList());

		QueryResult<CsqUserPaymentRecordVo> queryResult = new QueryResult<>();
		queryResult.setResultList(collect);
		queryResult.setTotalCount(total);
		return queryResult;
	}

	@Override
	public Object findWaters(Integer pageNum, Integer pageSize, Long userId, Integer option, boolean isGroupingByYears) {
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
				a.setDate(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "MM/dd"));
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

		Double accountMoney = csqPaymentDao.countMoney(userId, 1);

		//获取serviceName
		String serviceName = "";

		orderId = orderId == null? record.getOrderId(): orderId;
		boolean isSpecial = false;
		if(orderId == null) { //表明是平台插入
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
		if(isSpecial) {
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
				.entityType(CsqEntityTypeEnum.TYPE_HUMAN.toCode())    //现金充值类型
				.money(amount)
				.orderId(orderId).build();
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

		TCsqUserPaymentRecord build2 = TCsqUserPaymentRecord.builder()
			.userId(beneficiaryId)
			.orderId(orderId)
			.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())
			.description(demoIncomeDesc)
			.entityId(toId)
			.entityType(toType)
			.money(amount)
			.orderId(orderId).build();
		csqUserPaymentDao.multiInsert(build3 == null ? Arrays.asList(build1, build2) : Arrays.asList(build1, build2, build3));
	}

	@Override
	public void savePaymentRecord(TCsqOrder tCsqOrder) {
		savePaymentRecord(tCsqOrder.getUserId(), tCsqOrder.getFromType(), tCsqOrder.getFromId(), tCsqOrder.getToType(), tCsqOrder.getToId(), tCsqOrder.getPrice(), tCsqOrder.getId());
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
			unsortedMap.forEach((k,v) -> donaterMoneyMap.put(k, NumberUtil.keep2Places(v.stream()
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

}
