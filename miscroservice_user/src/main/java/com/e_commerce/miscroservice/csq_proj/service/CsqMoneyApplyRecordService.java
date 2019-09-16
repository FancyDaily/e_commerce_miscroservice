package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqMoneyApplyRecord;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-11 17:32
 */
public interface CsqMoneyApplyRecordService {

	/**
	 * 发起打款请求
	 * @param obj
	 */
	void addMoneyApply(TCsqMoneyApplyRecord obj);

	/**
	 * 打款请求列表
	 * @param searchParam
	 * @param searchType
	 * @param isFuzzySearch
	 * @param page
	 * @param status
	 * @return
	 */
	QueryResult moneyApplyList(String searchParam, Integer searchType, Boolean isFuzzySearch, Page page, Integer... status);

	/**
	 * 审核打款请求
	 * @param ids
	 * @param csqMoneyRecordId
	 * @param status
	 */
	void certMoneyApply(Long ids, Long csqMoneyRecordId, Integer status);

}
