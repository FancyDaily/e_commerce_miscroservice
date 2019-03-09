package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserCompany;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserCompanyDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserCompanyDaoImpl implements UserCompanyDao {
    /**
     * 根据用户id时间倒序查找UserCompany记录
     * @param userId
     * @return
     */
    @Override
    public List<TUserCompany> queryByUserIdDESC(Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getUserId,userId)
        .eq(TUserCompany::getIsValid, AppConstant.IS_VALID_YES)
        .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserCompany::getCreateTime)));
    }

    /**
     * 根据组织id获取UserCompany记录
     * @param companyId
     * @return
     */
    @Override
    public List<TUserCompany> selectByCompanyId(Long companyId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据组织id查找组织账号相关UserCompany记录
     * @param companyId
     * @return
     */
    @Override
    public List<TUserCompany> findRecordOfCompanyAccount(Long companyId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getCompanyJob,AppConstant.JOB_COMPANY_CREATER)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }
}
