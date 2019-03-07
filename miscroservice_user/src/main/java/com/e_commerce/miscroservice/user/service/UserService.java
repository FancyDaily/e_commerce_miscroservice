package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserSkill;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.user.vo.UserFreezeView;
import com.e_commerce.miscroservice.user.vo.UserSkillListView;

import java.util.Map;

public interface UserService {

    /**
     * 时间轨迹
     *
     * @param user
     * @param ymString
     * @param option
     * @return
     */
    Map<String, Object> payments(TUser user, String ymString, String option);

    /**
     * 冻结明细
     *
     * @param id
     * @param lastTime
     * @param pageSize
     * @return
     */
    QueryResult<UserFreezeView> frozenList(Long id, Long lastTime, Integer pageSize);

    /**
     * 公益历程列表
     * @param user
     * @param lastTime
     * @param pageSize
     * @param year
     */
    Map<String, Object> publicWelfareList(TUser user, Long lastTime, Integer pageSize, Integer year);

    /**
     * 查看技能(包含列表和详情)
     * @param user
     * @return
     */
    UserSkillListView skills(TUser user);

    /**
     * 添加技能
     * @param user
     * @param skill
     */
    void skillAdd(TUser user, TUserSkill skill);

    /**
     * 修改技能
     * @param user
     * @param skill
     */
    void skillModify(TUser user, TUserSkill skill);

    /**
     * 服务 - 根据userId查找用户
     * @param userId
     * @return
     */
    TUser getUserbyId(Long userId);

    /**
     * 冻结用户时间币
     * @param userId 用户id
     * @param freeTime 要冻结的时间币
     * @param serviceId 商品ID
     * @param serviceName 商品名称
     */
	void freezeTimeCoin(Long userId, long freeTime, Long serviceId, String serviceName);
}
