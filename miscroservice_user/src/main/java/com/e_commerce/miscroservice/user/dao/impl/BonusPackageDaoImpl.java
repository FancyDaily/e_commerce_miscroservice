package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.user.po.TBonusPackage;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.BonusPackageDao;
import org.springframework.stereotype.Component;

@Component
public class BonusPackageDaoImpl implements BonusPackageDao {
    @Override
    public int insert(TBonusPackage bonusPackage) {
        return MybatisPlus.getInstance().save(bonusPackage);
    }

    @Override
    public int updateByPrimaryKey(TBonusPackage bonusPackage) {
        return MybatisPlus.getInstance().update(bonusPackage,new MybatisPlusBuild(TBonusPackage.class)
        .eq(TBonusPackage::getId,bonusPackage.getId()));
    }

    @Override
    public TBonusPackage info(Long bonusId) {
        return MybatisPlus.getInstance().findOne(new TBonusPackage(),new MybatisPlusBuild(TBonusPackage.class)
        .eq(TBonusPackage::getId,bonusId)
        .eq(TBonusPackage::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TBonusPackage selectByPrimaryKey(Long bonusPackageId) {
        return MybatisPlus.getInstance().findOne(new TBonusPackage(),new MybatisPlusBuild(TBonusPackage.class)
        .eq(TBonusPackage::getId,bonusPackageId));
    }

    @Override
    public boolean isMine(Long id, Long bonusPackageId) {
        return !MybatisPlus.getInstance().findAll(new TBonusPackage(),new MybatisPlusBuild(TBonusPackage.class)
        .eq(TBonusPackage::getUserId,id)
        .eq(TBonusPackage::getId,bonusPackageId)
                .eq(TBonusPackage::getIsValid,AppConstant.IS_VALID_YES)).isEmpty();
    }
}
