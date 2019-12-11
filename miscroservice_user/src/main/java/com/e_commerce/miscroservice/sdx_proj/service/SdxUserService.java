package com.e_commerce.miscroservice.sdx_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;

import java.util.List;

public interface SdxUserService {
	TCsqUser infos(Long userId);

	Object globalDonate();

	Double transScoresToMoney(Integer sdxScores);

	Double getCutDownFee(Integer sdxScores);
}
