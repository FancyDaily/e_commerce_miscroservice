package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 10:35
 */
public interface CsqUserPaymentDao {

	List<TCsqUserPaymentRecord> selectByToType(int... type);

	List<TCsqUserPaymentRecord> selectByFromTypeAndToTypeDesc(int type, int subType);

	List<TCsqUserPaymentRecord> selectByToTypeDesc(int type);

	List<TCsqUserPaymentRecord> selectByToTypeAndUserId(long userId, int... type);

	List<TCsqUserPaymentRecord> selectByFromTypeAndServiceId(int toCode, Long serviceId);

	int insert(TCsqUserPaymentRecord build);

	List<TCsqUserPaymentRecord> selectByFromTypeAndServiceIdDesc(int toCode, Long serviceId);

	List<TCsqUserPaymentRecord> selectByToTypeAndToId(int toCode, Long fundId);

	List<TCsqUserPaymentRecord> selectByToTypeAndToIdAndCreateTimeBetween(int toCode, Long fundId);
}
