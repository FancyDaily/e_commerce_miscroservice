package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.CsqEntityTypeEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqMoneyApplyRecordEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqServiceEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMoneyApplyRecordDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqMoneyApplyRecord;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqMoneyApplyRecordService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqMoneyApplyRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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

	@Override
	public void addMoneyApply(TCsqMoneyApplyRecord obj) {
		obj.setStatus(CsqMoneyApplyRecordEnum.STATUS_UNDER_CERT.getCode());	//待审核
		csqMoneyApplyRecordDao.insert(obj);
	}

	@Override
	public QueryResult moneyApplyList(String searchParam, Integer searchType, Boolean isFuzzySearch, Page page, Integer... status) {
		page = PageUtil.prePage(page);
		MybatisPlusBuild baseBuild = csqMoneyApplyRecordDao.baseBuild();

		Integer SEARCH_TYPE_SERVICE_OR_FUND = 0;
		Integer SEARCH_TYPE_PERSON = 1;

		boolean isPerson = searchType.equals(SEARCH_TYPE_PERSON);
		if(isPerson) {	//去用户表获取到编号
			List<TCsqUser> tCsqUsers = csqUserDao.selectByName(searchParam, isFuzzySearch);
			List<Long> userIds = tCsqUsers.stream()
				.map(TCsqUser::getId).collect(Collectors.toList());
			baseBuild
				.in(TCsqMoneyApplyRecord::getUserId, userIds);	//实际递交申请的申请者的昵称
		} else {	//去serivce表获取到编号
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
			if(noneEmptyServiceCondition || noneEmptyFundCondition) {
				baseBuild = baseBuild
					.and()
					.groupBefore()
					.groupBefore();

					baseBuild = baseBuild
					.eq(TCsqMoneyApplyRecord::getEntityType, noneEmptyFundCondition? CsqEntityTypeEnum.TYPE_FUND.toCode(): CsqEntityTypeEnum.TYPE_SERVICE.toCode())
					.in(TCsqMoneyApplyRecord::getEntityId, noneEmptyFundCondition? fundIds: serviceIds)
					.groupAfter()
					;

					baseBuild = allCondition?
						baseBuild
						.or()
						.groupBefore()
							.eq(TCsqMoneyApplyRecord::getEntityType, noneEmptyFundCondition? CsqEntityTypeEnum.TYPE_FUND.toCode(): CsqEntityTypeEnum.TYPE_SERVICE.toCode())
							.in(TCsqMoneyApplyRecord::getEntityId, noneEmptyFundCondition? fundIds: serviceIds)
							.groupAfter():baseBuild
					;

					baseBuild.groupAfter();

			}
		}

		//状态
		baseBuild = status == null? baseBuild : baseBuild.in(TCsqMoneyApplyRecord::getStatus, Arrays.asList(status));

		//排序
		baseBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqMoneyApplyRecord::getCreateTime));

		List<TCsqMoneyApplyRecord> tCsqMoneyApplyRecords = csqMoneyApplyRecordDao.selectWithBuildPage(baseBuild, page);
		List<CsqMoneyApplyRecordVo> vos = tCsqMoneyApplyRecords.stream()
			.map(TCsqMoneyApplyRecord::copyCsqMoneyApplyRecordVo).collect(Collectors.toList());
		return PageUtil.buildQueryResult(vos, IdUtil.getTotal());
	}

	@Override
	public void certMoneyApply(Long ids, Long csqMoneyRecordId, Integer status) {
		TCsqMoneyApplyRecord build = TCsqMoneyApplyRecord.builder()
			.status(status).build();
		//针对审核通过和不通过可能有不同额外操作
		csqMoneyApplyRecordDao.update(build);
	}
}
