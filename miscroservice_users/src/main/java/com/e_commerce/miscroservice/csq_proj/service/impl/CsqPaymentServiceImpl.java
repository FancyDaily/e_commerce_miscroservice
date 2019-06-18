package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @ClassName CsqPaymentServiceImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-17 15:15
 * @Version 1.0
 */
@Service
public class CsqPaymentServiceImpl implements CsqPaymentService {
	@Autowired
	private CsqPaymentDao csqPaymentDao;
	@Override
	public QueryResult<TCsqUserPaymentRecord >  findWaters(Integer pageNum, Integer pageSize, Long userId) {
		QueryResult<TCsqUserPaymentRecord> queryResult = new QueryResult<TCsqUserPaymentRecord>();

		Page<Object> page = PageHelper.startPage(pageNum,pageSize);
		List<TCsqUserPaymentRecord> records = csqPaymentDao.findWaters(userId);

		queryResult.setResultList(records);
		queryResult.setTotalCount(page.getTotal());
		return queryResult;
	}
}
