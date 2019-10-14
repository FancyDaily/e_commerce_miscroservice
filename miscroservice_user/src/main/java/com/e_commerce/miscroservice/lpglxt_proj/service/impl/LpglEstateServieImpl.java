package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglEstateDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglEstate;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglEstateService;
import com.e_commerce.miscroservice.lpglxt_proj.vo.LpglHouseMapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglEstateServieImpl implements LpglEstateService {

	@Autowired
	private LpglEstateDao lpglEstateDao;

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Override
	public QueryResult list(Integer pageNum, Integer pageSize) {
		List<TLpglEstate> tLpglEstates = lpglEstateDao.selectAllPage(pageNum, pageSize);
		long total = IdUtil.getTotal();

		return PageUtil.buildQueryResult(tLpglEstates, total);
	}

	@Override
	public List<LpglHouseMapVo> houseMap(Long estateId, Integer buildingNum) {
		List<TLpglHouse> tLpglEstates =
			estateId == null ? lpglHouseDao.selectAll() :
				buildingNum == null ? lpglHouseDao.selectByEstateId(estateId) :
					lpglHouseDao.selectByEstateIdAndBuildingNum(estateId, buildingNum);

		//按面积分组

		//TODO
		return null;
	}
}
