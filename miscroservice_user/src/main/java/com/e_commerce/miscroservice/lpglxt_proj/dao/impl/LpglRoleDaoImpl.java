package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglRoleDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglRole;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglRoleDaoImpl implements LpglRoleDao {

	@Override
	public List<TLpglRole> selectInNames(List<String> roleNames) {
		return MybatisPlus.getInstance()
			.findAll(new TLpglRole(), new MybatisPlusBuild(TLpglRole.class)
				.eq(TLpglRole::getIsValid, AppConstant.IS_VALID_YES)
				.in(TLpglRole::getRoleName, roleNames)
			);
	}
}
