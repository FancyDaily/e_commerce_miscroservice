package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:44
 */
public interface CsqUserService {
	void checkAuth(TCsqUser user);

	void checkAuth(Long userId);
}
