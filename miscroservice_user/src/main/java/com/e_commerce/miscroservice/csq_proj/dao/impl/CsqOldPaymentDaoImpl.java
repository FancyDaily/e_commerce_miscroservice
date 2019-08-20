package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOldPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TOldPayment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:26
 */
@Component
public class CsqOldPaymentDaoImpl implements CsqOldPaymentDao {
	@Override
	public List<TOldPayment> selectByOptionUserInPFId(String id, List<String> serviceIds) {
		return MybatisPlus.getInstance().findAll(new TOldPayment(), new MybatisPlusBuild(TOldPayment.class)
			.eq(TOldPayment::getStatus, 1)
			.eq(TOldPayment::getOptionuser, id)
			.in(TOldPayment::getPfid, serviceIds));
	}

	@Override
	public List<TOldPayment> selectInPfIds(List<String> oldFundIds) {
		return MybatisPlus.getInstance().findAll(new TOldPayment(), new MybatisPlusBuild(TOldPayment.class)
			.eq(TOldPayment::getStatus, 1)
			.in(TOldPayment::getPfid, oldFundIds));
	}

	@Override
	public List<TOldPayment> selectInPfIdsDesc(List<String> oldFundIds) {
		return MybatisPlus.getInstance().findAll(new TOldPayment(), new MybatisPlusBuild(TOldPayment.class)
			.eq(TOldPayment::getStatus, 1)
			.in(TOldPayment::getPfid, oldFundIds)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TOldPayment::getAddtime)));
	}

	@Override
	public List<TOldPayment> selectInPfIdsAsc(List<String> oldFundIds) {
		return MybatisPlus.getInstance().findAll(new TOldPayment(), new MybatisPlusBuild(TOldPayment.class)
			.eq(TOldPayment::getStatus, 1)
			.in(TOldPayment::getPfid, oldFundIds)
			.orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TOldPayment::getAddtime)));
	}
}
