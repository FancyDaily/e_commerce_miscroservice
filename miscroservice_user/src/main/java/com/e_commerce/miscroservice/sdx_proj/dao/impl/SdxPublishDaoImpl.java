package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxPublishDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxPublish;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:16
 */
@Component
public class SdxPublishDaoImpl implements SdxPublishDao {

	@Override
	public TSdxPublish selectByMainKey(int mainKey) {
		return MybatisPlus.getInstance().findOne(new TSdxPublish(), new MybatisPlusBuild(TSdxPublish.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TSdxPublish::getMainKey, mainKey));
	}

	@Override
	public int update(TSdxPublish... tCsqPublish) {
		List<Long> updaterIds = Arrays.stream(tCsqPublish)
			.map(TSdxPublish::getId)
			.collect(Collectors.toList());
		if(updaterIds.size() == 1) {
			return update(tCsqPublish);
		}
		return MybatisPlus.getInstance().update(tCsqPublish, new MybatisPlusBuild(TSdxPublish.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.in(TSdxPublish::getId, updaterIds));
	}

	@Override
	public int update(TSdxPublish tCsqPublish) {
		return MybatisPlus.getInstance().update(tCsqPublish, new MybatisPlusBuild(TSdxPublish.class)
			.eq(TCsqOrder::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TSdxPublish::getId, tCsqPublish.getId()));
	}

	@Override
	public int insert(TSdxPublish... tCsqPublishes) {
		return MybatisPlus.getInstance().save(tCsqPublishes);
	}
}
