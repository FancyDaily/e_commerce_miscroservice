package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserAuth;

import java.util.List;

public interface UserAuthDao {
    List<TUserAuth> findAllByCardId(String cardId);

    int insert(TUserAuth userAuth);

    List<TUserAuth> selectByUserId(Long id);
}
