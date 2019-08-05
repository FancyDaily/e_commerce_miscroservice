package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserServiceReportDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import org.springframework.stereotype.Component;

import java.util.List;

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

	@Override
	public List<TCsqServiceReport> selectByServiceIdDesc(Long serviceId) {
		return MybatisPlus.getInstance().findAll(new TCsqServiceReport(), byServiceIdDescBuild(serviceId));
	}

	@Override
	public List<TCsqServiceReport> selectByServiceIdDescPage(Integer pageNum, Integer pageSize, Long serviceId) {
		MybatisPlusBuild mybatisPlusBuild = byServiceIdDescBuild(serviceId);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqServiceReport(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public TCsqServiceReport selectByPrimaryKey(Long serviceReportId) {
		return MybatisPlus.getInstance().findOne(new TCsqServiceReport(), baseBuild()
			.eq(TCsqServiceReport::getId, serviceReportId));
	}

	private MybatisPlusBuild byServiceIdDescBuild(Long serviceId) {
		return baseBuild()
			.eq(TCsqServiceReport::getServiceId, serviceId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqServiceReport::getCreateTime));
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqServiceReport.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES);
	}
}
