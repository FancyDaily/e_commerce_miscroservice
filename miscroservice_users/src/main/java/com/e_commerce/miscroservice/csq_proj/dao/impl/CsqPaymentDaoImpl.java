package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.mapper.CsqPaymentMapper;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @ClassName CsqPaymentDaoImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-17 15:17
 * @Version 1.0
 */
@Repository
public class CsqPaymentDaoImpl implements CsqPaymentDao {
	@Resource
	private CsqPaymentMapper csqPaymentMapper;
	@Override
	public List<TCsqUserPaymentRecord> findWaters(Long userId) {
		List<TCsqUserPaymentRecord> list = MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(),new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId,userId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
		);
		return list;
	}

	@Override
	public TCsqUserPaymentRecord findWaterById(Long recordId) {
		TCsqUserPaymentRecord record = MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(),new MybatisPlusBuild(TCsqUserPaymentRecord.class)
		.eq(TCsqUserPaymentRecord::getId,recordId)
		.eq(TCsqUserPaymentRecord::getId,AppConstant.IS_VALID_YES)
		);
		return record;
	}



	@Override
	public Double countMoney(Long userId, Integer inOut) {
		Double money = csqPaymentMapper.countMoney(userId,inOut);
		return money;
	}
}
