package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.enums.application.UploadPathEnum;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @ClassName CsqPaymentServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-17 15:15
 * @Version 1.0
 */
@Transactional(rollbackFor = Throwable.class)
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
		if(option == null) {
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
		if(isGroupingByYears) {
			return findWatersGroupingByYear(pageNum, pageSize, userId, option);
		}
		return findWaters(pageNum, pageSize, userId, option);
	}

	@Override
	public QueryResult<Map<String, Object>> findWatersGroupingByYear(Integer pageNum, Integer pageSize, Long userId, Integer option) {
		Page<Object> page = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> records;
		if(option == null) {
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
	public Map<String, Object> findMyCertificate(Long recordId, Long userId) {
		Map<String, Object> map = new HashMap<>();
		TCsqUserPaymentRecord record = csqPaymentDao.findWaterById(recordId);
		Double accountMoney = csqPaymentDao.countMoney(userId, 1);
		if (record != null) {
			Long entityId = record.getEntityId();
			Integer entityType = record.getEntityType();
			//获取serviceName
			TCsqService csqService = null;
			if (CsqEntityTypeEnum.TYPE_FUND.toCode() == entityType) {
				csqService = csqServiceDao.selectByFundId(entityId);

			} else if (CsqEntityTypeEnum.TYPE_SERVICE.toCode() == entityType) {
				csqService = csqServiceDao.selectByPrimaryKey(entityId);
			}
			String serviceName = csqService==null? null:csqService.getName();

			//获取name
			TCsqUser csqUser = csqUserDao.selectByPrimaryKey(userId);
			map.put("serviceName", serviceName);
			map.put("name", csqUser.getName());
			map.put("money", record.getMoney());
			map.put("countMoney", accountMoney);
			// TODO: 2019-06-20  二维码生成
			//page
			String page = "/index/fxxk";
			map.put("code", wechatService.genQRCode(String.valueOf(record.getId()), page, UploadPathEnum.innerEnum.CSQ_CERTIFICATE));
			map.put("time", DateUtil.timeStamp2Date(record.getCreateTime().getTime()));
			map.put("date", DateUtil.timeStamp2Date(record.getCreateTime().getTime(), "yyyy-MM-dd"));
		}
		return map;
	}

	@Override
	public Double countMoney(Long userId, Integer inOut) {
		return csqPaymentDao.countMoney(userId, inOut);
	}


	@Override
	public void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId) {
		//获取受益人编号
		Long beneficiaryId = getBeneficiaryId(toType, toId);
		TCsqUserPaymentRecord build1 = TCsqUserPaymentRecord.builder()
			.userId(userId)
			.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode())    //支出
			.entityId(fromId)
			.entityType(fromType)
			.money(amount)
			.orderId(orderId).build();

		TCsqUserPaymentRecord build2 = TCsqUserPaymentRecord.builder()
			.userId(beneficiaryId)
			.inOrOut(CsqUserPaymentEnum.INOUT_IN.toCode())
			.entityId(fromId)
			.entityType(fromType)
			.money(amount)
			.orderId(orderId).build();

		csqUserPaymentDao.insert(build1, build2);
	}

	private Long getBeneficiaryId(Integer toType, Long toId) {
		Long resultId = null;
		switch (CsqEntityTypeEnum.getEnum(toType)) {    //获取到相应枚举类型
			case TYPE_ACCOUNT:
				TCsqUser csqUser = csqUserDao.selectByPrimaryKey(toId);
				resultId = csqUser.getId();
				break;
			case TYPE_FUND:
				TCsqFund csqFund = csqFundDao.selectByPrimaryKey(toId);
				resultId = csqFund.getId();
				break;
			case TYPE_SERVICE:
				TCsqService csqService = csqServiceDao.selectByPrimaryKey(toId);
				resultId = csqService.getId();
				break;
		}
		return resultId;
	}

}
