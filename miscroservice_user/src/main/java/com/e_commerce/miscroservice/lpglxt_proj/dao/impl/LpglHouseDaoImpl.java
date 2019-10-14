package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglHouseDaoImpl implements LpglHouseDao {

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglHouse.class)
			.eq(TLpglHouse::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TLpglHouse> selectByEstateId(Long estateId) {
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), byEstateBuild(estateId)
		);
	}

	private MybatisPlusBuild byEstateBuild(Long estateId) {
		return baseBuild()
			.eq(TLpglHouse::getEstateId, estateId);
	}

	@Override
	public List<TLpglHouse> selectByEstateIdPage(Long estateId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = byEstateBuild(estateId);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TLpglHouse(), mybatisPlusBuild.page(pageNum, pageSize)
		);
	}

	@Override
	public List<TLpglHouse> selectAll() {
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), baseBuild()
		);
	}

	@Override
	public List<TLpglHouse> selectByEstateIdAndBuildingNum(Long estateId, Integer buildingNum) {
		//TODO
		return null;
	}
}
