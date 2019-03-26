package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.TUserCompany;

import java.util.ArrayList;
import java.util.List;

public interface UserCompanyDao {
    /**
     * 根据用户id时间倒序查询UserCompany记录
     * @param userId
     * @return
     */
    List<TUserCompany> queryByUserIdDESC(Long userId);

    /**
     * 根据组织id获取UserCompany记录
     * @param companyId
     * @return
     */
    List<TUserCompany> selectByCompanyId(Long companyId);

    /**
     * 查询组织账号的UserCompany记录
     * @param companyId
     * @return
     */
    List<TUserCompany> findRecordOfCompanyAccount(Long companyId);

    /**
     * 根据用户id时间倒序查询UserCompany记录
     * @param ids
     * @return
     */
    List<TUserCompany> queryByUserIdsDESC(Long... ids);

    /**
     * 根据用户id、组织内角色查询UserCompany记录
     * @param id
     * @param role
     * @return
     */
    List<TUserCompany> selectByUserIdAndCompanyjob(Long id, Integer role);

    /**
     * 根据用户id、组织id获取UserCompany记录
     * @param id
     * @param companyId
     * @return
     */
    List<TUserCompany> selectByUserIdAndCompanyId(Long id, Long companyId);

    /**
     * 更新
     * @param userCompany
     */
    int updateByPrimaryKey(TUserCompany userCompany);

    /**
     * 插入
     * @param userCompany
     */
    int insert(TUserCompany userCompany);

    /**
     * 根据用户id、组内职务查找用户-组织记录
     * @param id
     * @param jobCompanyCreater
     * @return
     */
    List<TUserCompany> selectByUserIdAndCompanyJob(Long id, Integer jobCompanyCreater);

    /**
     * 根据组织id查找用户-组织记录
     * @param companyId
     * @return
     */
    List<TUserCompany> selectJoinMembersByCompanyId(Long companyId);

    /**
     * 根据组织id、时间区间查找用户-组织记录
     * @param companyId
     * @param begin
     * @param end
     * @return
     */
    List<TUserCompany> selectJoinMembersByCompanyIdBetween(Long companyId, Long begin, Long end);

    /**
     * 查找userCompany记录
     * @param companyId 组织id
     * @param jobCompanyCreater 组织内角色
     * @param joinStateCompanyPass 审核状态
     * @param betLeft 开始时间戳
     * @param betRight 结束时间戳
     * @return
     */
    List<TUserCompany> selectBycompanyIdAndCompanyJobAndStateBetween(Long companyId, Integer jobCompanyCreater, Integer joinStateCompanyPass, long betLeft, long betRight);

    /**
     * 查找userCompany记录
     * @param companyId 组织id
     * @param joinStateCompanyPass 组织内角色
     * @param param 组内名称
     * @param groupId 组id
     * @return
     */
    List<TUserCompany> selectByCompanyIdAndStateAndTeamNameAndGroupId(Long companyId, Integer joinStateCompanyPass, String param, Long groupId);

    /**
     * 查找userCompany记录
     * @param companyId 组织id
     * @param valueOf 用户id
     * @return
     */
    List<TUserCompany> selectByCompanyIdAndUserId(Long companyId, Long valueOf);

    /**
     * 条件更新
     * @param idList
     * @param jobCompanyMember
     * @param companyId
     * @param userCompany
     * @return
     */
    int updateByUserIdsAndCompanyJobAndCompanyIdSetUserCompany(ArrayList<Long> idList, Integer jobCompanyMember, Long companyId, TUserCompany userCompany);

    /**
     * 条件更新
     * @param valueOf
     * @param jobCompanyMember
     * @param companyId
     * @param joinStateCompanyNotYet
     * @param userCompany
     */
    int updateByUserIdAndCompanyJobAndCompanyIdAndStateSetUserCompany(Long valueOf, Integer jobCompanyMember, Long companyId, Integer joinStateCompanyNotYet, TUserCompany userCompany);

    /**
     * 通过用户ID获取组织ID
     * @param userId
     * @return
     */
	TUserCompany getOwnCompanyIdByUser(Long userId);

    /**
     * 列出该组织下的所有用户
     * @param companyId
     * @return
     */
    List<TUserCompany> listCompanyUser(Long companyId);

    /**
     * 统计该分组下的用户数
     * @param groupId 分组ID
     * @return
     */
    long countGroupUser(Long groupId);

    /**
     * 查找记录
     * @param companyId 组织id
     * @param joinStateCompanyNotYet 加入状态
     * @return
     */
    List<TUserCompany> selectByCompanyIdAndState(Long companyId, Integer joinStateCompanyNotYet);

    /**
     * 根据组织用户名字查找组织用户
     * @param name
     * @return
     */
    List<TUserCompany> selectUserCompanyByName(String name);

    /**
     * 根据组织编号和用户列表 查看用户组织信息
     * @param userIdList
     * @param companyId
     * @return
     */
    List<TUserCompany> selectUserCompanyByIdAndUserIdList(List<Long> userIdList , Long companyId);

    /**
     * 查找记录
     * @param idList id集合
     * @param jobCompanyMember 组内身份
     * @return
     */
    List<TUserCompany> selectInUserIdAndCompanyJob(ArrayList<Long> idList, Integer jobCompanyMember);

    TUserCompany selectByPrimaryKey(Long companyId);
}
