package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.product.dao.ProductDescDao;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductDescDaoImpl implements ProductDescDao {

	@Override
	public int insert(TServiceDescribe describe) {
		return MybatisOperaterUtil.getInstance().save(describe);
	}

	//TODO 后期插件加入
	@Override
	public int batchInsert(List<TServiceDescribe> list) {
		return 0;
	}

}
