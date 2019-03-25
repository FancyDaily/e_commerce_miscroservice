package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TCompany;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.CompanyDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyDaoImpl implements CompanyDao {
    @Override
    public List<TCompany> selectExistUserCompany(String name, Long id, Integer corpCertStatusYes) {
        return MybatisOperaterUtil.getInstance().finAll(new TCompany(),new MybatisSqlWhereBuild(TCompany.class)
        .eq(TCompany::getName,name)
        .neq(TCompany::getUserId,id)
        .eq(TCompany::getStatus,corpCertStatusYes)
        .eq(TCompany::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TCompany selectLatestByUserId(Long id) {
        return MybatisOperaterUtil.getInstance().findOne(new TCompany(),new MybatisSqlWhereBuild(TCompany.class)
        .eq(TCompany::getUserId,id)
        .eq(TCompany::getIsValid,AppConstant.IS_VALID_YES)
        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TCompany::getCreateTime)));
    }

    @Override
    public List<TCompany> selectAllByUserId(Long id) {
        return MybatisOperaterUtil.getInstance().finAll(new TCompany(),new MybatisSqlWhereBuild(TCompany.class)
                .eq(TCompany::getUserId,id)
                .eq(TCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public int update(TCompany company) {
        return MybatisOperaterUtil.getInstance().update(company,new MybatisSqlWhereBuild(TCompany.class)
        .eq(TCompany::getId,company.getId()));
    }

    @Override
    public int insert(TCompany company) {
        return MybatisOperaterUtil.getInstance().save(company);
    }

    @Override
    public TCompany selectByPrimaryKey(Long companyId) {
        return MybatisOperaterUtil.getInstance().findOne(new TCompany(),new MybatisSqlWhereBuild(TCompany.class)
        .eq(TCompany::getId,companyId)
        .eq(TCompany::getIsValid,AppConstant.IS_VALID_YES));
    }
}
