package com.e_commerce.miscroservice.lpglxt_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
public interface LpglUserDao {
	TLpglUser selectByPrimaryKey(Long id);

	int updateByPrimaryKey(TLpglUser tCsqUser);

	int insert(TLpglUser... user);

	int insert(List<TLpglUser> user);

	List<TLpglUser> selectByVxOpenId(String openId);

	TLpglUser selectByUserTel(String telephone);

	List<TLpglUser> selectInIds(List<Long> userIds);

	List<TLpglUser> selectAll();

	int update(List<TLpglUser> asList);

	MybatisPlusBuild baseBuild();

	List<TLpglUser> selectByName(String name);

	List<TLpglUser> selectByBuild(MybatisPlusBuild baseBuild);

	List<TLpglUser> selectByBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize);

	List<TLpglUser> selectByName(String searchParam, boolean isLike);

	List<TLpglUser> selectInNames(List<String> userNames);

	List<TLpglUser> selectByGroupId(Long groupId);
}
