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

	@Override
	public List<TCsqKeyValue> selectByKeyAndType(Long userId, int code) {
		return MybatisPlus.getInstance().findAll(new TCsqKeyValue(), new MybatisPlusBuild(TCsqKeyValue.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqKeyValue::getMainKey, userId)
			.eq(TCsqKeyValue::getType, code));
	}

	@Override
	public List<TCsqKeyValue> selectByKeyAndTypeDesc(Long userId, int code) {
		return MybatisPlus.getInstance().findAll(new TCsqKeyValue(), new MybatisPlusBuild(TCsqKeyValue.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqKeyValue::getMainKey, userId)
			.eq(TCsqKeyValue::getType, code)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqKeyValue::getCreateTime)));
	}

	@Override
	public int save(TCsqKeyValue... build) {
		return MybatisPlus.getInstance().save(build);
	}
}
