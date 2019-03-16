package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
import com.e_commerce.miscroservice.user.vo.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    BonusPackageVIew bonusPackageInfo(TUser user, Long bonusId);

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
     * @param token
     * @param user
     * @param cardId
     * @param cardName
     */
    void auth(String token, TUser user, String cardId, String cardName);

    /**
     * 提交组织审核信息
     * @param user
     * @param company
     */
    void companyAuth(TUser user, TCompany company);

    boolean ifAlreadyCert(Long id);

    String getCertStatus(Long id);

    /**
     * 签到信息查询
     * @param user
     * @param ymString
     * @return
     */
    SignUpInfoView signUpInfo(TUser user, String ymString);

    /**
     * 每日签到
     * @param token
     * @param user
     * @return
     */
    long signUp(String token, TUser user);

    /**
     * 用户反馈
     * @param user
     */
    void feedBack(TUser user,TReport report);

    /**
     * 任务信息查询
     * @param user
     * @return
     */
    Set<Integer> taskList(TUser user);

    /**
     * 红包退回
     * @param user
     * @param bonusPackageId
     */
    void sendBackBonusPackage(TUser user, Long bonusPackageId);

    /**
     * 获取key-value值
     * @param key
     * @return
     */
    TPublish getPublishValue(String key);

    /**
     * 发送短信
     * @param telephone
     * @return
     */
    AjaxResult genrateSMSCode(String telephone);

    /**
     * 校验短信验证码
     * @param telephone
     * @param validCode
     */
    void checkSMS(String telephone, String validCode);

    /**
     * 回馈邀请人
     * @param inviterId
     * @param mineId
     */
    void payInviter(Long inviterId, Long mineId);

    /**
     * 分享（查看二维码）
     * @param user
     * @param serviceId
     * @param option
     * @param token
     * @param userId
     * @return
     */
    ShareServiceView share(TUser user, String serviceId, String option, String token, String userId);

    /**
     * 微信授权基本信息更新
     * @param user
     * @param token
     * @return
     */
    void wechatInfoAuth(TUser user, String token);

    /**
     * 根据id获取场景值
     * @param scene
     * @return
     */
    SceneView scene(Long scene);

    /**
     * 激活(生成邀请码)
     * @param token
     * @param inviteCode
     */
    void generateInviteCode(String token, String inviteCode);

    /**
     * 重置密码
     * @param telephone
     * @param validCode
     * @param password
     */
    void modifyPwd(String telephone, String validCode, String password);

    /**
     * 申请加入组织
     * @param user
     * @param companyId
     */
    void joinCompany(TUser user, Long companyId);

    /**
     * 组织时间轨迹
     * @param user
     * @param year
     * @param month
     * @param type
     * @return
     */
    CompanyPaymentView queryPayment(TUser user, String year, String month, String type);

    /**
     * 任务完成
     * @param user
     * @param growthValueEnum
     */
    TUser taskComplete(TUser user, GrowthValueEnum growthValueEnum);

    TUser taskComplete(TUser user, GrowthValueEnum growthValueEnum, Integer counts);

    /**
     * 是自己的红包
     * @param user
     * @param bonusPackageId
     * @return
     */
    Map<String, Object> isMyBonusPackage(TUser user, Long bonusPackageId);

    /**
     * 组织版登录(密码)
     * @param telephone
     * @param password
     * @return
     */
    Map<String, Object> loginGroupByPwd(String telephone, String password);

    /**
     * 手机号验证码登录
     * @param telephone
     * @param validCode
     * @return
     */
    Map<String, Object> loginUserBySMS(String telephone, String validCode);

    @Transactional(rollbackFor = Throwable.class)
    TUser rigester(TUser user);

    TUser getUserAccountByTelephone(String telephone);

    /**
     * 为用户增加发布次数
     * @param user 当前用户
     * @param type 类型 1、求助 2、服务
     */
	void addPublishTimes(TUser user, int type);
    /**
     * 是否关注了该用户
     * @param userId 当前用户ID
     * @param userFollowId 被关注用户ID
     * @return
     */
    boolean isCareUser(Long userId, Long userFollowId);
}
