package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUserPosistion;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglUserPositionDaoImpl implements LpglUserPositionDao {

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglUserPosistion.class)
			.eq(TLpglCert::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TLpglUserPosistion> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().findAll(new TLpglUserPosistion(), new MybatisPlusBuild(TLpglUserPosistion.class)
			.eq(TLpglUserPosistion::getUserId, userId)
		);
	}

	@Override
	public List<TLpglUserPosistion> selectByPositionId(Long id) {
		return MybatisPlus.getInstance().findAll(new TLpglUserPosistion(), baseBuild()
			.eq(TLpglUserPosistion::getPosistionId, id)
		);
	}

	@Override
	public List<TLpglUserPosistion> selectInPositionIds(List<Long> positionIds) {
		return MybatisPlus.getInstance().findAll(new TLpglUserPosistion(), baseBuild()
			.in(TLpglUserPosistion::getPosistionId, positionIds)
		);
	}
}
