package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqFund;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-11 16:50
 */
public interface CsqFundDao {
	int insert(TCsqFund... csqFund);

	List<TCsqFund> selectByUserId(Long userId);

	List<TCsqFund> selectByUserIdAndStatus(Long userId, int val);

	TCsqFund selectByPrimaryKey(Long fundId);

	int update(TCsqFund csqFund);

	List<TCsqFund> selectByUserIdInStatusDesc(Long userId, Integer... option);

	List<TCsqFund> selectInIds(List<Long> fundIds);

	List<TCsqFund> selectByUserIdAndInStatus(Long userId, List<Integer> asList);

	List<TCsqFund> selectByUserIdAndNotEqStatus(Long userId, Integer status);

	TCsqFund selectByUserIdAndPrimaryKey(Long userId, Long fundId);

	List<TCsqFund> selectByUserIdInStatusDescPage(Long userId, Integer[] option, Integer pageNum, Integer pageSize);

	TCsqFund selectByExtend(String extend);

	int multiUpdate(List<TCsqFund> toUpdaterFunds);

	TCsqFund selectByName(String fundName);

	List<TCsqFund> selectAll();

	MybatisPlusBuild baseBuild();

	List<TCsqFund> selectInNames(List<String> names);

	List<TCsqFund> selectWithBuild(MybatisPlusBuild baseBuild);

	List<TCsqFund> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize);

	int batchUpdate(List<TCsqFund> toUpdateFunds);

	List<TCsqFund> selectInUserIds(List<Long> userIds);
}
