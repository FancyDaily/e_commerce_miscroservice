package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;

import java.util.List;

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

	/**
	 * 根据主键id查询service
	 * @param id
	 * @return
	 */
	TService selectByPrimaryKey(Long id);

	/**
	 * 根据主键进行更新
	 * @param lowerFrameService
	 * @return
	 */
	int updateByPrimaryKeySelective(TService lowerFrameService);

	/**
	 * 获取用户最新一条求助或服务的ID
	 * @param userId 用户ID
	 * @param type 类型 1、求助 2、服务
	 * @return 可能为null
	 */
	TService selectUserNewOneRecord(Long userId, Integer type);

	/**
	 * 获取商品信息根据商品的idList
	 * @param productIds 商品的ID
	 * @return 商品的list
	 */
	List<TService> selectListByIds(List<Long> productIds);

	/**
	 * 根据商品ID的list获取商品详情list
	 * @param productIds 商品ID的list
	 * @return 商品详情的list
	 */
	List<TServiceDescribe> getListProductDesc(List<Long> productIds);
}
