package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUser;

import java.util.List;

public interface UserDao {

    TUser selectByPrimaryKey(Long id);

    List<TUser> queryByIds(List<Long> idList);

	int updateByPrimaryKey(TUser tUser);

    TUser info(Long userId);

    TUser queryDoppelganger(TUser user);

    List<TUser> queryUsersByTelephone(String telephone);

    List<TUser> selectByInviteCode(String inviteCode);

    List<TUser> selectUserTelByJurisdictionAndIsCompany(String telephone, Integer jurisdictionNormal, Integer isCompanyAccountYes);

    List<TUser> selectByTelephone(String telephone);

    Long insert(TUser user);

    List<TUser> selectByVxOpenId(String openId);

    List<TUser> selectUserTelByJurisdiction(String telephone, Integer jurisdictionNormal);

    List<TUser> selectByUserTelByPasswordByIsCompanyAccYes(String telephone, String password, Integer isCompanyAccountYes);

    List<TUser> selectByTelephoneInInIds(String param, List<Long> userIds);

    List<TUser> selectByNameAndTelephoneLikeSkillInIds(String name, String telephone, String skill, List<Long> idList);

    List<TUser> selectUserByName(String name);

    List<TUser> selectUserByTelephone(String telephone);
}
