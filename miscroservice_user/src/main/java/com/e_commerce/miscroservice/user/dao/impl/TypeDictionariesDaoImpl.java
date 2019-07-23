package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TTypeDictionaries;
import com.e_commerce.miscroservice.commons.enums.application.DictionaryEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.TypeDictionariesDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeDictionariesDaoImpl implements TypeDictionariesDao {
    @Override
    public List<TTypeDictionaries> selectInviteRecords(Long mineId, Long inviterId) {
        return MybatisPlus.getInstance().findAll(new TTypeDictionaries(),new MybatisPlusBuild(TTypeDictionaries.class)
                .eq(TTypeDictionaries::getEntityId,mineId)
                .eq(TTypeDictionaries::getTargetId,inviterId)
        .eq(TTypeDictionaries::getType, DictionaryEnum.INVITER.getType())
        .eq(TTypeDictionaries::getSubType,DictionaryEnum.INVITER.getSubType())
        .eq(TTypeDictionaries::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TTypeDictionaries dictionaries) {
        return MybatisPlus.getInstance().save(dictionaries);
    }

    @Override
    public List<TTypeDictionaries> selectByIdAndIsValid(Long scene, String isValid) {
        return MybatisPlus.getInstance().findAll(new TTypeDictionaries(),new MybatisPlusBuild(TTypeDictionaries.class)
        .eq(TTypeDictionaries::getId,scene)
        .eq(TTypeDictionaries::getIsValid,AppConstant.IS_VALID_YES));
    }
}
