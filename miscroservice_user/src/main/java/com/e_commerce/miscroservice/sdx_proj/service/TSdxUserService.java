package com.e_commerce.miscroservice.sdx_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;

public interface TSdxUserService {
	TCsqUser infos(Long userId);

	Object globalDonate();

}
