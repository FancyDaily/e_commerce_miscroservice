package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundVo;

import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:58
 */
public interface CsqFundService {

	/**
	 * 申请专项基金
	 * @param userId
	 * @param orderNo
	 */
	void applyForAFund(Long userId, String orderNo);

	/**
	 * 修改基金基本信息
	 * @param userId
	 * @param fund
	 */
	void modifyFund(Long userId, TCsqFund fund);

	/**
	 * 申请基金前的校验(在付款之前,避免支付之后无谓的退款)
	 * @param userId
	 * @return
	 */
	boolean checkBeforeApplyForAFund(Long userId);

	/**
	 * 审核 - 基金公开
	 * @param userId
	 * @param fundId
	 * @param option
	 */
	void certFund(Long userId, Long fundId, Integer option);

	/**
	 * 基金详情
	 * @param fundId
	 * @return
	 */
	CsqFundVo fundDetail(Long fundId);

	QueryResult getGotoList(Long fundId, Integer pageNum, Integer pageSize);

	/**
	 * 基金分享
	 * @param userId
	 * @param fundId
	 * @return
	 */
	Map<String, Object> share(Long userId, Long fundId);

	/**
	 * 列表
	 *
	 * @param userId
	 * @param option
	 * @return
	 */
	QueryResult<CsqFundVo> list(Long userId, Integer pageNum, Integer pageSize, Integer... option);

	void insertForSomeOne(Long userId);

	/**
	 * 获取基金捐献项目列表
	 * @param serviceId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	QueryResult donateServiceList(Long serviceId, Integer pageNum, Integer pageSize);

	boolean isMine(Long fundId, Long userId);
}
