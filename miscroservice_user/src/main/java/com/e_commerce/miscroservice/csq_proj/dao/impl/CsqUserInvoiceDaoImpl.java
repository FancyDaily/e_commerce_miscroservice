package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
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
		return MybatisPlus.getInstance().findAll(new TCsqUserInvoice(), new MybatisPlusBuild(TCsqUserInvoice.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserInvoice::getUserId, userId));
	}

	@Override
	public TCsqUserInvoice selectByPrimaryKey(Long invoiceId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserInvoice(), new MybatisPlusBuild(TCsqUserInvoice.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqUserInvoice::getId, invoiceId));
	}
}
