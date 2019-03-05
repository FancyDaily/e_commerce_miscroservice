package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.application.TService;

/**
 * @author 马晓晨
 * @date 2019/3/4
 */
public interface ProductDao {
	/**
	 * 插入一条记录
	 * @param service
	 * @return
	 */
	int insert(TService service);
}
