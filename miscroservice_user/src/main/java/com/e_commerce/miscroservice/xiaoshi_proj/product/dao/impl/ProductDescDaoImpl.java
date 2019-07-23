package com.e_commerce.miscroservice.xiaoshi_proj.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.xiaoshi_proj.product.dao.ProductDescDao;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductDescDaoImpl implements ProductDescDao {

	@Override
	public int insert(TServiceDescribe describe) {
		return MybatisPlus.getInstance().save(describe);
	}

	@Override
	public int batchInsert(List<TServiceDescribe> list) {
		return MybatisPlus.getInstance().save(list);
	}

	@Override
	public List<TServiceDescribe> selectDescByServiceId(Long id) {
		return MybatisPlus.getInstance().findAll(new TServiceDescribe()
				, new MybatisPlusBuild(TServiceDescribe.class).eq(TServiceDescribe::getServiceId, id));
	}

}
