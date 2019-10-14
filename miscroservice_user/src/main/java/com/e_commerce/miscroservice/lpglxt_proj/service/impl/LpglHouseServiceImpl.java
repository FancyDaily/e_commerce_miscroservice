package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
@Service
public class LpglHouseServiceImpl implements LpglHouseService {

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Override
	public QueryResult list(Long estateId, Integer pageNum, Integer pageSize) {
		List<TLpglHouse> tLpglHouses = lpglHouseDao.selectByEstateIdPage(estateId, pageNum, pageSize);
		long total = IdUtil.getTotal();

		return PageUtil.buildQueryResult(tLpglHouses, total);
	}
}
