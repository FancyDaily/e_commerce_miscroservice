package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMoneyApplyRecordDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqMoneyApplyRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-11 17:33
 */
@Component
public class CsqMoneyApplyRecordDaoImpl implements CsqMoneyApplyRecordDao {
	@Override
	public int insert(TCsqMoneyApplyRecord... build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqMoneyApplyRecord.class)
			.eq(TCsqMoneyApplyRecord::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqMoneyApplyRecord> selectWithBuildPage(MybatisPlusBuild baseBuild, Page page) {
		IdUtil.setTotal(baseBuild);
		return MybatisPlus.getInstance().findAll(new TCsqMoneyApplyRecord(), baseBuild.page(page.getPageNum(), page.getPageSize()));
	}

	@Override
	public int update(TCsqMoneyApplyRecord build) {
		return MybatisPlus.getInstance().update(build, baseBuild()
			.eq(TCsqMoneyApplyRecord::getId, build.getId())
		);
	}
}
