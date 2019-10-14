package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglEstateDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglEstate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglEstateDaoImpl implements LpglEstateDao {

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglEstate.class)
			.eq(TLpglEstate::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TLpglEstate> selectAll() {
		return MybatisPlus.getInstance().findAll(new TLpglEstate(), baseBuild());
	}

	@Override
	public List<TLpglEstate> selectAllPage(Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(baseBuild());
		return MybatisPlus.getInstance().findAll(new TLpglEstate(), baseBuild().page(pageNum, pageSize));
	}
}
