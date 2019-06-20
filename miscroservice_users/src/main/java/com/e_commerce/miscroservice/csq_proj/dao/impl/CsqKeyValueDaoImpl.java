package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqKeyValueDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqKeyValue;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 17:39
 */
@Component
public class CsqKeyValueDaoImpl implements CsqKeyValueDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqKeyValue.class)
			.eq(TCsqKeyValue::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqKeyValue> selectByKeyAndType(Long userId, int code) {
		return MybatisPlus.getInstance().finAll(new TCsqKeyValue(), baseWhereBuild()
			.eq(TCsqKeyValue::getKey, userId)
			.eq(TCsqKeyValue::getType, code));
	}

	@Override
	public List<TCsqKeyValue> selectByKeyAndTypeDesc(Long userId, int code) {
		return MybatisPlus.getInstance().finAll(new TCsqKeyValue(), baseWhereBuild()
			.eq(TCsqKeyValue::getKey, userId)
			.eq(TCsqKeyValue::getType, code)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqKeyValue::getCreateTime)));
	}
}
