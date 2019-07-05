package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.enums.application.UploadPathEnum;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFundDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPaymentDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


	@Override
	public QueryResult<CsqUserPaymentRecordVo> findWaters(Integer pageNum, Integer pageSize, Long userId) {
		QueryResult<CsqUserPaymentRecordVo> queryResult = new QueryResult<>();

		Page<Object> page = PageHelper.startPage(pageNum, pageSize);
		List<TCsqUserPaymentRecord> records = csqPaymentDao.findWaters(userId);
		List<CsqUserPaymentRecordVo> recordVos = records.stream()
			.map(a -> a.copyUserPaymentRecordVo()).collect(Collectors.toList());

		queryResult.setResultList(recordVos);
		queryResult.setTotalCount(page.getTotal());
		return queryResult;
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
