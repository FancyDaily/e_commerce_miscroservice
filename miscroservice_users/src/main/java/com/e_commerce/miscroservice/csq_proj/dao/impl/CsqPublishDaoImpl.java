package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPublishDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.csq_proj.po.TCsqPublish;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:16
 */
@Component
public class CsqPublishDaoImpl implements CsqPublishDao {

	@Override
	public TCsqPublish selectByMainKey(int mainKey) {
		return MybatisPlus.getInstance().findOne(new TCsqPublish(), new MybatisPlusBuild(TCsqPublish.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqPublish::getMainKey, mainKey));
	}

	@Override
	public int update(TCsqPublish... tCsqPublish) {
		List<Long> updaterIds = Arrays.stream(tCsqPublish)
			.map(TCsqPublish::getId)
			.collect(Collectors.toList());
		if(updaterIds.size() == 1) {
			return update(tCsqPublish);
		}
		return MybatisPlus.getInstance().update(tCsqPublish, new MybatisPlusBuild(TCsqPublish.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.in(TCsqPublish::getId, updaterIds));
	}

	@Override
	public int update(TCsqPublish tCsqPublish) {
		return MybatisPlus.getInstance().update(tCsqPublish, new MybatisPlusBuild(TCsqPublish.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqPublish::getId, tCsqPublish.getId()));
	}

	@Override
	public int insert(TCsqPublish... tCsqPublishes) {
		return MybatisPlus.getInstance().save(tCsqPublishes);
	}
}
