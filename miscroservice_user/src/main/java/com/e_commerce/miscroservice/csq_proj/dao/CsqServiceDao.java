package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
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

	List<TCsqService> selectLikeByPubKeys(String a);

	List<TCsqService> selectLikeByPubKeysAndUserIdNeq(String a, Long userId);

	MybatisPlusBuild getBaseBuild();

	List<TCsqService> selectInIdsOrInFundIds(List<Long> serviceIds, List<Long> fundIds);

	List<TCsqService> selectInFundIds(List<Long> fundIds);

	List<TCsqService> selectInIdsPage(List<Long> csqServiceIds, Integer pageNum, Integer pageSize);

	List<TCsqService> selectMinePage(Integer pageNum, Integer pageSize, Long userId);

	List<TCsqService> selectAllPage(Long userId, Integer pageNum, Integer pageSize);

	List<TCsqService> selectInIdsOrInFundIdsPage(Integer pageNum, Integer pageSize, List<Long> serviceIds, List<Long> fundIds);

	List<TCsqService> selectLikeByPubKeysAndUserIdNeqAndFundStatus(String a, Long userId);

	int multiUpdate(List<TCsqService> oldServices);

	List<TCsqService> selectInExtends(List<Long> collect);

	List<TCsqService> selectWithBuild(MybatisPlusBuild baseBuild);

	List<TCsqService> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize);

	List<TCsqService> selectByType(int code);

	TCsqService selectByName(String name);

	List<TCsqService> selectByName(String searchParam, Boolean isFuzzySearch);

	List<TCsqService> selectByNamePage(String searchParam, Boolean isFuzzySearch, Integer pageNum, Integer pageSize);

	List<TCsqService> selectAll(Long userIds);
}
