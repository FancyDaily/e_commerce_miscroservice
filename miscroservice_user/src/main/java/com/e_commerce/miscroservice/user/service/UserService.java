package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TBonusPackage;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserSkill;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.user.vo.DesensitizedUserView;
import com.e_commerce.miscroservice.user.vo.UserFreezeView;
import com.e_commerce.miscroservice.user.vo.UserPageView;
import com.e_commerce.miscroservice.user.vo.UserSkillListView;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * 收藏/取消收藏
     * @param user
     * @param orderId
     */
    void collect(TUser user, Long orderId);

    /**
     * 个人主页
     * @param user
     * @param userId
     * @return
     */
    UserPageView page(TUser user, Long userId);

    /**
     * 发布的服务/求助
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param isService
     * @return
     */
    QueryResult pageService(Long userId, Integer pageNum, Integer pageSize, boolean isService);

    /**
     * 历史服务和求助
     * @param user
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    QueryResult historyService(TUser user, Long userId, Integer pageNum, Integer pageSize);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    void freezeTimeCoin(Long userId, long freeTime, Long serviceId, String serviceName);

    /**
     * 删除技能
     * @param id
     */
    void skillDelete(Long id);

    /**
     * 查看用户基本信息
     * @param user
     * @param userId
     * @return
     */
    DesensitizedUserView info(TUser user, Long userId);

    /**
     * 修改个人信息
     *
     * @param token
     * @param user
     * @return
     */
    String modify(String token, TUser user);

    /**
     * 预生成一个红包
     *
     * @param user
     * @param bonusPackage
     * @return
     */
    TBonusPackage preGenerateBonusPackage(TUser user, TBonusPackage bonusPackage);

    /**
     * 创建一个红包
     * @param user
     * @param bonusPackageId
     */
    void generateBonusPackage(TUser user, Long bonusPackageId);

    /**
     * 查看一个红包
     * @param user
     * @param bonusId
     * @return
     */
    TBonusPackage bonusPackageInfo(TUser user, Long bonusId);

    /**
     * 打开一个红包
     * @param user
     * @param bonusId
     */
    void openBonusPackage(TUser user, Long bonusId);

    /**
     * 收藏列表
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    QueryResult<List<TOrder>> collectList(TUser user, Integer pageNum, Integer pageSize);

    /**
     * 用户认证信息更新(实名认证)
     * @param user
     * @param cardId
     * @param cardName
     */
    void auth(TUser user, String cardId, String cardName);
}
