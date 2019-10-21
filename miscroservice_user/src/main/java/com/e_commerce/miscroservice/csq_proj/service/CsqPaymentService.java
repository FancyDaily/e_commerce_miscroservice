package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicUserVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqLineDiagramData;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CsqPaymentService {
	/**
	 * 查询流水信息
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	QueryResult<CsqUserPaymentRecordVo> findWaters(Integer pageNum, Integer pageSize, Long userId, Integer option);

	Object findWatersAndTotal(Integer pageNum, Integer pageSize, Long userId, Integer option, boolean isGroupingByYears);

	QueryResult<Map<String, Object>> findWatersGroupingByYear(Integer pageNum, Integer pageSize, Long userId, Integer option);

	/**
	 * 查询我的证书
	 *
	 * @param orderNo
	 * @param recordId
	 * @param userId
	 * @return
	 */
	Map<String,Object> findMyCertificate(String orderNo, Long recordId, Long userId);

	/**
	 * 收入支出 统计
	 * @param userId
	 * @param inOut
	 * @return
	 */
	Double countMoney(Long userId, Integer inOut);

	void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId);

	void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId, String description);

	void savePaymentRecord(Long userId, Integer fromType, Long fromId, Integer toType, Long toId, Double amount, Long orderId, String description, Timestamp timestamp);

	void savePaymentRecord(TCsqOrder tCsqOrder);

	Map<String, Object> getBeneficiaryMap(Integer toType, Long toId);

	List<Long> getPaymentRelatedOrderIds(Long entityId);

	List<Long> getPaymentRelatedOrderIds(Long entityId, Integer entityType);

	List<CsqBasicUserVo> getTopDonaters(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords, List<Long> orderIds);

	Map<String, Object> findWatersAndTotal(String searchParma, Integer pageNum, Integer pageSize, Boolean isFuzzySearch, String orderNo);

	HashMap<String, Object> donateRecordList(Long userIds, String searchParam, Page page, boolean isFuzzySearch);

	QueryResult platformDataStatistics(Long userIds, String searchParam, String startDate, String endDate, Integer pageNum, Integer pageSize, Boolean isFuzzySearch, Boolean isServiceOnly);

	Double getPlatFromInCome();

	HashMap<String, List<CsqLineDiagramData>> platformDataStatistics(Long userIds, Long entityId, Integer entityType, String startDate, String endDate);
}
