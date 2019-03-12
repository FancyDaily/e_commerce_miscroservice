package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TGroup;

import java.util.List;

public interface GroupDao {
    List<TGroup> selectByCompanyIdAndAuth(Long companyId, Integer groupAuthDefault);
}
