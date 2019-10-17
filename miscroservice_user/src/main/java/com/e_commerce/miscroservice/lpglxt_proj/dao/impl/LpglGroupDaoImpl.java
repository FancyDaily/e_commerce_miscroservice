package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglGroupDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TlpglGroup;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分组
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglGroupDaoImpl implements LpglGroupDao {

	@Override
	public List<TlpglGroup> selectByEstateIdPage(Long estateId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild baseBuild = baseBuild()
			.eq(TlpglGroup::getEstateId, estateId);
		IdUtil.setTotal(baseBuild);
		return MybatisPlus.getInstance().findAll(new TlpglGroup(), baseBuild.page(pageNum, pageSize));
	}

	@Override
	public int insert(TlpglGroup build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public int update(TlpglGroup build) {
		return MybatisPlus.getInstance().update(build, baseBuild()
			.eq(TlpglGroup::getId, build.getId())
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TlpglGroup.class).eq(TlpglGroup::getIsValid, AppConstant.IS_VALID_YES);
	}
}
