package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionRoleDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistionRole;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpgPositionRoleDaoImpl implements LpglPositionRoleDao {

	@Override
	public List<TLpglPosistionRole> selectInRoleIds(List<Long> roleIds) {
		return MybatisPlus.getInstance().findAll(new TLpglPosistionRole(), new MybatisPlusBuild(TLpglPosistionRole.class)
			.eq(TLpglPosistionRole::getIsValid, AppConstant.IS_VALID_YES)
			.in(TLpglPosistionRole::getRoleId, roleIds)
		);
	}
}
