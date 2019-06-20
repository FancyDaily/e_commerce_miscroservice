package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.csq_proj.vo.CsqCollectionVo;

import java.util.List;

public interface CsqCollectionService {
	/**
	 * 收藏操作
	 * @param serviceId
	 * @param userId
	 */
	Object clickCollection(Long serviceId, Integer userId);

	/**
	 * 收藏列表
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	QueryResult<CsqCollectionVo> collectionList(Integer pageNum, Integer pageSize, Integer userId);

}
