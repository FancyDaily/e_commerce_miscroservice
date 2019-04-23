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

    List<TUser> selectInUserIds(String[] split);

    List<TUser> selectByTelephoneAndJurisdiction(String telephone, Integer jurisdictionNormal);

    List<TUser> selectByName(String param);

    List<TUser> selectByJurisdictionAndCreateTimeDesc(Integer jurisdictionNormal);

    List<TUser> selectByNameAndJurisdictionCreateTimeDesc(String param, Integer jurisdictionNormal);

    List<TUser> selectByUserAccountAndPasswordAndJurisdiction(String account, String password, Integer jurisdictionAdmin);

    List<TUser> selectByUserTelAndJurisAndIsCompany(String userTel, Integer jurisdictionNormal, Integer isCompanyAccountYes);
}
