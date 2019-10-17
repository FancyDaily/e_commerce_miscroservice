package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglEstateDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglEstate;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

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
	public Map<String, Map<Integer, Map<Double, Map<Integer, List<TLpglHouse>>>>> houseMap(Long estateId, Integer buildingNum) {
		List<TLpglHouse> tLpglHouses =
			estateId == null ? lpglHouseDao.selectAll() :
				buildingNum == null ? lpglHouseDao.selectByEstateId(estateId) :
					lpglHouseDao.selectByEstateIdAndBuildingNum(estateId, buildingNum);

		if(tLpglHouses == null) {
			return new HashMap<>();
		}
		//按楼号分组
		return tLpglHouses.stream()
			.collect(groupingBy(TLpglHouse::getGroupName,
				groupingBy(TLpglHouse::getBuildingNum,
					groupingBy(TLpglHouse::getBuildingArea,
						groupingBy(TLpglHouse::getFloorNum)
					)
				)
				)
			);
	}

	@Override
	public void save(String name) {
		TLpglEstate build = TLpglEstate.builder()
			.name(name).build();
		lpglEstateDao.insert(build);
	}

	@Override
	public void modify(Long estateId, String name, String isValid) {
		TLpglEstate build = TLpglEstate.builder().name(name).build();
		build.setId(estateId);
		build.setIsValid(isValid);
		lpglEstateDao.update(build);
	}
}
