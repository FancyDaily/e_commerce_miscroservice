package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglPositionDaoImpl implements LpglPositionDao {

	@Override
	public int modify(Long positionId, Double discountCredit) {
		TLpglPosistion build = TLpglPosistion.builder()
			.discountCredit(discountCredit).build();
		build.setId(positionId);
		return MybatisPlus.getInstance().update(build, baseBuild()
			.eq(TLpglPosistion::getId, build.getId())
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglPosistion.class)
			.eq(TLpglPosistion::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public TLpglPosistion selectByPrimaryKey(Long posistionId) {
		return MybatisPlus.getInstance().findOne(new TLpglPosistion(), baseBuild()
			.eq(TLpglPosistion::getId, posistionId)
		);
	}

	@Override
	public List<TLpglPosistion> selectByLevel(Integer level) {
		return MybatisPlus.getInstance().findAll(new TLpglPosistion(), baseBuild()
			.eq(TLpglPosistion::getLevel, level)
		);
	}

	@Override
	public TLpglPosistion getPosition(Integer level) {
		return MybatisPlus.getInstance().findOne(new TLpglPosistion(), baseBuild()
			.eq(TLpglPosistion::getLevel, level)
		);
	}
}
