package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserServiceReportDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 10:36
 */
@Component
public class CsqUserServiceReportDaoImpl implements CsqUserServiceReportDao {
	@Override
	public int insert(TCsqServiceReport serviceReport) {
		return MybatisPlus.getInstance().save(serviceReport);
	}
}
