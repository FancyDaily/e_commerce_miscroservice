package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TOldPayment;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:25
 */
public interface CsqOldPaymentDao {
	List<TOldPayment> selectByOptionUserInPFId(String id, List<String> serviceIds);

	List<TOldPayment> selectInPfIds(List<String> oldFundIds);

	List<TOldPayment> selectInPfIdsDesc(List<String> oldFundIds);

}
