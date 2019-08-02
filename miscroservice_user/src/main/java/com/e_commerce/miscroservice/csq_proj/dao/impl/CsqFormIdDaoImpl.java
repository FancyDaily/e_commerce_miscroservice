package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TFormid;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqFormIdDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFormId;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-02 14:11
 */
@Component
public class CsqFormIdDaoImpl implements CsqFormIdDao {

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqFormId.class)
			.eq(TCsqFormId::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public TCsqFormId selectByUserIdAndFormId(Long userId, String formId) {
		return MybatisPlus.getInstance().findOne(new TCsqFormId(), baseBuild()
			.eq(TCsqFormId::getUserId, userId)
			.eq(TCsqFormId::getFormId, formId));
	}

	@Override
	public TCsqFormId selectAvailableFormIdByUserId(Long userId){
		long time = System.currentTimeMillis() - 7L * 24 * 3600 * 1000;
		TCsqFormId tcsqFormId = MybatisPlus.getInstance().findOne(new TCsqFormId(), baseBuild()
			.eq(TCsqFormId::getUserId, userId)
			.neq(TFormid::getFormId, "undefined")
			.neq(TFormid::getFormId, "the formId is a mock one")
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqFormId::getCreateTime)));
		if(tcsqFormId == null || tcsqFormId.getCreateTime().getTime() <= time) {
			return null;
		}
		return tcsqFormId;
	}

	@Override
	public int update(TCsqFormId formid) {
		return MybatisPlus.getInstance().update(formid, new MybatisPlusBuild(TCsqFormId.class)
			.eq(TCsqFormId::getId, formid.getId()));
	}
}
