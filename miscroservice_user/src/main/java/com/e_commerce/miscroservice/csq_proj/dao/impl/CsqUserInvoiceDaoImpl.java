package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserInvoiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 15:16
 */
@Component
public class CsqUserInvoiceDaoImpl implements CsqUserInvoiceDao {

	@Override
	public int insert(TCsqUserInvoice... userInvoice) {
		return MybatisPlus.getInstance().save(userInvoice);
	}

	@Override
	public List<TCsqUserInvoice> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqUserInvoice(), baseBuild()
			.eq(TCsqUserInvoice::getUserId, userId));
	}

	@Override
	public TCsqUserInvoice selectByPrimaryKey(Long invoiceId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserInvoice(), baseBuild()
			.eq(TCsqUserInvoice::getId, invoiceId));
	}


	@Override
	public List<TCsqUserInvoice> selectByUserIdDesc(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqUserInvoice(), byUserIdDescBuild(userId));
	}

	private MybatisPlusBuild byUserIdDescBuild(Long userId) {
		return baseBuild()
			.eq(TCsqUserInvoice::getUserId, userId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserInvoice::getCreateTime));
	}

	@Override
	public List<TCsqUserInvoice> selectByUserIdDescPage(Long userId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = byUserIdDescBuild(userId);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserInvoice(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public int update(TCsqUserInvoice build) {
		return MybatisPlus.getInstance().update(build, new MybatisPlusBuild(TCsqUserInvoice.class)
			.eq(TCsqUserInvoice::getId, build.getId()));
	}

	@Override
	public MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqUserInvoice.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqUserInvoice> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(baseBuild);
		return MybatisPlus.getInstance().findAll(new TCsqUserInvoice(), baseBuild.page(pageNum, pageSize));

	}

	@Override
	public List<TCsqUserInvoice> selectByIsOut(int code) {
		return MybatisPlus.getInstance().findAll(new TCsqUserInvoice(), baseBuild()
			.eq(TCsqUserInvoice::getIsOut, code)
		);

	}
}
