package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TCompany;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.user.vo.StrServiceView;
import com.e_commerce.miscroservice.user.vo.StrUserCompanyView;

public interface CompanyService {
    /**
     * 加入的组织列表
     * @param user
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    QueryResult<StrUserCompanyView> getCompanyList(TUser user, Long userId, Integer pageNum, Integer pageSize);

    QueryResult<StrServiceView> getActivityList(Long companyId, Integer pageNum, Integer pageSize);

    QueryResult<StrServiceView> getMyActivityList(Long userId, Long companyId, Integer pageNum, Integer pageSize);

    /**
     * 根据主键查询TCompany记录
     * @param id
     * @return
     */
    TCompany companyInfo(Long id);
}
