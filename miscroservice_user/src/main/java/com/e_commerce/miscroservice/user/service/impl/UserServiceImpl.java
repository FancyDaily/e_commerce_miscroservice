package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;


import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.PaymentEnum;
import com.e_commerce.miscroservice.commons.enums.application.SysMsgEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.util.colligate.*;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.service.impl.BaseService;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private OrderCommonController orderService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTimeRecordDao userTimeRecordDao;

    @Autowired
    private UserFreezeDao userFreezeDao;

    @Autowired
    private PublicWelfareDao publicWelfareDao;

    @Autowired
    private UserSkillDao userSkillDao;

    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private BonusPackageDao bonusPackageDao;

    @Autowired
    private RedisUtil redisUtil;

    private SnowflakeIdWorker idGenerator = new SnowflakeIdWorker();

    /**
     * 时间轨迹
     *
     * @param user
     * @param ymString
     * @param option
     * @return
     */
    @Override
    public Map<String, Object> payments(TUser user, String ymString, String option) {
        // id
        Long id = user.getId();

        // 同步
        user = userDao.selectByPrimaryKey(id);  //TODO

        // 结果
        List<SingleUserTimeRecordView> resultList = new ArrayList<SingleUserTimeRecordView>();

        // 判空
        if (StringUtil.isEmpty(ymString)) {
            ymString = DateUtil.timeStamp2Date(System.currentTimeMillis());
        }

        if (!StringUtil.isEmpty(ymString) && !ymString.contains("-")) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "日期参数格式不正确!");
        }

        // 当前月份
        String[] split = ymString.split("-");
        String month = split[1].toString();

        // 处理请求参数 ymString
        Map<String, Object> map = DateUtil.ym2BetweenStamp(ymString);
        String beginStr = (String) map.get("begin");
        String endStr = (String) map.get("end");
        Long begin = Long.valueOf(beginStr);
        Long end = Long.valueOf(endStr);

        // 返回结果
        List<TUserTimeRecord> totalList = userTimeRecordDao.selectMonthlyTimeRecord(id, begin, end);   //TODO

        // 计算月度总计，并分组：收入、支出
        List<SingleUserTimeRecordView> inList = new ArrayList<SingleUserTimeRecordView>();
        List<SingleUserTimeRecordView> outList = new ArrayList<SingleUserTimeRecordView>();
        Long totalIn = 0L;
        Long totalOut = 0L;

        // 筛选数据、统计总和
        for (TUserTimeRecord record : totalList) {
            SingleUserTimeRecordView view = BeanUtil.copy(record, SingleUserTimeRecordView.class);
            view.setIdString(String.valueOf(view.getId()));
            view.setDate(DateUtil.timeStamp2Date(record.getCreateTime()));
            Integer type = record.getType();
            for (PaymentEnum payType : PaymentEnum.values()) {
                if (type.equals(payType.getCode())) {
                    view.setTitle(payType.getMessage());
                    break;
                }
            }

            // 流水名目
            if (id.equals(record.getUserId())) { // 收入
                if (record.getType().equals(PaymentEnum.PAYMENT_TYPE_PROVIDE_SERV.getCode())) {
                    view.setTitle(PaymentEnum.PAYMENT_TYPE_PROVIDE_SERV.getMessage());
                }
                resultList.add(view);
                totalIn += record.getTime();
                inList.add(view);
            }

            if (id.equals(record.getFromUserId())) { // 支出
                if (record.getType().equals(PaymentEnum.PAYMENT_TYPE_ACEPT_SERV.getCode())) {
                    view.setTitle(PaymentEnum.PAYMENT_TYPE_ACEPT_SERV.getMessage());
                }
                view.setTime(-view.getTime());
                resultList.add(view);
                totalOut += record.getTime();
                outList.add(view);
            }
        }

        if (StringUtil.equals(AppConstant.PAYMENTS_OPTION_IN, option)) { // 收入
            resultList = inList;
        }

        if (StringUtil.equals(AppConstant.PAYMENTS_OPTION_OUT, option)) { // 收入
            resultList = outList;
        }

        Collections.sort(resultList, new Comparator<SingleUserTimeRecordView>() {

            @Override
            public int compare(SingleUserTimeRecordView o1, SingleUserTimeRecordView o2) {
                return (int) (o2.getCreateTime() - o1.getCreateTime());
            }
        });

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Long surplusTime = user.getSurplusTime(); // 总额
        Long freezeTime = user.getFreezeTime(); // 冻结
        Long vacantTime = surplusTime - freezeTime; // 可用
        resultMap.put("total", surplusTime); // 总额
        resultMap.put("vacant", vacantTime);
        resultMap.put("frozen", freezeTime);
        resultMap.put("month", month);
        resultMap.put("monthTotalIn", totalIn);
        resultMap.put("monthTotalOut", totalOut);
        resultMap.put("monthList", resultList);

        return resultMap;
    }

    /**
     * 冻结明细
     *
     * @param id
     * @param lastTime
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<UserFreezeView> frozenList(Long id, Long lastTime, Integer pageSize) {
        // 判空
        if (lastTime == null) {
            lastTime = System.currentTimeMillis();
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        List<TUserFreeze> userFreezes = userFreezeDao.queryUserFreeze(id, lastTime, MybatisSqlWhereBuild.ORDER.DESC);

        // 如果列表为空
        if (userFreezes.isEmpty()) {
            return new QueryResult<UserFreezeView>();
        }

        // idList
        List<Long> idList = new ArrayList<Long>();
        // 结果集list
        List<UserFreezeView> resultList = new ArrayList<UserFreezeView>();
        // 结果集list
        List<UserFreezeView> finalResultList = new ArrayList<UserFreezeView>();
        // 遍历装载 -> 冻结金额、分页时间、服务id
        for (TUserFreeze userFreeze : userFreezes) {
            idList.add(userFreeze.getOrderId());
            UserFreezeView result = BeanUtil.copy(userFreeze, UserFreezeView.class); //TODO 装载分页时间戳、冻结时间、订单id
            resultList.add(result);
        }

        // PageHelper
        Page<Object> startPage = PageHelper.startPage(0, pageSize);

        // 服务集
        // 查找订单表
        List<TOrder> orders = orderService.selectOrdersInOrderIds(idList);//TODO 调用订单模块的controller() 入餐 -> orderId
        //建立订单id-订单实体映射
        Map<Long, TOrder> orderMap = new HashMap<Long, TOrder>();
        for (TOrder order : orders) {
            orderMap.put(order.getId(), order);
        }

        // 遍历装载
        for (UserFreezeView result : resultList) {
            TOrder order = orderMap.get(result.getOrderId());
            if (order != null) {
                result.setAddressName(order.getAddressName());
                result.setServiceName(order.getServiceName());
                result.setStartTime(order.getStartTime());
                result.setEndTime(order.getEndTime());
                result.setServicePersonnel(order.getServicePersonnel());
                result.setType(order.getType());
                result.setServiceIdString(String.valueOf(order.getServiceId()));
                result.setOrderIdString(String.valueOf(order.getId()));
                finalResultList.add(result);
            }
        }

        QueryResult<UserFreezeView> queryResult = new QueryResult<UserFreezeView>();
        queryResult.setResultList(finalResultList);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }

    /**
     * 公益历程列表
     *
     * @param user
     * @param lastTime
     * @param pageSize
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> publicWelfareList(TUser user, Long lastTime, Integer pageSize, Integer year) {
        // 判空
        if (lastTime == null) {
            lastTime = System.currentTimeMillis();
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        if (year == null) {
            long timeStamp = System.currentTimeMillis();
            String timeStamp2Date = DateUtil.timeStamp2Date(timeStamp);
            String[] split = timeStamp2Date.split("-");
            year = Integer.valueOf(split[0]);
        }

        // between
        Map<String, Object> betMap = DateUtil.y2BetweenStamp(year);
        Long betLeft = (Long) betMap.get("betLeft");
        Long betRight = (Long) betMap.get("betRight");

        // id
        Long id = user.getId();

        WelfareParamView param = new WelfareParamView();
        param.setId(id);
        param.setYear(year);

        //查询
        Map<String, Object> yearAndAllPBMap = publicWelfareDao.selectPublicWelfare(param, id, betLeft, betRight, lastTime, MybatisSqlWhereBuild.ORDER.DESC);

        // 年度
        Long yearWelfare = (Long) yearAndAllPBMap.get("yearWelfare");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("yearTotal", yearWelfare);
        Long publicWelfareTime = user.getPublicWelfareTime();
        resultMap.put("total", publicWelfareTime);

        // PageHelper
        Page<Object> startPage = PageHelper.startPage(0, pageSize);

        // 查询公益历程详细
        List<TPublicWelfare> publicWelfares = (List<TPublicWelfare>) yearAndAllPBMap.get("publicWelfares");

        // 历程明细集
        List<WelfareView> welfareList = new ArrayList<>();
        for (TPublicWelfare publicWelfare : publicWelfares) {
            WelfareView welfareView = new WelfareView();
            welfareView.setCoin(String.valueOf(publicWelfare.getTime()));   //金额
            welfareView.setIn(true);    //收入
            welfareView.setName(publicWelfare.getName());//项目名
            welfareView.setTimeStamp(publicWelfare.getCreateTime());//分页的时间戳
            welfareView.setTime(publicWelfare.getDate());//日期
            welfareList.add(welfareView);
        }

        QueryResult<WelfareView> queryResult = new QueryResult<>();
        queryResult.setResultList(welfareList);
        queryResult.setTotalCount(startPage.getTotal());

        resultMap.put("detailList", queryResult);

        return resultMap;
    }

    /**
     * 查看技能(包含列表和详情)
     *
     * @param user
     * @return
     */
    @Override
    public UserSkillListView skills(TUser user) {
        Long userId = user.getId();

        List<TUserSkill> userSkills = userSkillDao.queryOnesSkills(user.getId());

        // 处理返回数据
        UserSkillListView skillView = new UserSkillListView();
        List<UserSkillView> userSkillList = new ArrayList<UserSkillView>();
        for (TUserSkill userSkill : userSkills) {
            UserSkillView theView = BeanUtil.copy(userSkill, UserSkillView.class);
            theView.setIdString(String.valueOf(theView.getId()));
            if (theView.getDetailUrls() != null && theView.getDetailUrls().contains(",")) {
                theView.setDetailUrlArray(theView.getDetailUrls().split(","));
            }
            userSkillList.add(theView);
        }
        skillView.setSkillCnt(userSkills.size());
        skillView.setUserSkills(userSkillList);
        return skillView;

    }

    /**
     * 新增技能
     *
     * @param user
     * @param skill
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void skillAdd(TUser user, TUserSkill skill) {
        //校验
        skillPass(user, skill, false);

        skill.setId(idGenerator.nextId()); // 生成主键
        skill.setUserId(user.getId()); // 创建者id

        // craeter & updater
        long currentTimeMillis = System.currentTimeMillis();
        skill.setCreateTime(currentTimeMillis);
        skill.setCreateUser(user.getId());
        skill.setCreateUserName(user.getName());
        skill.setUpdateTime(currentTimeMillis);
        skill.setUpdateUser(user.getId());
        skill.setUpdateUserName(user.getName());
        skill.setIsValid(AppConstant.IS_VALID_YES);
        userSkillDao.insert(skill);
    }

    /**
     * 修改技能
     *
     * @param user
     * @param skill
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void skillModify(TUser user, TUserSkill skill) {
        //校验
        skillPass(user, skill, true);

        // updater
        long currentTimeMillis = System.currentTimeMillis();
        skill.setUpdateTime(currentTimeMillis);
        skill.setUpdateUser(user.getId());
        skill.setUpdateUserName(user.getName());
        userSkillDao.update(skill);
    }

    /**
     * 根据id查询用户
     *
     * @param userId
     * @return
     */
    @Override
    public TUser getUserbyId(Long userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    /**
     * 收藏/取消收藏
     *
     * @param user
     * @param orderId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void collect(TUser user, Long orderId) {

//TODO  orderService.updateOrderRelationship();
    }

    /**
     * 个人主页
     * @param user
     * @param userId
     * @return
     */
    @Override
    public UserPageView page(TUser user, Long userId) {
        UserPageView result = new UserPageView();
        //基本信息
        user = userDao.selectByPrimaryKey(userId);
        DesensitizedUserView view = BeanUtil.copy(user, DesensitizedUserView.class);
        //关注状态
        Integer attenStatus = userFollowDao.queryAttenStatus(user.getId(),userId);
        view.setIsAtten(attenStatus);
        result.setDesensitizedUserView(view);
        //求助列表
        QueryResult<TOrder> helps = getOnesAvailableItems(userId,1,8,false);

        //服务列表
        QueryResult<TOrder> services = getOnesAvailableItems(userId, 1,8,true);

        //技能列表
        UserSkillListView skills = skills(user);

        result.setHelps(helps);
        result.setServices(services);
        result.setSkills(skills);

        return result;
    }

    /**
     * 发布的服务/求助
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param isService
     * @return
     */
    @Override
    public QueryResult pageService(Long userId, Integer pageNum, Integer pageSize, boolean isService) {
        return getOnesAvailableItems(userId,pageNum,pageSize,isService);
    }

    /**
     * 获取历史互助记录列表
     * @param user
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult historyService(TUser user, Long userId, Integer pageNum, Integer pageSize) {

        if (pageNum == null) {
            pageNum = 1;
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        //分页
        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);

        //查找符合条件的订单记录
        List<TOrder> orders = orderService.selectEndOrdersByUserId(userId);
        List<Long> orderIds = new ArrayList<>();
        for(TOrder order:orders) {
            orderIds.add(order.getId());
        }

        Map<Long,Object> evaluateMap = new HashMap<>();
        List<TEvaluate> evaluates = orderService.selectEvaluateInOrderIdsAndByUserId(orderIds,userId);
        for(TEvaluate evaluate:evaluates) {
            List<TEvaluate> evaluateList = (List<TEvaluate>) evaluateMap.get(evaluate.getOrderId());
            if(evaluateList==null) {
                evaluateList = new ArrayList<>();
            }
            evaluateList.add(evaluate);
            evaluateMap.put(evaluate.getOrderId(),evaluateList);
        }

        //结果集
        List<HistoryServView> resultList = new ArrayList<>();
        for(TOrder order:orders) {
            HistoryServView historyServView = new HistoryServView();
            historyServView.setUser(userDao.selectByPrimaryKey(order.getCreateUser())); //查找用户信息
            historyServView.setOrder(order);
            historyServView.setEvaluates((List<TEvaluate>) evaluateMap.get(order.getId()));
            resultList.add(historyServView);
        }

        //倒序输出
        Collections.sort(resultList,new Comparator<HistoryServView>() {
                    @Override
                    public int compare(HistoryServView o1, HistoryServView o2) {
                        return (int) (o2.getOrder().getCreateTime() - o1.getOrder().getCreateTime());
                    }
                }
        );

        QueryResult result = new QueryResult();
        result.setResultList(resultList);
        result.setTotalCount(startPage.getTotal());

        return result;
    }

    private QueryResult getOnesAvailableItems(Long userId,  Integer pageNum, Integer pageSize,boolean isService) {
        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TOrder> orders = orderService.selectOdersByUserId(userId, isService);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotalCount(startPage.getTotal());
        queryResult.setResultList(orders);
        return queryResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void freezeTimeCoin(Long userId, long freeTime, Long serviceId, String serviceName) {
        //跟新用户冻结信息
        TUser tUser = userDao.selectByPrimaryKey(userId);
        tUser.setUpdateTime(System.currentTimeMillis());
        tUser.setFreezeTime(tUser.getFreezeTime() + freeTime);
        userDao.updateByPrimaryKey(tUser);
        //创建用户冻结记录
        TUserFreeze userFreeze = new TUserFreeze();
        userFreeze.setId(idGenerator.nextId());
        userFreeze.setUserId(userId);
        userFreeze.setOrderId(serviceId);
        userFreeze.setServiceName(serviceName);
        userFreeze.setFreezeTime(freeTime); // 冻结金额
        userFreeze.setCreateTime(System.currentTimeMillis());
        userFreeze.setCreateUser(userId);
        userFreeze.setCreateUserName(tUser.getName());
        userFreeze.setUpdateTime(System.currentTimeMillis());
        userFreeze.setUpdateUser(userId);
        userFreeze.setUpdateUserName(tUser.getName());
        userFreeze.setIsValid(AppConstant.IS_VALID_YES);
        userFreezeDao.insert(userFreeze);
    }

    /**
     * 删除技能
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void skillDelete(Long id) {
        userSkillDao.delete(id);
    }

    @Override
    public DesensitizedUserView info(TUser user, Long userId) {
        TUser findUser = userDao.info(userId);
        if(findUser==null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"该用户不存在！");
        }
        return BeanUtil.copy(findUser,DesensitizedUserView.class);
    }

    /**
     * 更新用户信息
     *
     * @param token
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public String modify(String token, TUser user) {
      return "";
/*
        TUser idHolder = (TUser) redisUtil.get(token);
        TUser updateData = user; // 原始数据

        // 判空
        if (user == null) {
            return token;
        }

        // 赋予id
        if (user.getId() == null) {
            user.setId(idHolder.getId());
        }

        // 防止将他人的信息篡改
        if (!idHolder.getId().equals(user.getId())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "请勿尝试篡改他人的数据！");
        }

        String telephone = user.getUserTel();
        if (telephone != null) {
            // 若对手机号进行修改
            if (!idHolder.getUserTel().equals(telephone)) {
                if (getUserByTelephone(telephone).isEmpty()) {
                    throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该手机号已存在！");
                }
//				return flushRedisUserNewToken(token, user);
//				flushRedisUser(token, user);
//				return token;
            }
        } else { // 若为基本信息修改
            user.setAuthStatus(AppConstant.BASIC_INFO_ALREADY_MODIFY);
        }

        long currentTimeMillis = System.currentTimeMillis();
        // updater
        user.setUpdateTime(currentTimeMillis);
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());
        // 更新数据库
        userDao.updateByPrimaryKey(user);

        // TODO 如果为修改昵称 -> 同步修改服务表里的创建者昵称
        user = userDao.selectByPrimaryKey(idHolder.getId());
//        completeReward(user); //TODO 任务奖励

        // 如果为组织账号的个人账号,并且进行的是修改手机号操作 => 增加一步，同步修改组织账号的手机号
        if (updateData != null && updateData.getUserTel() != null) {
            TUser companyAccount = userDao.queryDoppelganger(idHolder); //TODO 查找组织账号
            if (companyAccount != null && !idHolder.getId().equals(companyAccount.getId())
                    && !idHolder.getUserTel().equals(updateData.getUserTel())) { // 当前为组织账号的个人账号进行手机号修改
                companyAccount.setUserTel(telephone);
                // updater
                companyAccount.setUpdateTime(currentTimeMillis);
                companyAccount.setUpdateUser(user.getId());
                companyAccount.setUpdateUserName(user.getName());
                userDao.updateByPrimaryKey(companyAccount);
                // 删除组织账号的缓存
                String redisKey = "str" + companyAccount.getId();
                String companyToken = (String) redisUtil.get(redisKey);
                if (companyToken != null) {
                    redisUtil.del(companyToken);// 删除访问凭证
                }
                redisUtil.del(redisKey);// 删除登录凭证
            }
        }

        // 刷新缓存
        flushRedisUser(token, user);
        return token;
 */

    }

    /**
     * 预创建一个红包
     *
     * @param user
     * @param bonusPackage
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public TBonusPackage preGenerateBonusPackage(TUser user, TBonusPackage bonusPackage) {
        //TODO 判穷(含授信)

        long currentTimeMillis = System.currentTimeMillis();
        bonusPackage.setId(idGenerator.nextId());
        bonusPackage.setUserId(user.getId());
        bonusPackage.setCreateTime(currentTimeMillis);
        bonusPackage.setUpdateTime(currentTimeMillis);
        bonusPackage.setCreateUser(user.getId());
        bonusPackage.setUpdateUser(user.getId());
        bonusPackage.setCreateUserName(user.getName());
        bonusPackage.setUpdateUserName(user.getName());
        bonusPackage.setIsValid(AppConstant.IS_VALID_NO);   //预生成红包不可见
        bonusPackageDao.insert(bonusPackage);
        return bonusPackage;
    }

    /**
     * 生成红包
     * @param user
     * @param bonusPackageId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void generateBonusPackage(TUser user, Long bonusPackageId) {
        TBonusPackage bonusPackage = bonusPackageDao.selectByPrimaryKey(bonusPackageId);
        //校验
        if(bonusPackage==null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"该红包不存在！");
        }
        //校验红包状态
        if(AppConstant.IS_VALID_YES.equals(bonusPackage.getIsValid())) {
            return;
        }
        Long time = bonusPackage.getTime();
        Long currentMills = System.currentTimeMillis();
        //TODO 判穷(含授信)

        //余额变动
        user = userDao.selectByPrimaryKey(user.getId());    //最新数据
        user.setSurplusTime(user.getSurplusTime() - time);
        //updater
        user.setUpdateTime(currentMills);
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());
        userDao.updateByPrimaryKey(user);

        //流水
        TUserTimeRecord record = new TUserTimeRecord();
        record.setId(idGenerator.nextId());
        record.setFromUserId(user.getId());
        record.setTime(time);
        record.setType(PaymentEnum.PAYMENT_TYPE_BONUS_PACKAGE_OUT.getCode());
        record.setTargetId(bonusPackageId);
        //creater & updater
        record.setCreateTime(currentMills);
        record.setCreateUser(user.getId());
        record.setCreateUserName(user.getName());
        record.setUpdateTime(currentMills);
        record.setUpdateUser(user.getId());
        record.setUpdateUserName(user.getName());
        record.setIsValid(AppConstant.IS_VALID_YES);
        userTimeRecordDao.insert(record);

        bonusPackage.setId(bonusPackageId);
        //updater
        bonusPackage.setUpdateUser(user.getId());
        bonusPackage.setUpdateUserName(user.getName());
        bonusPackage.setUpdateTime(System.currentTimeMillis());
        //isValid
        bonusPackage.setIsValid(AppConstant.IS_VALID_YES);
        bonusPackageDao.updateByPrimaryKey(bonusPackage);
    }

    /**
     * 查看一个红包
     * @param user
     * @param bonusId
     * @return
     */
    @Override
    public TBonusPackage bonusPackageInfo(TUser user, Long bonusId) {
        return bonusPackageDao.info(bonusId);
    }

    /**
     * 打开一个红包
     * @param user
     * @param bonusId
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    @Override
    public void openBonusPackage(TUser user, Long bonusId) {
        long currentTimeMillis = System.currentTimeMillis();
        // 判空
        if (bonusId == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "红包id不能为空！");
        }

        TBonusPackage bonusRecord = bonusPackageDao.selectByPrimaryKey(bonusId);

        if (bonusRecord == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该红包不存在!");
        }

        if (AppConstant.IS_VALID_NO.equals(bonusRecord.getIsValid())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"红包已失效！");
        }

        //判权
        if(bonusRecord.getCreateUser().equals(user.getId())) { //TODO
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"您不能领取自己的红包!");
        }

        // 更新红包状态
        bonusRecord.setIsValid(AppConstant.IS_VALID_NO);
        //updater
        bonusRecord.setUpdateTime(currentTimeMillis);
        bonusRecord.setUpdateUser(user.getId());
        bonusRecord.setUpdateUserName(user.getName());
        bonusPackageDao.updateByPrimaryKey(bonusRecord);

        // 用户余额增加
        user = userDao.selectByPrimaryKey(user.getId());
        user.setSurplusTime(user.getSurplusTime() + bonusRecord.getTime());
        userDao.updateByPrimaryKey(user);

        // 记录收入流水
        TUserTimeRecord userTimeRecord = new TUserTimeRecord();
        userTimeRecord.setId(snowflakeIdWorker.nextId());
        userTimeRecord.setType(PaymentEnum.PAYMENT_TYPE_BONUS_PACKAGE_IN.getCode());
        userTimeRecord.setTargetId(bonusId); // 关联红包记录
        userTimeRecord.setTime(bonusRecord.getTime());
        // creater & updater
        userTimeRecord.setCreateTime(currentTimeMillis);
        userTimeRecord.setCreateUser(user.getId());
        userTimeRecord.setCreateUserName(user.getName());
        userTimeRecord.setUpdateTime(currentTimeMillis);
        userTimeRecord.setUpdateUser(user.getId());
        userTimeRecord.setUpdateUserName(user.getName());
        userTimeRecord.setIsValid(AppConstant.IS_VALID_YES);
        userTimeRecordDao.insert(userTimeRecord);

        // 写一条通知(通知红包发起人)
        String content = String.format(SysMsgEnum.BONUS_PACKAGE_DONE.getContent(), bonusRecord.getDescription(),
                user.getId());
        Long targetUserId = bonusRecord.getUserId();

        //TODO 插入一条系统消息 调用order模块的接口
        //insertSysMsg(targetUserId, SysMsgEnum.BONUS_PACKAGE_DONE.getTitle(), content);
    }

    /**
     * 收藏列表
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<List<TOrder>> collectList(TUser user, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        List<TOrderRelationship> orderRelationships = orderService.selectCollectList(user.getId());

        List<Long> idList = new ArrayList<>();
        for(TOrderRelationship orderRelationship:orderRelationships) {
            idList.add(orderRelationship.getOrderId());
        }

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TOrder> orders = orderService.selectOrdersInOrderIdsInStatus(idList, AppConstant.COLLECTION_AVAILABLE_STATUS_ARRAY);

        QueryResult queryResult = new QueryResult();
        queryResult.setResultList(orders);
        queryResult.setTotalCount(startPage.getTotal());

        return  queryResult;
    }

    /**
     * 刷勋缓存
     * @param token
     * @param user
     */
    private void flushRedisUser(String token, TUser user) {
        String idKey = String.valueOf(user.getId());

        // 清除
        if (redisUtil.hasKey(token)) {
            redisUtil.del(token);
        }
        if (redisUtil.hasKey(idKey)) {
            redisUtil.del(idKey);
        }

        // 刷新
        redisUtil.set(token, user, getUserTokenInterval());
        redisUtil.set(idKey, user, getUserTokenInterval());
    }

    /**
     * 通过手机号获取用户记录
     * @param telephone
     * @return
     */
    private List<TUser> getUserByTelephone(String telephone) {
        return userDao.queryUsersByTelephone(telephone);
    }

    /**
     * 技能校验
     * @param user
     * @param skill
     * @param isModify
     */
    private void skillPass(TUser user, TUserSkill skill, boolean isModify) {
        Long userId = user.getId();
        // 非空校验 必要元素：技能名称、封面图、id
        if (skill == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "技能名称、封面图、技能编号都不能为空！");
        }

        String name = skill.getName();

        if (isModify) {
            if (skill.getId() == null) {
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "技能编号不能为空!");
            }
        }

        if (name == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "技能名称不能为空！");
        }

        if (skill.getHeadUrl() == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "封面图不能为空!");
        }

        // 判长 技能名
        if (name.length() > 8) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "技能名过长！"); // TODO
        }

        // 判重 技能名
        boolean isExist = userSkillDao.isExist(name, userId);

        if (!isModify) {
            if (isExist) {
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "存在同名技能！");
            }
        }
    }

}
