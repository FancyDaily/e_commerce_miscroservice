package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
@Component
public class CsqOrderDaoImpl implements CsqOrderDao {
	@Override
	public TCsqOrder selectByUserIdAndFundId(Long userId, Long fundId) {
		//TODO
		return null;
	}
}
