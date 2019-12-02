package com.e_commerce.miscroservice.sdx_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;

public interface SdxUserService {
	TCsqUser infos(Long userId);

	Object globalDonate();

	Double getCutDownFee(Integer sdxScores);
}
