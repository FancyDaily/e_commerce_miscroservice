package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 10:35
 */
public interface CsqUserServiceReportDao {
	int insert(TCsqServiceReport serviceReport);

	List<TCsqServiceReport> selectByServiceIdDesc(Long serviceId);

	List<TCsqServiceReport> selectByServiceIdDescPage(Integer pageNum, Integer pageSize, Long serviceId);
}
