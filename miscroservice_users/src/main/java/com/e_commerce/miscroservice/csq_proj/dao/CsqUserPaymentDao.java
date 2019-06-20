package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:35
 */
public interface CsqUserPaymentDao {

	List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOut(Long fundId, int toCode, int toCode1);

	int insert(TCsqUserPaymentRecord... build);

	List<TCsqUserPaymentRecord> selectByEntityIdAndEntityTypeAndInOutDesc(Long serviceId, int toCode, int toCode1);
}
