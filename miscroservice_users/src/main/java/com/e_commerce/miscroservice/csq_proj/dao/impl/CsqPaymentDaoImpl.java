package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.sun.deploy.association.utility.AppConstants;
import org.springframework.stereotype.Repository;

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
	@Override
	public List<TCsqUserPaymentRecord> findWaters(Long userId) {
		List<TCsqUserPaymentRecord> list = MybatisPlus.getInstance().finAll(new TCsqUserPaymentRecord(),new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId,userId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
		);
		return list;
	}
}
