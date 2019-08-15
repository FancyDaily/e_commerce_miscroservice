package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOldUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TOldUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:25
 */
@Component
public class CsqOldUserDaoImpl implements CsqOldUserDao {
	@Override
	public int update(ArrayList<TOldUser> toUpdaters) {
		List<String> toupdateIds = toUpdaters.stream()
			.map(TOldUser::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(toUpdaters, new MybatisPlusBuild(TOldUser.class)
			.in(TOldUser::getId, toupdateIds));
	}

	@Override
	public List<TOldUser> selectAll() {
		return MybatisPlus.getInstance().findAll(new TOldUser(), new MybatisPlusBuild(TOldUser.class)
			.eq(TOldUser::getStatus, 1));
	}

	@Override
	public TOldUser selectByPrimaryKey(Long valueOf) {
		return MybatisPlus.getInstance().findOne(new TOldUser(), new MybatisPlusBuild(TOldUser.class)
			.eq(TOldUser::getId, valueOf));
	}

	@Override
	public List<TOldUser> selectInPrimaryKeys(List<Long> oldIds) {
		return MybatisPlus.getInstance().findAll(new TOldUser(), new MybatisPlusBuild(TOldUser.class)
			.in(TOldUser::getId));
	}
}
