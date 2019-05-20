package com.e_commerce.miscroservice.xiaoshi_proj.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TServiceSummary;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.product.dao.serviceSummaryDao;
import org.springframework.stereotype.Repository;

/**
 * @author 姜修弘
 * @date 2019/3/18
 */
@Repository
public class serviceSummaryImpl implements serviceSummaryDao {

	/**
	 * 插入精彩瞬间
	 * @param serviceSummary
	 * @return
	 */
	public long saveServiceSummary(TServiceSummary serviceSummary){
		return MybatisPlus.getInstance().save(serviceSummary);
	}

	/**
	 * 根据serviceId 查找精彩瞬间
	 * @param serviceId
	 * @return
	 */
	public TServiceSummary selectServiceSummaryByServiceId(Long serviceId){
		return MybatisPlus.getInstance().findOne(new TServiceSummary(), new MybatisPlusBuild(TServiceSummary.class)
				.eq(TServiceSummary::getServiceId, serviceId));
	}

}
