package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
@Service
public class LpglPositionServiceImpl implements LpglPositionService {

	@Autowired
	private LpglPositionDao lpglPositionDao;

	@Override
	public void modify(Long positionId, Double discountCredit) {
		lpglPositionDao.modify(positionId, discountCredit);
	}
}
