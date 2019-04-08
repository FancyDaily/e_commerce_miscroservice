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

    /**
     * 查找用户-组织记录，根据用户id、组内职务
     * @param id
     * @param jobCompanyCreater
     * @return
     */
    @Override
    public List<TUserCompany> selectByUserIdAndCompanyJob(Long id, Integer jobCompanyCreater) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getUserId,id)
        .eq(TUserCompany::getCompanyJob,jobCompanyCreater)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 查找加入的成员根据组织id
     * @param companyId
     * @return
     */
    @Override
    public List<TUserCompany> selectJoinMembersByCompanyId(Long companyId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getCompanyId,companyId)
        .neq(TUserCompany::getCompanyJob,AppConstant.JOB_COMPANY_CREATER)
        .eq(TUserCompany::getState,AppConstant.JOIN_STATE_COMPANY_PASS)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 查找加入的成员根据组织id、时间区间
     * @param companyId
     * @param begin
     * @param end
     * @return
     */
    @Override
    public List<TUserCompany> selectJoinMembersByCompanyIdBetween(Long companyId, Long begin, Long end) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
                .eq(TUserCompany::getCompanyId,companyId)
                .neq(TUserCompany::getCompanyJob,AppConstant.JOB_COMPANY_CREATER)
                .eq(TUserCompany::getState,AppConstant.JOIN_STATE_COMPANY_PASS)
                .between(TUserCompany::getCreateTime,begin,end)
                .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据组织id、组内角色、审核状态、起止时间戳查询用户-组织记录
     * @param companyId 组织id
     * @param jobCompanyCreater 组织内角色
     * @param joinStateCompanyPass 审核状态
     * @param betLeft 开始时间戳
     * @param betRight 结束时间戳
     * @return
     */
    @Override
    public List<TUserCompany> selectBycompanyIdAndCompanyJobAndStateBetween(Long companyId, Integer jobCompanyCreater, Integer joinStateCompanyPass, long betLeft, long betRight) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getCompanyJob,jobCompanyCreater)
        .eq(TUserCompany::getState,joinStateCompanyPass)
                .between(TUserCompany::getCreateTime,betLeft,betRight)
                .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TUserCompany> selectByCompanyIdAndStateAndTeamNameAndGroupId(Long companyId, Integer joinStateCompanyPass, String param, Long groupId) {
        MybatisSqlWhereBuild build = new MybatisSqlWhereBuild(TUserCompany.class)
                .eq(TUserCompany::getCompanyId, companyId)
                .eq(TUserCompany::getState,joinStateCompanyPass)
                .eq(TUserCompany::getCompanyJob, AppConstant.JOB_COMPANY_MEMBER)
                .eq(TUserCompany::getIsValid, AppConstant.IS_VALID_YES);
        if(param!=null && !"".equals(param)) {
            build.eq(TUserCompany::getTeamName,param);
        }

        if(groupId!=null && !"".equals(groupId)) {
            build.eq(TUserCompany::getGroupId,groupId);
        }

        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),build);
    }

    @Override
    public List<TUserCompany> selectByCompanyIdAndUserId(Long companyId, Long userId) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getUserId,userId)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public int updateByUserIdsAndCompanyJobAndCompanyIdSetUserCompany(ArrayList<Long> idList, Integer jobCompanyMember, Long companyId, TUserCompany userCompany) {
        return MybatisOperaterUtil.getInstance().update(userCompany,new MybatisSqlWhereBuild(TUserCompany.class)
                .in(TUserCompany::getUserId,idList)
                .eq(TUserCompany::getCompanyJob,jobCompanyMember)
                .eq(TUserCompany::getState,AppConstant.JOIN_STATE_COMPANY_PASS)
                .eq(TUserCompany::getCompanyId,companyId)
                .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 条件更新
     * @param valueOf
     * @param jobCompanyMember
     * @param companyId
     * @param joinStateCompanyNotYet
     * @param userCompany
     */
    @Override
    public int updateByUserIdAndCompanyJobAndCompanyIdAndStateSetUserCompany(Long valueOf, Integer jobCompanyMember, Long companyId, Integer joinStateCompanyNotYet, TUserCompany userCompany) {
        return MybatisOperaterUtil.getInstance().update(userCompany,new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getUserId,valueOf)
        .eq(TUserCompany::getCompanyJob,jobCompanyMember)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getState,joinStateCompanyNotYet)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
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

    @Override
    public List<TUserCompany> selectByCompanyIdAndState(Long companyId, Integer joinStateCompanyNotYet) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getCompanyId,companyId)
        .eq(TUserCompany::getState,joinStateCompanyNotYet)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    /**
     * 根据组织用户名字查找组织用户
     * @param name
     * @return
     */
    public List<TUserCompany> selectUserCompanyByName(String name){
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany() ,
                new MybatisSqlWhereBuild(TUserCompany.class).eq(TUserCompany::getTeamName , name)
                        .eq(TUserCompany::getIsValid , AppConstant.IS_VALID_YES));
    }

    /**
     * 根据组织编号和用户列表 查看用户组织信息
     * @param userIdList
     * @param companyId
     * @return
     */
    public List<TUserCompany> selectUserCompanyByIdAndUserIdList(List<Long> userIdList , Long companyId){
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany() ,
                new MybatisSqlWhereBuild(TUserCompany.class).eq(TUserCompany::getCompanyId , companyId)
                        .in(TUserCompany::getUserId , userIdList)
                        .eq(TUserCompany::getIsValid , AppConstant.IS_VALID_YES));
    }
    @Override
    public List<TUserCompany> selectInUserIdAndCompanyJob(ArrayList<Long> idList, Integer jobCompanyMember) {
        return MybatisOperaterUtil.getInstance().finAll(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
                .in(TUserCompany::getUserId,idList)
                .eq(TUserCompany::getCompanyJob,jobCompanyMember)
                .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

    @Override
    public TUserCompany selectByPrimaryKey(Long companyId) {
        return MybatisOperaterUtil.getInstance().findOne(new TUserCompany(),new MybatisSqlWhereBuild(TUserCompany.class)
        .eq(TUserCompany::getId,companyId)
        .eq(TUserCompany::getIsValid,AppConstant.IS_VALID_YES));
    }

}

