package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TCompany;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.CompanyDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyDaoImpl implements CompanyDao {
    @Override
    public List<TCompany> selectExistUserCompany(String name, Long id, Integer corpCertStatusYes) {
        return MybatisPlus.getInstance().findAll(new TCompany(),new MybatisPlusBuild(TCompany.class)
        .eq(TCompany::getName,name)
        .neq(TCompany::getUserId,id)
        .eq(TCompany::getStatus,corpCertStatusYes)
        .eq(TCompany::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TCompany selectLatestByUserId(Long id) {
        return MybatisPlus.getInstance().findOne(new TCompany(),new MybatisPlusBuild(TCompany.class)
        .eq(TCompany::getUserId,id)
        .eq(TCompany::getIsValid,AppConstant.IS_VALID_YES)
        .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCompany::getCreateTime)));
    }

    @Override
    public List<TCompany> selectAllByUserId(Long id) {
        return MybatisPlus.getInstance().findAll(new TCompany(),new MybatisPlusBuild(TCompany.class)
                .eq(TCompany::getUserId,id)
                .eq(TCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public int update(TCompany company) {
        return MybatisPlus.getInstance().update(company,new MybatisPlusBuild(TCompany.class)
        .eq(TCompany::getId,company.getId()));
    }

    @Override
    public int insert(TCompany company) {
        return MybatisPlus.getInstance().save(company);
    }

    @Override
    public TCompany selectByPrimaryKey(Long companyId) {
        return MybatisPlus.getInstance().findOne(new TCompany(),new MybatisPlusBuild(TCompany.class)
        .eq(TCompany::getId,companyId)
        .eq(TCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public int updateByPrimaryKey(TCompany company) {
        return MybatisPlus.getInstance().update(company,new MybatisPlusBuild(TCompany.class)
        .eq(TCompany::getId,company.getId()));
    }
}
