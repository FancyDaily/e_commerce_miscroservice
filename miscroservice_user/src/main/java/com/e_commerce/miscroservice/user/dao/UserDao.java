package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUser;

import java.util.List;

public interface UserDao {

    TUser selectByPrimaryKey(Long id);

    List<TUser> queryByIds(List<Long> idList);

	int updateByPrimaryKey(TUser tUser);

    TUser info(Long userId);

    List<TUser> queryUsersByTelephone(String telephone, Integer application);

    List<TUser> selectByInviteCode(String inviteCode, Integer application);

    List<TUser> selectUserTelByJurisdictionAndIsCompany(String telephone, Integer jurisdictionNormal, Integer isCompanyAccountYes, Integer application);

    List<TUser> selectByTelephone(String telephone, Integer application);

    Long insert(TUser user, Integer application);

    List<TUser> selectByVxOpenId(String openId, Integer application);

    List<TUser> selectUserTelByJurisdiction(String telephone, Integer jurisdictionNormal, Integer application);

    List<TUser> selectByUserTelByPasswordByIsCompanyAccYes(String telephone, String password, Integer isCompanyAccount, Integer application);

    List<TUser> selectByTelephoneInInIds(String param, List<Long> userIds, Integer application);

    List<TUser> selectByNameAndTelephoneLikeSkillInIds(String name, String telephone, String skill, List<Long> idList, Integer application);

    List<TUser> selectInUserIds(String[] split, Integer application);

    List<TUser> selectByTelephoneAndJurisdiction(String telephone, Integer jurisdictionNormal, Integer application);

    List<TUser> selectByName(String param, Integer application);

    List<TUser> selectByJurisdictionAndCreateTimeDesc(Integer jurisdictionNormal, Integer application);

    List<TUser> selectByNameAndJurisdictionCreateTimeDesc(String param, Integer jurisdictionNormal, Integer application);

    List<TUser> selectByUserAccountAndPasswordAndJurisdiction(String account, String password, Integer jurisdictionAdmin, Integer application);

    List<TUser> selectByUserTelAndJurisAndIsCompany(String userTel, Integer jurisdictionNormal, Integer isCompanyAccountYes, Integer application);

    TUser queryDoppelganger(TUser user, Integer application);

    List<TUser> selectUserByName(String name, Integer application);

    List<TUser> selectUserByTelephone(String telephone, Integer application);

    TUser selectByUserAccountAndPassword(String telephone, String password, int application);
}
