package com.e_commerce.miscroservice.product.dao;



import com.e_commerce.miscroservice.order.po.TServiceDescribe;
import java.util.List;

public interface ProductDescDao {
	/**
	 * 插入一条记录
	 * @param describe
	 * @return
	 */
	int insert(TServiceDescribe describe);

	/**
	 * 插入多条记录
	 * @param list
	 * @return
	 */
	int batchInsert(List<TServiceDescribe> list);

	/**
	 * 根据商品ID查询出多条商品详情
	 * @param id
	 * @return
	 */
	List<TServiceDescribe> selectDescByServiceId(Long id);
}
