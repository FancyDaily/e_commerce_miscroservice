package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TCompany;

import java.util.List;

public interface CompanyDao {
    List<TCompany> selectExistUserCompany(String name, Long id, Integer corpCertStatusYes);

    TCompany selectLatestByUserId(Long id);

    List<TCompany> selectAllByUserId(Long id);

    int update(TCompany company);

    int insert(TCompany company);

    TCompany selectByPrimaryKey(Long companyId);
}
