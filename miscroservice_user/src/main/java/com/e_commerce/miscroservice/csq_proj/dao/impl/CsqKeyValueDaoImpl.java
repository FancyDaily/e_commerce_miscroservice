package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqKeyValueDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqKeyValue;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 17:39
 */
@Component
public class CsqKeyValueDaoImpl implements CsqKeyValueDao {

	private MybatisPlusBuild baseBUild() {
		return new MybatisPlusBuild(TCsqKeyValue.class)
			.eq(TCsqKeyValue::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqKeyValue> selectByKeyAndType(Long userId, int code) {
		return MybatisPlus.getInstance().findAll(new TCsqKeyValue(), baseBUild()
			.eq(TCsqKeyValue::getMainKey, userId)
			.eq(TCsqKeyValue::getType, code));
	}

	@Override
	public List<TCsqKeyValue> selectByKeyAndTypeDesc(Long userId, int code) {
		return MybatisPlus.getInstance().findAll(new TCsqKeyValue(), baseBUild()
			.eq(TCsqKeyValue::getMainKey, userId)
			.eq(TCsqKeyValue::getType, code)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqKeyValue::getCreateTime)));
	}

	@Override
	public int save(TCsqKeyValue... build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public TCsqKeyValue selectByKeyAndTypeAndValue(Long userId, int code, String toString) {
		return MybatisPlus.getInstance().findOne(new TCsqKeyValue(), baseBUild()
			.eq(TCsqKeyValue::getMainKey, userId)
			.eq(TCsqKeyValue::getType, code)
			.eq(TCsqKeyValue::getTheValue, toString)
		);
	}
}
