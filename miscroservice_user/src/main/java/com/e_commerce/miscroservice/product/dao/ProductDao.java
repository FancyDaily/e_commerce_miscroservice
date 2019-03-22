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

	/**
	 * 获取商品详情
	 * @param serviceId 商品ID
	 * @return 详情list（多张图片多个desc）
	 */
	List<TServiceDescribe> getProductDesc(Long serviceId);

	/**
	 * 根据用户查询所有的求助服务
	 * @param userId 当前用户ID
	 * @param pageNum 页数
	 * @param pageSize 每页数量
	 * @param type 类型 1、求助 2、服务
	 * @return
	 */
	List<TService> getListProductByUserId(Long userId, Integer pageNum, Integer pageSize, Integer type);

	/**
	 * 查看一个人的所有服务订单
	 * @param userId
	 * @return
	 */
	List<TService> selectUserServ(Long userId);

	/**
	 * 批量更新订单
	 * @param serviceList
	 * @param serviceIdList
	 * @return
	 */
	long updateServiceByList(List<TService> serviceList, List<Long> serviceIdList);
}
