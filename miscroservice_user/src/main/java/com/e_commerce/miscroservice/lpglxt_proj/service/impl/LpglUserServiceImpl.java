package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglUserService;
import org.springframework.stereotype.Service;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglUserServiceImpl implements LpglUserService {
	@Override
	public TLpglUser findOne(Long id) {
		TLpglUser tLpglUser = MybatisPlus.getInstance().findOne(new TLpglUser(),new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getId,id)
			.eq(TLpglUser::getDeletedFlag,0)
		);
		return tLpglUser;
	}
}
