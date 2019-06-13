package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.csq_proj.dao.CsqUserAuthDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserAuth;
import org.springframework.stereotype.Component;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-13 17:34
 */
@Component
public class CsqUserAuthDaoImpl implements CsqUserAuthDao {
	@Override
	public int insert(TCsqUserAuth... csqUserAuth) {
		return MybatisPlus.getInstance().save(csqUserAuth);
	}
}
