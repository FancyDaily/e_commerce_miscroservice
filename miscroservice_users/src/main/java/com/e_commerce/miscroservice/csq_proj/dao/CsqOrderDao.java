package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.user.po.TUser;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:13
 */
public interface CsqOrderDao {
	
	TCsqOrder selectByUserIdAndFundId(Long userId, Long fundId);
}
