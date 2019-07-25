package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.enums.application.UploadPathEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.NumberUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
	private CsqPaymentDao csqUserPaymentDao;
	@Autowired
	private CsqOrderDao csqOrderDao;

	@Override
	public QueryResult<CsqUserPaymentRecordVo> findWaters(Integer pageNum, Integer pageSize, Long userId, Integer option) {
		List<TCsqUserPaymentRecord> records;
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);
		if (option == null) {
			records = csqPaymentDao.selectByUserIdDesc(userId);
		} else {
			records = csqPaymentDao.selectByUserIdAndInOrOutDesc(userId, option);
		}

		List<CsqUserPaymentRecordVo> collect = records.stream()
			.map(a -> {
				a.setDate(DateUtil.timeStamp2Date(a.getCreateTime().getTime(), "MM/dd"));
				return a.copyUserPaymentRecordVo();
			}).collect(Collectors.toList());

		QueryResult<CsqUserPaymentRecordVo> queryResult = new QueryResult<>();
		queryResult.setResultList(collect);
		queryResult.setTotalCount(page.getTotal());
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
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> records;
		if (option == null) {
			records = csqPaymentDao.selectByUserIdDesc(userId);
		} else {
			records = csqPaymentDao.selectByUserIdAndInOrOutDesc(userId, option);
		}

		List<Map<String, Object>> mapList = getMapList(records);

		QueryResult<Map<String, Object>> queryResult = new QueryResult<>();
		queryResult.setResultList(mapList);
		queryResult.setTotalCount(page.getTotal());
		return queryResult;
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
		TCsqUserPaymentRecord theOtherTypeRecord = findTheOtherTypeRecord(record);

		Double accountMoney = csqPaymentDao.countMoney(userId, 1);

		Long entityId = theOtherTypeRecord.getEntityId();
		Integer entityType = theOtherTypeRecord.getEntityType();
		//获取serviceName
		String serviceName = "";
		if (CsqEntityTypeEnum.TYPE_FUND.toCode() == entityType) {
			TCsqFund csqFund = csqFundDao.selectByPrimaryKey(entityId);
			serviceName = csqFund == null ? null : csqFund.getName() + "基金会";
		} else if (CsqEntityTypeEnum.TYPE_SERVICE.toCode() == entityType) {
			TCsqService csqService = csqServiceDao.selectByPrimaryKey(entityId);
			serviceName = csqService == null ? null : csqService.getName() + "项目";
		}

		//获取name
		TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
		map.put("serviceName", serviceName);
		map.put("name", csqUser.getName());
		map.put("money", record.getMoney());
		map.put("countMoney", NumberUtil.keep2Places(accountMoney));
		// TODO: 2019-06-20  二维码生成
		//page
		String page = "/index/fxxk";
		map.put("code", wechatService.genQRCode(String.valueOf(record.getId()), page, UploadPathEnum.innerEnum.CSQ_CERTIFICATE));
		map.put("time", DateUtil.timeStamp2Date(record.getCreateTime().getTime()));
		map.put("date", DateUtil.timeStamp2Date(record.getCreateTime().getTime(), "yyyy-MM-dd"));
		return map;
	}

	private TCsqUserPaymentRecord findTheOtherTypeRecord(TCsqUserPaymentRecord record) {
		Long recordId = record.getId();
		Long orderId = record.getOrderId();
		return csqUserPaymentDao.selectByOrderIdAndNeqId(orderId, recordId);
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

	private Map<String, Object> getBeneficiaryMap(Integer toType, Long toId) {
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
				resultId = csqFund.getId();
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
				resultId = csqService.getId();
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

}
