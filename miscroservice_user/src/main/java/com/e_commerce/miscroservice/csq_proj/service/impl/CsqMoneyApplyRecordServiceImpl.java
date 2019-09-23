package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqMoneyApplyRecordEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqServiceEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqUserPaymentEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.*;
import com.e_commerce.miscroservice.csq_proj.po.*;
import com.e_commerce.miscroservice.csq_proj.service.CsqMoneyApplyRecordService;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.service.CsqServiceService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqMoneyApplyRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-11 17:32
 */
@Service
public class CsqMoneyApplyRecordServiceImpl implements CsqMoneyApplyRecordService {

	@Autowired
	CsqMoneyApplyRecordDao csqMoneyApplyRecordDao;

	@Autowired
	CsqServiceDao csqServiceDao;

	@Autowired
	CsqUserDao csqUserDao;

	@Autowired
	CsqServiceService csqServiceService;

	@Autowired
	CsqFundDao csqFundDao;

	@Autowired
	CsqPaymentDao csqPaymentDao;

	@Autowired
	CsqPaymentService csqPaymentService;

	@Override
	public void addMoneyApply(TCsqMoneyApplyRecord obj) {
		//对应对象余额check（最好进行）
		obj.setStatus(CsqMoneyApplyRecordEnum.STATUS_UNDER_CERT.getCode());    //待审核
		csqMoneyApplyRecordDao.insert(obj);
	}

	@Override
	public QueryResult moneyApplyList(String searchParam, Integer searchType, Boolean isFuzzySearch, Page page, Integer... status) {
		page = PageUtil.prePage(page);
		MybatisPlusBuild baseBuild = csqMoneyApplyRecordDao.baseBuild();

		Integer SEARCH_TYPE_SERVICE_OR_FUND = 0;
		Integer SEARCH_TYPE_PERSON = 1;

		boolean isPerson = searchType.equals(SEARCH_TYPE_PERSON);
		if (isPerson) {    //去用户表获取到编号
			List<TCsqUser> tCsqUsers = csqUserDao.selectByName(searchParam, isFuzzySearch);
			List<Long> userIds = tCsqUsers.stream()
				.map(TCsqUser::getId).collect(Collectors.toList());
			baseBuild
				.in(TCsqMoneyApplyRecord::getUserId, userIds);    //实际递交申请的申请者的昵称
		} else {    //去serivce表获取到编号
			List<TCsqService> csqServices = csqServiceDao.selectByName(searchParam, isFuzzySearch);
			List<Long> serviceIds = csqServices.stream()
				.filter(a -> CsqServiceEnum.TYPE_SERIVE.getCode() == a.getType())
				.map(TCsqService::getId).collect(Collectors.toList());
			List<Long> fundIds = csqServices.stream()
				.filter(a -> CsqServiceEnum.TYPE_FUND.getCode() == a.getType())
				.map(TCsqService::getFundId).collect(Collectors.toList());
			boolean noneEmptyServiceCondition = !serviceIds.isEmpty();
			boolean noneEmptyFundCondition = !fundIds.isEmpty();
			boolean allCondition = noneEmptyFundCondition && noneEmptyServiceCondition;
			if (noneEmptyServiceCondition || noneEmptyFundCondition) {
				baseBuild = baseBuild
					.and()
					.groupBefore()
					.groupBefore();

				baseBuild = baseBuild
					.eq(TCsqMoneyApplyRecord::getEntityType, noneEmptyFundCondition ? CsqEntityTypeEnum.TYPE_FUND.toCode() : CsqEntityTypeEnum.TYPE_SERVICE.toCode())
					.in(TCsqMoneyApplyRecord::getEntityId, noneEmptyFundCondition ? fundIds : serviceIds)
					.groupAfter()
				;

				baseBuild = allCondition ?
					baseBuild
						.or()
						.groupBefore()
						.eq(TCsqMoneyApplyRecord::getEntityType, noneEmptyFundCondition ?  CsqEntityTypeEnum.TYPE_SERVICE.toCode() : CsqEntityTypeEnum.TYPE_FUND.toCode())
						.in(TCsqMoneyApplyRecord::getEntityId, noneEmptyFundCondition ? serviceIds: fundIds)
						.groupAfter() : baseBuild
				;

				baseBuild.groupAfter();

			}
		}

		//状态
		baseBuild = status == null || status.length==0 ? baseBuild : baseBuild.in(TCsqMoneyApplyRecord::getStatus, Arrays.asList(status));

		//排序
		baseBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqMoneyApplyRecord::getCreateTime));

		List<TCsqMoneyApplyRecord> tCsqMoneyApplyRecords = csqMoneyApplyRecordDao.selectWithBuildPage(baseBuild, page);
		List<Long> fundIds = tCsqMoneyApplyRecords.stream()
			.filter(a -> a.getEntityType() == CsqEntityTypeEnum.TYPE_FUND.toCode())
			.map(TCsqMoneyApplyRecord::getEntityId).collect(Collectors.toList());
		List<Long> serviceIds = tCsqMoneyApplyRecords.stream()
			.filter(a -> a.getEntityType() == CsqEntityTypeEnum.TYPE_SERVICE.toCode())
			.map(TCsqMoneyApplyRecord::getEntityId).collect(Collectors.toList());
		List<TCsqService> csqServices = csqServiceDao.selectInIdsOrInFundIds(serviceIds, fundIds);
		Map<Long, List<TCsqService>> serviceMap = csqServices.stream()
			.collect(Collectors.groupingBy(TCsqService::getId));

		List<CsqMoneyApplyRecordVo> vos = tCsqMoneyApplyRecords.stream()
			.map(a -> {
				CsqMoneyApplyRecordVo vo = a.copyCsqMoneyApplyRecordVo();
				String date = DateUtil.timeStamp2Date(a.getCreateTime().getTime());
				vo.setDate(date);
				List<TCsqService> services = serviceMap.get(a.getEntityId());
				if(services != null) {
					vo.setName(services.get(0).getName());
				}
				return vo;
			}).collect(Collectors.toList());
		return PageUtil.buildQueryResult(vos, IdUtil.getTotal());
	}

	@Override
	public void certMoneyApply(Long ids, Long csqMoneyRecordId, Integer status) {
		TCsqMoneyApplyRecord csqMoneyApplyRecord = csqMoneyApplyRecordDao.selectByPrimaryKey(csqMoneyRecordId);
		TCsqMoneyApplyRecord build = TCsqMoneyApplyRecord.builder()
			.status(status).build();
		//针对审核通过和不通过可能有不同额外操作
		csqMoneyApplyRecordDao.update(build);
		//TODO 若审核通过，项目或基金做一条支出记录, 同时余额减少,如果是基金，要同步项目
		if (CsqMoneyApplyRecordEnum.STATUS_CERT_PASS.getCode().equals(status)) {    //审核通过
			Integer entityType = csqMoneyApplyRecord.getEntityType();
			Long entityId = csqMoneyApplyRecord.getEntityId();
			Double money = csqMoneyApplyRecord.getMoney();
			boolean isFund = CsqEntityTypeEnum.TYPE_FUND.toCode() == entityType;
			//TODO 插入支出流水
			Map<String, Object> beneficiaryMap = csqPaymentService.getBeneficiaryMap(entityType, entityId);
			Long userId = (Long) beneficiaryMap.get("beneficiaryId");
			StringBuilder builder = new StringBuilder("提款");
			builder.append(money).append("元");
			TCsqUserPaymentRecord inserter = TCsqUserPaymentRecord.builder()
				.entityId(entityId)
				.entityType(entityType)
				.userId(userId)
				.money(money)
				.inOrOut(CsqUserPaymentEnum.INOUT_OUT.toCode())
				.description(build.toString()).build();
			csqPaymentDao.insert(inserter);

			if (isFund) {    //为基金
				TCsqFund csqFund = csqFundDao.selectByPrimaryKey(entityId);
				Double balance = csqFund.getBalance();
				double theAmount = getSurplusMoney(money, balance);
				csqFund.setBalance(theAmount);
				csqServiceService.synchronizeService(csqFund);
				return;
			}
			//为项目
			TCsqService csqService = csqServiceDao.selectByPrimaryKey(entityId);
			Double surplusAmount = csqService.getSurplusAmount();
			double theAmount = getSurplusMoney(money, surplusAmount);
			csqService.setSurplusAmount(theAmount);
			csqServiceDao.update(csqService);
			//说明: 实现从项目或基金到 entityType为1 即平台外的支出.此处有个问题，即没有接收方的时候，会缺订单和流水。
			//那么当有接收方时，才会产生对方的订单与流水。
		}
	}

	private double getSurplusMoney(Double money, Double balance) {
		double theAmount = balance - money;
		if (theAmount < 0) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "余额不足，无法审批提款！");
		}
		return theAmount;
	}
}
