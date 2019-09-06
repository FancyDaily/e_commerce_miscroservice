package com.e_commerce.miscroservice.csq_proj.dao;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
public interface CsqUserDao {

	TCsqUser selectByPrimaryKey(Long userId);

	List<TCsqUser> queryByIds(List<Long> idList);

	int updateByPrimaryKey(TCsqUser tCsqUser);

	int insert(TCsqUser... user);

	int insert(List<TCsqUser> user);

	List<TCsqUser> selectByVxOpenId(String openId);

	TCsqUser selectByVxOpenIdAndAccountType(String openid, Integer accountType);

	TCsqUser selectByUserTel(String telephone);

	TCsqUser selectByUserTelAndAccountType(String telephone, Integer accountType);

	List<TCsqUser> selectInIds(List<Long> userIds);

	TCsqUser selectByUserTelAndPasswordAndAccountType(String telephone, String password, Integer accountType);

	List<TCsqUser> selectAll();

	TCsqUser selectByOldId(String oldIdStr);

	int update(List<TCsqUser> asList);

	TCsqUser selectByNameAndNotNullUserTel(String name);

	MybatisPlusBuild baseBuild();

	TCsqUser selectByNameAndNotNullUserTelAndNullOpenid(String name);

	List<TCsqUser> selectByName(String name);

	List<TCsqUser> selectByBuild(MybatisPlusBuild baseBuild);

	List<TCsqUser> selectByBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize);

	List<TCsqUser> selectByName(String searchParam, boolean isLike);

	List<TCsqUser> selectInNames(List<String> userNames);
}
