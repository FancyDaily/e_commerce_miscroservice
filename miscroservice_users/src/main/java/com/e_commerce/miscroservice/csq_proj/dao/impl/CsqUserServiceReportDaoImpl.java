package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserServiceReportDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 10:36
 */
@Component
public class CsqUserServiceReportDaoImpl implements CsqUserServiceReportDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqServiceReport.class)
			.eq(TCsqServiceReport::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public int insert(TCsqServiceReport serviceReport) {
		return MybatisPlus.getInstance().save(serviceReport);
	}

	@Override
	public List<TCsqServiceReport> selectByServiceIdDesc(Long serviceId) {
		return MybatisPlus.getInstance().finAll(new TCsqServiceReport(), baseWhereBuild()
			.eq(TCsqServiceReport::getServiceId, serviceId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqServiceReport::getCreateTime)));
	}
}
