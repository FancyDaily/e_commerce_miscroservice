package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TBonusPackage;

public interface BonusPackageDao {
    int insert(TBonusPackage bonusPackage);

    int updateByPrimaryKey(TBonusPackage bonusPackage);

    TBonusPackage info(Long bonusId);

    TBonusPackage selectByPrimaryKey(Long bonusPackageId);

    boolean isMine(Long id, Long bonusPackageId);
}
