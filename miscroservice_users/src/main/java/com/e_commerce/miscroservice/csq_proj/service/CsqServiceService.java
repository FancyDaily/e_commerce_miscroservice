package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 14:02
 */
public interface CsqServiceService {
	/**
	 * 发布项目
	 * @param userId
	 * @param service
	 */
	void publish(Long userId, TCsqService service);

	/**
	 * 项目列表
	 * @param userId
	 * @param option
	 * @param pageNum
	 * @param pageSizez
	 * @return
	 */
	QueryResult<TCsqService> list(Long userId, Integer option, Integer pageNum, Integer pageSizez);

	/**
	 * 项目详情
	 *
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	Map<String, Object> detail(Long userId, Long serviceId);

	/**
	 * 项目审核
	 * @param userId
	 * @param serviceId
	 */
	void cert(Long userId, Long serviceId);

	/**
	 * 项目支出明细
	 * @param userId
	 * @param serviceId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	QueryResult<TCsqUserPaymentRecord> billOut(Long userId, Long serviceId, Integer pageNum, Integer pageSize);

	/**
	 * 发布项目汇报
	 * @param userId
	 * @param serviceId
	 * @param serviceReport
	 */
	void submitReport(Long userId, Long serviceId, TCsqServiceReport serviceReport);

	/**
	 * 捐款
	 * @param orderNo
	 */
	void donate(String orderNo);
}
