package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 15:16
 */
public interface CsqUserInvoiceDao {

	int insert(TCsqUserInvoice... userInvoice);

	List<TCsqUserInvoice> selectByUserId(Long userId);

	TCsqUserInvoice selectByPrimaryKey(Long invoiceId);

	List<TCsqUserInvoice> selectByUserIdDesc(Long userId);

	List<TCsqUserInvoice> selectByUserIdDescPage(Long userId, Integer pageNum, Integer pageSize);

	int update(TCsqUserInvoice build);

	MybatisPlusBuild baseBuild();

	List<TCsqUserInvoice> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize);
}
