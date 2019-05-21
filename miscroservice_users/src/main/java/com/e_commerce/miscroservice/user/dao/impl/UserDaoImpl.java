package com.e_commerce.miscroservice.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    @Qualifier("userRedisTemplate")
    private HashOperations<String,String,String> userRedisTemplate;

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    @Override
    public TUser selectByPrimaryKey(Long id) {
        return MybatisPlus.getInstance().findOne(new TUser(), new MybatisPlusBuild(TUser.class)
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
        return MybatisPlus.getInstance().finAll(new TUser(), new MybatisPlusBuild(TUser.class)
                .in(TUser::getId, idList)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int updateByPrimaryKey(TUser tUser) {
        MybatisPlus.getInstance().update(tUser, new MybatisPlusBuild(TUser.class)
                .eq(TUser::getId, tUser.getId()));
//        UserUtil.flushRedisUser(tUser);
        if(tUser.getAuthenticationStatus()==null) {
            tUser = MybatisPlus.getInstance().findOne(new TUser(), new MybatisPlusBuild(TUser.class)
                    .eq(TUser::getId, tUser.getId()));
        }
        flushRedisUser(tUser);
        return 1;
    }

    private void flushRedisUser(TUser user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",user);

        userRedisTemplate.put(String.format(AppConstant.MINE_INFOS,user.getId()),String.valueOf(user.getId()),jsonObject.toJSONString());
    }

    @Override
    public TUser info(Long userId) {
        return MybatisPlus.getInstance().findOne(new TUser(), new MybatisPlusBuild(TUser.class)
                .eq(TUser::getId, userId)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> queryUsersByTelephone(String telephone,Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(), new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel, telephone)
                .eq(TUser::getApplication, application)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByInviteCode(String inviteCode, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getInviteCode,inviteCode)
                .eq(TUser::getApplication, application)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectUserTelByJurisdictionAndIsCompany(String telephone, Integer jurisdictionNormal, Integer isCompanyAccountYes, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel,telephone)
                .eq(TUser::getJurisdiction,jurisdictionNormal)
                .eq(TUser::getIsCompanyAccount,isCompanyAccountYes)
                .eq(TUser::getApplication,application)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByTelephone(String telephone, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel,telephone)
                .eq(TUser::getApplication,application)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public Long insert(TUser user, Integer application) {
        if(application!=null) {
            user.setApplication(application);
        }
        MybatisPlus.getInstance().save(user);
        return user.getId();
    }

    @Override
    public List<TUser> selectByVxOpenId(String openId, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getVxOpenId,openId)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectUserTelByJurisdiction(String telephone, Integer jurisdictionNormal, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
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
    public List<TUser> selectByUserTelByPasswordByIsCompanyAccYes(String telephone, String password, Integer isCompanyAccount, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel,telephone)
                .eq(TUser::getPassword,password)
                .eq(TUser::getIsCompanyAccount,isCompanyAccount)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByTelephoneInInIds(String param, List<Long> userIds, Integer application) {
        MybatisPlusBuild build = new MybatisPlusBuild(TUser.class)
                .in(TUser::getId, userIds)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUser::getCreateTime));
        if(param!=null) {
            build.eq(TUser::getUserTel,param);
        }

        return MybatisPlus.getInstance().finAll(new TUser(),build);
    }

    @Override
    public List<TUser> selectByNameAndTelephoneLikeSkillInIds(String name, String telephone, String skill, List<Long> idList, Integer application) {
        MybatisPlusBuild build = new MybatisPlusBuild(TUser.class)
                .in(TUser::getId,idList)
                .eq(TUser::getIsValid, AppConstant.IS_VALID_YES);

        if(name!=null) {
            build.eq(TUser::getName, name);
        }

        if(telephone!=null) {
            build.eq(TUser::getUserTel, telephone);
        }

        if(skill!=null && skill!="") {
            build.like(TUser::getSkill, "%" + skill + "%");
        }

        return MybatisPlus.getInstance().finAll(new TUser(),build);
    }

    /**
     * 根据ids查找用户
     * @param split 用户ids
     * @return
     */
    @Override
    public List<TUser> selectInUserIds(String[] split, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .in(TUser::getId,split)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据手机号和权限查询
     * @param telephone
     * @param jurisdictionNormal
     * @return
     */
    @Override
    public List<TUser> selectByTelephoneAndJurisdiction(String telephone, Integer jurisdictionNormal, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel,telephone)
                .eq(TUser::getJurisdiction,jurisdictionNormal)
                .eq(TUser::getApplication, application)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByName(String param, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getName,param)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByJurisdictionAndCreateTimeDesc(Integer jurisdictionNormal, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getJurisdiction,jurisdictionNormal)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUser::getCreateTime)));
    }

    @Override
    public List<TUser> selectByNameAndJurisdictionCreateTimeDesc(String param, Integer jurisdictionNormal, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(), new MybatisPlusBuild(TUser.class)
                .eq(TUser::getName,param)
                .eq(TUser::getJurisdiction,jurisdictionNormal)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES)
                .orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TUser::getCreateTime)));
    }

    @Override
    public List<TUser> selectByUserAccountAndPasswordAndJurisdiction(String account, String password, Integer jurisdictionAdmin, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(), new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserAccount,account)
                .eq(TUser::getPassword,password)
                .eq(TUser::getJurisdiction,jurisdictionAdmin)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUser> selectByUserTelAndJurisAndIsCompany(String userTel, Integer jurisdictionNormal, Integer isCompanyAccountYes, Integer application) {
        return MybatisPlus.getInstance().finAll(new TUser(),new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel,userTel)
                .eq(TUser::getJurisdiction,jurisdictionNormal)
                .eq(TUser::getIsCompanyAccount,isCompanyAccountYes)
                .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 获取该账号的分身
     *
     * @param user
     * @return
     */
    @Override
    public TUser queryDoppelganger(TUser user, Integer application) {
        TUser result = null;
        List<TUser> users = queryUsersByTelephone(user.getUserTel(), application);

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


    /**
     * 根据名字查找user
     * @param name
     * @return
     */
    @Override
    public List<TUser> selectUserByName(String name,Integer application){
        return MybatisPlus.getInstance().finAll(new TUser() , new MybatisPlusBuild(TUser.class).eq(TUser::getName , name));
    }

    /**
     * 根据电话查找user
     * @param telephone
     * @return
     */
    @Override
    public List<TUser> selectUserByTelephone(String telephone,Integer application){
        return MybatisPlus.getInstance().finAll(new TUser() , new MybatisPlusBuild(TUser.class).eq(TUser::getUserTel , telephone));
    }

    @Override
    public TUser selectByUserAccountAndPassword(String telephone, String password, int application) {
        return MybatisPlus.getInstance().findOne(new TUser(), new MybatisPlusBuild(TUser.class)
                .eq(TUser::getUserTel, telephone)
        .eq(TUser::getPassword, password)
        .eq(TUser::getApplication, application)
        .eq(TUser::getIsValid,AppConstant.IS_VALID_YES));
    }

}





