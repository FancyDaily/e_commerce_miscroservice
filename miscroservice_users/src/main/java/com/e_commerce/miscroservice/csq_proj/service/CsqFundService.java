package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;

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
	 * @param fund
	 */
	void modifyFund(TCsqFund fund);

	/**
	 * 申请基金前的校验(在付款之前,避免支付之后无谓的退款)
	 * @param userId
	 * @return
	 */
	boolean checkBeforeApplyForAFund(Long userId);
}
