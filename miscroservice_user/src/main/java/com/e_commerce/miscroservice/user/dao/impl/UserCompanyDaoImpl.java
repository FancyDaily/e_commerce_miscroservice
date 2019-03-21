package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUserCompany;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserCompanyDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    /**
     * 根据多个用户id时间倒序查找UserCompany记录
     * @param ids
     * @return
     */
    @Override
    public List<TUserCompany> queryByUserIdsDESC(Long... ids) {
        List<Long> userIds = new ArrayList<>();
        for(Long id:ids) {
            if(id!=null) {
                userIds.add(id);
            }
        }
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
                .in(TUserCompany::getUserId,userIds)
                .eq(TUserCompany::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUserCompany::getCreateTime)));
    }

    /**
     * 根据用户id、组织内角色查询UserCompany记录
     * @param id
     * @param role
     * @return
     */
    @Override
    public List<TUserCompany> selectByUserIdAndCompanyjob(Long id, Integer role) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getUserId,id)
        .eq(TUserCompany::getCompanyJob,role)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据用户id、组织id查询UserCompany记录
     * @param id
     * @param companyId
     * @return
     */
    @Override
    public List<TUserCompany> selectByUserIdAndCompanyId(Long id, Long companyId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getUserId, id)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 更新
     * @param userCompany
     */
    @Override
    public int updateByPrimaryKey(TUserCompany userCompany) {
        return MybatisOperaterUtil.getInstance().update(userCompany,new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getId,userCompany.getId())
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 插入
     * @param userCompany
     * @return
     */
    @Override
    public int insert(TUserCompany userCompany) {
        return MybatisOperaterUtil.getInstance().save(userCompany);
    }

    @Override
    public TUserCompany getOwnCompanyIdByUser(Long userId) {
        return MybatisOperaterUtil.getInstance().findOne(new TUserCompany(), new MybatisSqlWhereBuild(TUserCompany.class)
                .eq(TUserCompany::getIsValid, AppConstant.IS_VALID_YES).eq(TUserCompany::getUserId, userId)
                .eq(TUserCompany::getCompanyJob,AppConstant.JOB_COMPANY_CREATER));
    }

    @Override
    public List<TUserCompany> listCompanyUser(Long companyId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(), new MybatisSqlWhereBuild(TUserCompany.class)
                .eq(TUserCompany::getState, 2).eq(TUserCompany::getIsValid, AppConstant.IS_VALID_YES)
                .eq(TUserCompany::getCompanyId, companyId).isNotNull(TUserCompany::getGroupId));
    }

    @Override
    public long countGroupUser(Long groupId) {
        return  MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TUserCompany.class)
                .eq(TUserCompany::getIsValid, AppConstant.IS_VALID_YES).eq(TUserCompany::getState, 2)
                .eq(TUserCompany::getGroupId, groupId));
    }
}

