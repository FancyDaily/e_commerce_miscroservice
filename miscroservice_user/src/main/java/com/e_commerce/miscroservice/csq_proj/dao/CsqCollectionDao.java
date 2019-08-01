package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqUserCollection;

import java.util.List;

public interface CsqCollectionDao {
	/**
	 * 查找收藏表
	 * @param serviceId
	 * @param userId
	 * @return
	 */
	TCsqUserCollection findOne(Long serviceId, Long userId);

	/**
	 * 添加收藏
	 * @param in
	 * @return
	 */
	Integer insert(TCsqUserCollection in);

	/**
	 * 取消收藏
	 * @param in
	 * @return
	 */
	Integer update(TCsqUserCollection in);

	/**
	 * 收藏列表
	 * * @param userId
	 * @return
	 */
	List<TCsqUserCollection> findAll( Long userId);

	/**
	 * 收藏列表page
	 * @param valueOf
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<TCsqUserCollection> findAllPage(Long valueOf, Integer pageNum, Integer pageSize);
}
