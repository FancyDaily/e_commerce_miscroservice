package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.csq_proj.po.TCsqService;

import java.util.List;

/**
 * @Description TODO
 * @ClassName CsqServiceDao
 * @Auhor huangyangfeng
 * @Date 2019-06-16 12:18
 * @Version 1.0
 */
public interface CsqServiceDao {
	/**
	 * 查找项目
	 * @param serviceId
	 * @return
	 */
	TCsqService findOne(Long serviceId);

	/**
	 * 批量查询项目
	 * @param serviceIdList
	 * @return
	 */
	List<TCsqService> findAll(List<Long> serviceIdList);

	List<TCsqService> selectInIds(List<Long> csqServiceIds);

	/**
	 * 插入记录
	 * @param service
	 * @return
	 */
	int insert(TCsqService... service);

	/**
	 * 查找所有
	 * @return
	 */
	List<TCsqService> selectAll();

	/**
	 * 查找我的所有
	 * @param userId
	 * @return
	 */
	List<TCsqService> selectMine(Long userId);

	TCsqService selectByPrimaryKey(Long serviceId);

	int update(TCsqService csqService);

	TCsqService selectByFundId(Long fundId);

	int updateByFundId(TCsqService build);

	List<TCsqService> selectByNameAndUserId(String name, Long userId);
}
