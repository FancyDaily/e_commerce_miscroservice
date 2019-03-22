package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.UserDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    @Override
    public TUser selectByPrimaryKey(Long id) {
        return MybatisOperaterUtil.getInstance().findOne(new TUser(), new MybatisSqlWhereBuild(TUser.class)
                .eq(TUser::getId, id)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    /**
     * 根据主键集合查找
     *
     * @param idList
     * @return
     */
    @Override
    public List<TUser> queryByIds(List<Long> idList) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(), new MybatisSqlWhereBuild(TUser.class)
                .in(TUser::getId, idList)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int updateByPrimaryKey(TUser tUser) {
        return MybatisOperaterUtil.getInstance().update(tUser, new MybatisSqlWhereBuild(TUser.class)
                .eq(TUser::getId, tUser.getId()));
    }

    @Override
    public TUser info(Long userId) {
        return MybatisOperaterUtil.getInstance().findOne(new TUser(), new MybatisSqlWhereBuild(TUser.class)
                .eq(TUser::getId, userId)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    public List<TUser> queryUsersByTelephone(String telephone) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(), new MybatisSqlWhereBuild(TUser.class)
                .eq(TUser::getUserTel, telephone)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByInviteCode(String inviteCode) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(),new MybatisSqlWhereBuild(TUser.class)
        .eq(TUser::getInviteCode,inviteCode)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectUserTelByJurisdictionAndIsCompany(String telephone, Integer jurisdictionNormal, Integer isCompanyAccountYes) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(),new MybatisSqlWhereBuild(TUser.class)
        .eq(TUser::getUserTel,telephone)
        .eq(TUser::getJurisdiction,jurisdictionNormal)
        .eq(TUser::getIsCompanyAccount,isCompanyAccountYes)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByTelephone(String telephone) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(),new MybatisSqlWhereBuild(TUser.class)
        .eq(TUser::getUserTel,telephone)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public Long insert(TUser user) {
        MybatisOperaterUtil.getInstance().save(user);
        return user.getId();
    }

    @Override
    public List<TUser> selectByVxOpenId(String openId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(),new MybatisSqlWhereBuild(TUser.class)
        .eq(TUser::getVxOpenId,openId)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectUserTelByJurisdiction(String telephone, Integer jurisdictionNormal) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(),new MybatisSqlWhereBuild(TUser.class)
        .eq(TUser::getUserTel,telephone)
        .eq(TUser::getJurisdiction,jurisdictionNormal)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据手机号、密码、账号性质查找用户记录
     * @param telephone
     * @param password
     * @param isCompanyAccount
     * @return
     */
    @Override
    public List<TUser> selectByUserTelByPasswordByIsCompanyAccYes(String telephone, String password, Integer isCompanyAccount) {
        return MybatisOperaterUtil.getInstance().finAll(new TUser(),new MybatisSqlWhereBuild(TUser.class)
        .eq(TUser::getUserTel,telephone)
        .eq(TUser::getPassword,password)
        .eq(TUser::getIsCompanyAccount,isCompanyAccount)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByTelephoneInInIds(String param, List<Long> userIds) {
        MybatisSqlWhereBuild build = new MybatisSqlWhereBuild(TUser.class)
                .in(TUser::getId, userIds)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TUser::getCreateTime));
        if(param!=null) {
            build.eq(TUser::getUserTel,param);
        }

        return MybatisOperaterUtil.getInstance().finAll(new TUser(),build);
    }

    /**
     * 获取该账号的分身
     *
     * @param user
     * @return
     */
    @Override
    public TUser queryDoppelganger(TUser user) {
        TUser result = null;
        List<TUser> users = queryUsersByTelephone(user.getUserTel());

        if (AppConstant.AUTH_TYPE_CORP.equals(user.getAuthenticationType()) && AppConstant.AUTH_STATUS_YES.equals(user.getAuthenticationStatus())) {
            if (AppConstant.IS_COMPANY_ACCOUNT_YES.equals(user.getIsCompanyAccount())) { //目的是获取组织账号的个人账号
                for (TUser theUser : users) {
                    if (AppConstant.IS_COMPANY_ACCOUNT_NO.equals(theUser.getIsCompanyAccount())) {
                        result = theUser;
                    }
                }
            } else {    //目的是获取个人账号的组织账号
                for (TUser theUser : users) {
                    if (AppConstant.IS_COMPANY_ACCOUNT_YES.equals(theUser.getIsCompanyAccount())) {
                        result = theUser;
                    }
                }
            }
        }

        return result;
    }

}





