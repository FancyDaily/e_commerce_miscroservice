package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author 马晓晨
 * @date 2019/3/4
 */
@Repository
public class ProductDaoImpl implements ProductDao {
	@Autowired
	ProductMapper productMapper;


	@Override
	public int insert(TService service) {
		return MybatisOperaterUtil.getInstance().save(service);
	}
}
