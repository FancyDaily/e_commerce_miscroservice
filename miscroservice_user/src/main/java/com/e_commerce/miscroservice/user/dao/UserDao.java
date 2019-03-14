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
}
