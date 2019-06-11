package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.FundDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 16:50
 */
@Component
public class FunDaoImpl implements FundDao {

	@Override
	public int insert(TCsqFund... csqFund) {
		return MybatisPlus.getInstance().save(csqFund);
	}

	@Override
	public List<TCsqFund> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().finAll(new TCsqFund(), new MybatisPlusBuild(TCsqFund.class)
		.eq(TCsqFund::getUserId, userId)
		.eq(TCsqFund::getIsValid, AppConstant.IS_VALID_YES));
	}
}
