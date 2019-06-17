package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPublishDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqPublish;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:16
 */
@Component
public class CsqPublishDaoImpl implements CsqPublishDao {
	@Override
	public TCsqPublish selectByMainKey(int mainKey) {
		return MybatisPlus.getInstance().findOne(new TCsqPublish(), new MybatisPlusBuild(TCsqPublish.class)
			.eq(TCsqPublish::getMain_key, mainKey)
			.eq(TCsqPublish::getIsValid, AppConstant.IS_VALID_YES));
	}
}
