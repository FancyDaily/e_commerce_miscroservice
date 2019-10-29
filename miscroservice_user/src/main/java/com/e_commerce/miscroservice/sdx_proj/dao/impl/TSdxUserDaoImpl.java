package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.e_commerce.miscroservice.csq_proj.dao.CsqUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-25 14:02
 */
@Component
public class TSdxUserDaoImpl implements TSdxUserDao {

	@Autowired
	private CsqUserDao csqUserDao;

	@Override
	public TCsqUser findById(Long userId) {
		/*return MybatisPlus.getInstance().findOne(new TCsqUser(), new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getIsValid, AppConstant.IS_VALID_YES)
		);*/
		return csqUserDao.selectByPrimaryKey(userId);
	}

	@Override
	public int update(TCsqUser byId) {
		/*return MybatisPlus.getInstance().update(byId, new MybatisPlusBuild(TCsqUser.class)
			.eq(TCsqUser::getId, byId.getId())
		);*/
		return csqUserDao.update(Arrays.asList(byId));
	}

	@Override
	public List<TCsqUser> selectInIds(List<Long> userIds) {
		return csqUserDao.selectInIds(userIds);
	}

	@Override
	public Map<Long, List<TCsqUser>> groupingByIdInIds(List<Long> userIds) {
		if(userIds == null || userIds.isEmpty()) return new HashMap<>();
		List<TCsqUser> tCsqUsers = selectInIds(userIds);
		return tCsqUsers.stream()
			.collect(Collectors.groupingBy(TCsqUser::getId));
	}

}
