package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateRecordVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceListVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceReportVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;

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
	Long publish(Long userId, TCsqService service);

	/**
	 * 项目列表
	 * @param userId
	 * @param option
	 * @param pageNum
	 * @param pageSizez
	 * @return
	 */
	QueryResult<CsqServiceListVo> list(Long userId, Integer option, Integer pageNum, Integer pageSizez);

	/**
	 * 项目详情
	 *
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	Map<String, Object> detail(Long userId, Long serviceId);

	List<CsqDonateRecordVo> dealWithRedisDonateRecord(Long serviceId);

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
	QueryResult<CsqUserPaymentRecordVo> billOut(Long userId, Long serviceId, Integer pageNum, Integer pageSize);

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

	/**
	 * 确认发布权限
	 * @param userId
	 */
	void checkPubAuth(Long userId);

	/**
	 * 基金向基金类型的项目同步
	 * @param fundId
	 */
	void synchronizeService(Long fundId);

	void synchronizeService(TCsqFund csqFund);

	/**
	 * 项目汇报列表
	 * @param serviceId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	QueryResult<CsqServiceReportVo> reportList(Long serviceId, Integer pageNum, Integer pageSize);

	/**
	 * 捐助记录列表
	 * @param serviceId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	QueryResult donateList(Long serviceId, Integer pageNum, Integer pageSize);

	/**
	 * 修改项目信息
	 * @param csqService
	 */
	void modify(TCsqService csqService);

	/**
	 * 获取项目
	 * @param fundId
	 * @return
	 */
	TCsqService getService(Long fundId);

	/**
	 * 项目汇报详情
	 * @param serviceReportId
	 * @return
	 */
	CsqServiceReportVo reportDetail(Long serviceReportId);

	TCsqService selectByExtend(String extend);

	List<TCsqService> findAllByTypeAndIdGreaterThan(int code, long l);

	List<TCsqService> selectInIds(Long... ids);

	List<TCsqService> selectInExtends(List<Long> collect);

	QueryResult<TCsqService> list(String searchParam, Integer pageNum, Integer pageSize, boolean isFuzzySearch);

	Map<Integer, Object> countGroupByStatus(Long userId);

	void synchronizeService(List<TCsqFund> toUpdateFunds);

	String getName(Integer entityType, Long k);

	Map serviceNameCheck(String name, Integer type);

	Map<String, Object> getTotalBalance();


}
