package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.util.colligate.*;
import com.e_commerce.miscroservice.commons.wechat.service.WechatService;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.service.impl.BaseService;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.user.service.GrowthValueService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.service.apiImpl.SendSmsService;
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
    private SendSmsService smsService;

    @Autowired
    private OrderCommonController orderService;

    @Autowired
    private GrowthValueService growthValueService;

    @Autowired
    private WechatService wechatService;

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
    private UserAuthDao userAuthDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserTaskDao userTaskDao;

    @Autowired
    private TypeDictionariesDao typeDictionariesDao;

    @Autowired
    private UserCompanyDao userCompanyDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private RedisUtil redisUtil;

    private SnowflakeIdWorker idGenerator = new SnowflakeIdWorker();

//    @Value("${debug}")
    private String debug;

//    @Value("${page.invite}")
    private String pageValueInvite;

//    @Value("${page.person}") // TODO
    private String pageValuePerson;

//    @Value("${page.service}")
    private String pageValueService;

//    @Value("${page.help}")
    private String pageValueHelp;

//    @Value("${page.company}")
    private String pageValueCompany;

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

        List<TUserFreeze> userFreezes = userFreezeDao.queryUserFreezeDESC(id,lastTime);

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
        if (orderId == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单关系Id不能为空!");
        }

        TOrderRelationship orderRelationship = orderService.selectOrdertionshipByuserIdAndOrderId(user.getId(), orderId);
        if (orderRelationship == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单不存在!");
        }
        if (OrderRelationshipEnum.SERVICE_COLLECTION_IS_TURE.getType() != orderRelationship.getServiceCollectionType()) {    //当前业务为收藏
            orderService.updateCollectStatus(orderRelationship.getId(), OrderRelationshipEnum.SERVICE_COLLECTION_IS_TURE.getType());
        } else { //当前业务为取消收藏
            orderService.updateCollectStatus(orderRelationship.getId(), OrderRelationshipEnum.SERVICE_COLLECTION_IS_CANCEL.getType());
        }
    }

    /**
     * 个人主页
     *
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
        Integer attenStatus = userFollowDao.queryAttenStatus(user.getId(), userId);
        view.setIsAtten(attenStatus);
        result.setDesensitizedUserView(view);
        //求助列表
        QueryResult<TOrder> helps = getOnesAvailableItems(userId, 1, 8, false);

        //服务列表
        QueryResult<TOrder> services = getOnesAvailableItems(userId, 1, 8, true);

        //技能列表
        UserSkillListView skills = skills(user);

        result.setHelps(helps);
        result.setServices(services);
        result.setSkills(skills);

        return result;
    }

    /**
     * 发布的服务/求助
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param isService
     * @return
     */
    @Override
    public QueryResult pageService(Long userId, Integer pageNum, Integer pageSize, boolean isService) {
        return getOnesAvailableItems(userId, pageNum, pageSize, isService);
    }

    /**
     * 获取历史互助记录列表
     *
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
        for (TOrder order : orders) {
            orderIds.add(order.getId());
        }

        Map<Long, Object> evaluateMap = new HashMap<>();
        List<TEvaluate> evaluates = orderService.selectEvaluateInOrderIdsAndByUserId(orderIds, userId);
        for (TEvaluate evaluate : evaluates) {
            List<TEvaluate> evaluateList = (List<TEvaluate>) evaluateMap.get(evaluate.getOrderId());
            if (evaluateList == null) {
                evaluateList = new ArrayList<>();
            }
            evaluateList.add(evaluate);
            evaluateMap.put(evaluate.getOrderId(), evaluateList);
        }

        //结果集
        List<HistoryServView> resultList = new ArrayList<>();
        for (TOrder order : orders) {
            HistoryServView historyServView = new HistoryServView();
            historyServView.setUser(userDao.selectByPrimaryKey(order.getCreateUser())); //查找用户信息
            historyServView.setOrder(order);
            historyServView.setEvaluates((List<TEvaluate>) evaluateMap.get(order.getId()));
            resultList.add(historyServView);
        }

        //倒序输出
        Collections.sort(resultList, new Comparator<HistoryServView>() {
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

    private QueryResult getOnesAvailableItems(Long userId, Integer pageNum, Integer pageSize, boolean isService) {
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
//        MybatisOperaterUtil.getInstance().update(tUser, new MybatisSqlWhereBuild(TUser.class)
//                .eq(TUser::getAge, tUser.getId()));
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
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void skillDelete(Long id) {
        if (id == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "技能id不能为空");
        }
        userSkillDao.delete(id);
    }

    @Override
    public DesensitizedUserView info(TUser user, Long userId) {
        TUser findUser = userDao.info(userId);
        if (findUser == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该用户不存在！");
        }
        DesensitizedUserView view = BeanUtil.copy(findUser, DesensitizedUserView.class);
        String companyNames = view.getCompanyNames();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (String companyName : companyNames.split(",")) {
            if (count == 2) {
                break;
            }
            stringBuilder.append(companyName).append(",");
            count++;
        }
        String string = stringBuilder.toString();
        if (string.endsWith(",")) {
            string = string.substring(0, string.length() - 1);
        }
        view.setLimitedCompanyNames(string);
        return view;
    }

    /**
     * 更新用户信息
     *
     * @param token
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String modify(String token, TUser user) {
//        return "";
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

        user = userDao.selectByPrimaryKey(idHolder.getId());
        completeReward(user); //TODO 用户信息完整度任务奖励

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

    }


    /**
     * 功能描述: 获得完整度任务奖励(包含判断,包含账单)
     * 作者: 许方毅
     * 创建时间: 2018年11月12日 下午5:56:52
     * @param user
     */
    private void completeReward(TUser user) {
        Long userId = user.getId();

        // TODO 与数据库同步
        user = userDao.selectByPrimaryKey(userId);

        // 查询是否有任务完成记录
        List<TUserTask> pageTask = userTaskDao.findTasksByTypeAndUserId(TaskEnum.TASK_PAGE.getType(), user.getId());
        if (!pageTask.isEmpty()) {
            return;
        }

        // 完整度累计
        Integer completeNum = 0;

        // 任务完成所需 完整度
        Integer completeTaskNum = AppConstant.COMPLETE_TASK_NUM;

        // 汇总个人主页记录，计算完整度
        // 封面图片
        String userPicturePath = user.getUserPicturePath();

        if (userPicturePath != null) {
            int num = PersonalIntegrity.USER_PICTURE_PATH.getNum();
            completeNum = completeNum + num;
        }

        String workPlace = user.getWorkPlace();
        if (workPlace != null && !workPlace.isEmpty()) {
            int num = PersonalIntegrity.WORK_TOTAL.getNum();
            completeNum = completeNum + num;
        }

        String college = user.getCollege();
        if (college != null && !college.isEmpty()) {
            int num = PersonalIntegrity.EDU_TOTAL.getNum();
            completeNum = completeNum + num;
        }

        if (completeNum >= completeTaskNum) {
            // 获取任务奖励
            Long reward = TaskEnum.TASK_PAGE.getReward();
            rewardComplete(user, reward);
            // 插入一条任务完成记录
            TUserTask userTask = new TUserTask();
            userTask.setId(idGenerator.nextId());
            userTask.setUserId(userId);
            userTask.setType(TaskEnum.TASK_PAGE.getType());
            userTask.setTargetId(String.valueOf(AppConstant.TARGET_ID_TASK_PAGE));  //TODO targetId
            long timeStamp = System.currentTimeMillis();
            userTask.setCreateTime(timeStamp);
            userTask.setCreateUser(userId);
            userTask.setCreateUserName(user.getName());
            userTask.setUpdateTime(timeStamp);
            userTask.setUpdateUser(userId);
            userTask.setUpdateUserName(user.getName());
            userTask.setIsValid(AppConstant.IS_VALID_YES);

            userTaskDao.insert(userTask);

            //TODO 插入系统消息 REMARK:SYSMSG
//            insertSysMsg(userId, SysMsgEnum.PAGE_COMPLETE.getTitle(), SysMsgEnum.PAGE_COMPLETE.getContent());
        }

        user.setIntegrity(completeNum);
        userDao.updateByPrimaryKey(user);
    }

    private void rewardComplete(TUser user, Long reward) {
        // id
        Long id = user.getId();
        // name
        String name = user.getName();

        // 插入账单流水
        TUserTimeRecord userTimeRecord = new TUserTimeRecord();
        userTimeRecord.setId(snowflakeIdWorker.nextId());
        userTimeRecord.setUserId(id);
        userTimeRecord.setType(PaymentEnum.PAYMENT_TYPE_PRIVATE_PAGE_BONUS.getCode());
        userTimeRecord.setTime(reward);
        // creater & updater
        userTimeRecord.setCreateTime(System.currentTimeMillis());
        userTimeRecord.setCreateUser(id);
        userTimeRecord.setCreateUserName(name);
        userTimeRecord.setUpdateTime(System.currentTimeMillis());
        userTimeRecord.setCreateUser(id);
        userTimeRecord.setCreateUserName(name);
        userTimeRecord.setIsValid(AppConstant.IS_VALID_YES);
        userTimeRecordDao.insert(userTimeRecord);

        // 刷新用户信息(database) 余额
        user.setSurplusTime(user.getSurplusTime() + reward); // 账户余额

        // 更新数据库
        userDao.updateByPrimaryKey(user);

        // -----end---------
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
     *
     * @param user
     * @param bonusPackageId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void generateBonusPackage(TUser user, Long bonusPackageId) {
        TBonusPackage bonusPackage = bonusPackageDao.selectByPrimaryKey(bonusPackageId);
        //校验
        if (bonusPackage == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该红包不存在！");
        }
        //校验红包状态
        if (AppConstant.IS_VALID_YES.equals(bonusPackage.getIsValid())) {
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
     *
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
     *
     * @param user
     * @param bonusId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
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
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "红包已失效！");
        }

        //判权
        if (bonusRecord.getCreateUser().equals(user.getId())) { //TODO
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您不能领取自己的红包!");
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
     *
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
        for (TOrderRelationship orderRelationship : orderRelationships) {
            idList.add(orderRelationship.getOrderId());
        }

        if (idList.isEmpty()) {
            return new QueryResult<>();
        }

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TOrder> orders = orderService.selectOrdersInOrderIdsInStatus(idList, AppConstant.COLLECTION_AVAILABLE_STATUS_ARRAY);

        QueryResult queryResult = new QueryResult();
        queryResult.setResultList(orders);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }

    /**
     * 用户认证信息更新(实名认证)
     *
     * @param user
     * @param cardId
     * @param cardName
     */
    @Override
    public void auth(String token, TUser user, String cardId, String cardName) {
        // TODO 与数据库同步
        user = userDao.selectByPrimaryKey(user.getId());

        // 判断是否以及提交过实名或者以及实名过
        if (AppConstant.AUTH_STATUS_YES.equals(user.getAuthenticationStatus())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经实名过！");
        }

        // 判空
        if (cardId == null || cardName == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "必要身份证参数不全！");
        }

        // 判断是否已经有记录
        List<TUserAuth> auths = userAuthDao.findAllByCardId(cardId);
        if (!auths.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该身份证已被使用！");
        }

        // insert
        TUserAuth userAuth = new TUserAuth();
        userAuth.setId(String.valueOf(idGenerator.nextId()));
        // creater
        Long timeStamp = System.currentTimeMillis();
        userAuth.setUserId(user.getId());
        userAuth.setCreateTime(timeStamp);
        userAuth.setCreateUser(user.getId());
        userAuth.setCreateUserName(user.getName());
        userAuth.setUpdateTime(timeStamp);
        userAuth.setUpdateUser(user.getId());
        userAuth.setUpdateUserName(user.getName());
        userAuth.setIsValid(AppConstant.IS_VALID_YES);
        userAuthDao.insert(userAuth);

        // 认证状态
        user.setAuthenticationStatus(AppConstant.AUTH_STATUS_YES);
        user.setAuthenticationType(AppConstant.AUTH_TYPE_PERSON);

        /*
         * // 性别 user.setSex(userAuth.getSex());
         */

        // 生日
//        user.setBirthday(userAuth.getBirthday());

        // updater
        user.setUpdateTime(timeStamp);
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());

        // 插入系统消息
//        insertSysMsg(user.getId(), SysMsgEnum.AUTH.getTitle(), SysMsgEnum.AUTH.getContent()); //TODO 插入系统消息

        // 实名认证奖励(插入账单流水记录)
//        insertReward(user, PaymentEnum.PAYMENT_TYPE_CERT_BONUS);  //TODO 插入实名认证奖励(插入账单流水记录)

        // 实名认证任务完成(插入任务记录)
//        addMedal(user, DictionaryEnum.TASK_AUTH.getType(), DictionaryEnum.TASK_AUTH.getSubType(),   ////TODO 插入实名认证奖励(插入任务记录)
//                AppConstant.TARGET_ID_TASK_AUTH);

        userDao.updateByPrimaryKey(user);

        user = userDao.selectByPrimaryKey(user.getId());

//        flushRedisUser(token, user);  //刷新缓存
    }

    /**
     * 单位认证信息更新
     *
     * @param user
     * @param company
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void companyAuth(TUser user, TCompany company) {
        if (!ifAlreadyCert(user.getId())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户未实名！");
        }

        if (company.getName() == null || company.getType() == null || company.getProvince() == null
                || company.getCity() == null || company.getCounty() == null || company.getDepict() == null
                || company.getContactsName() == null || company.getContactsTel() == null
                || company.getContactsCardId() == null || company.getUrl() == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "单位信息不能为空!");
        }
        // 单位是否已认证过
        // ，如果有，则提示联系管理员加入（返回单位已存在，更新失败）。
        if (ifAuthCompany(user, company)) {
            // 插入或更新一条待审核的企业记录
            insertOrUpdateCompany(user, company);
        }
    }

    private boolean ifAuthCompany(TUser user, TCompany company) {
        boolean result = false;
//		String code = company.getCode();
        String name = company.getName();
        if (StringUtil.isEmpty(name)) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "公司名称不能为空");
        }

        List<TCompany> companies = companyDao.selectExistUserCompany(name, user.getId(), AppConstant.CORP_CERT_STATUS_YES);
        if (companies != null && companies.size() > 0) {
            throw new MessageException("公司已存在!请联系管理员邀请加入!");
        }
        result = true;
        return result;
    }

    private void insertOrUpdateCompany(TUser user, TCompany company) {
        Long id = company.getId();

        // 查询是否为审核中或已完成
        List<TCompany> companies = companyDao.selectAllByUserId(user.getId());

        if (companies != null && !companies.isEmpty()) {
            for (TCompany type : companies) {
                if (AppConstant.CORP_CERT_STATUS_NOT_YET.equals(type.getStatus())) {
                    throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "你已经有审核中的认证，请耐心等待！");
                }
                if (AppConstant.CORP_CERT_STATUS_YES.equals(type.getStatus())) {
                    throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "每个人只能审核一个组织认证！");
                }
            }

        }

        // 修改
        if (id != null) {
            company.setStatus(AppConstant.CORP_CERT_STATUS_NOT_YET);
            // updater
            company.setUpdateTime(System.currentTimeMillis());
            company.setUpdateUser(user.getId());
            company.setUpdateUserName(user.getName());
            companyDao.update(company); // TODO ifSelective
            return;
        }

        // 创建公司
        company.setId(idGenerator.nextId());
        company.setUserId(user.getId());

        // 默认待审核
        company.setStatus(AppConstant.CORP_CERT_STATUS_NOT_YET);

        // creater & updater
        company.setCreateTime(System.currentTimeMillis());
        company.setCreateUser(user.getId());
        company.setCreateUserName(user.getName());
        company.setUpdateTime(System.currentTimeMillis());
        company.setUpdateUser(user.getId());
        company.setUpdateUserName(user.getName());

        // valid
        company.setIsValid(AppConstant.IS_VALID_YES);

        // insert
        companyDao.insert(company);
    }

    /**
     * 功能描述: 是否已经实名
     * 作者: 许方毅
     * 创建时间: 2018年11月2日 下午4:28:36
     *
     * @param id
     * @return
     */
    @Override
    public boolean ifAlreadyCert(Long id) {
        boolean result = false;
        if (StringUtil.equals(String.valueOf(AppConstant.AUTH_STATUS_YES), getCertStatus(id))) {
            result = true;
        }
        return result;
    }

    /**
     * 功能描述: 获取用户实名状态(默认为个人)
     * 作者: 许方毅
     * 创建时间: 2018年10月29日 下午2:58:42
     *
     * @param id
     * @return
     */
    @Override
    public String getCertStatus(Long id) {
        TUser user = userDao.selectByPrimaryKey(id);
        if (user != null) {
            return String.valueOf(user.getAuthenticationStatus());
        }
        return String.valueOf(AppConstant.DEFAULT_AUTH_STATUS);
    }

    /**
     * 签到信息查询
     *
     * @param user
     * @param ymString
     * @return
     */
    @Override
    public SignUpInfoView signUpInfo(TUser user, String ymString) {
        SignUpInfoView infoView = new SignUpInfoView();
        // 连续天数
        Integer bonus7 = null;
        long count = 0l;
        boolean state = false;

        String thisYmString = DateUtil.getThisYmString().substring(0, 7);

        if (ymString == null || ymString.isEmpty()) {
            ymString = thisYmString;
        }

        List<TUserTask> userTasks = new ArrayList<>();

        // 签到日历
        if (ymString != null && !ymString.equals(thisYmString)) { // 若不为当月
            Map<String, Object> betweenMap = DateUtil.ym2BetweenStamp(ymString);
            Long beginTimeStamp = Long.valueOf((String) betweenMap.get("begin")); // TODO
            Long endTimeStamp = Long.valueOf((String) betweenMap.get("end"));
            userTasks = userTaskDao.queryOnesSignUpBetweenTime(user.getId(), beginTimeStamp, endTimeStamp);
        }

        // 如果为当月，减少查询次数
        if (ymString == null || ymString.equals(thisYmString)) {
            // 获取时间戳区间
            Map<String, Object> thisBetweenMap = DateUtil.ym2BetweenStamp(thisYmString);
            Long thisBeginStamp = Long.valueOf((String) thisBetweenMap.get("begin"));
            Long thisEndStamp = Long.valueOf((String) thisBetweenMap.get("end"));
            // 基础签到信息(连续天数，签到状态)
            userTasks = userTaskDao.queryOnessignUpBetweenTimeDesc(user.getId(), thisBeginStamp, thisEndStamp);

            if (!userTasks.isEmpty()) {
                TUserTask task = userTasks.get(0);
                if (DateUtil.isToday(task.getCreateTime())) {
                    state = true;
                }
            }
        }
        List<DateTypeDictionaryView> resultList = new ArrayList<>();
        // 处理成日期格式
        for (TUserTask signUPInfo : userTasks) {
            DateTypeDictionaryView view = BeanUtil.copy(signUPInfo, DateTypeDictionaryView.class);
            view.setCreateDate(DateUtil.timeStamp2Date(view.getCreateTime()));
            view.setUpdateDate(DateUtil.timeStamp2Date(view.getUpdateTime()));
            view.setIdString(String.valueOf(view.getId()));
            resultList.add(view);
        }

        // position
        int position = 1;

        // 查询最后签到记录
        List<TUserTask> theUserTasks = userTaskDao.findlatestSignUps(user.getId());

        if (!theUserTasks.isEmpty()) {
            // 获取最后一次的签到记录
            TUserTask lastSignUp = theUserTasks.get(0);
            Long timeStamp = lastSignUp.getCreateTime();
            count = lastSignUp.getTargetNum();
            if (DateUtil.isToday(timeStamp)) { // 若为今日
                position = Integer.parseInt(String.valueOf(count));
                // 如果为第七天
                if (count == 7) {
                    bonus7 = Integer.valueOf(lastSignUp.getValue());
                }
            } else if (DateUtil.oneMillesVsAnother(System.currentTimeMillis(), timeStamp)) { // 若为昨日
                if (count < 7) {
                    position = Integer.parseInt(String.valueOf(count)) + 1;
                }
            } else { // 断签
                count = 0;
            }
        }

        // 周期内日期字符数组
        String[] listWithinSeven = DateUtil.getDateListWithinSeven(position);

        // 装载返回信息
        infoView.setBonus7(bonus7);
        infoView.setCount(count);
        infoView.setState(state);
        infoView.setSignUpList(resultList);
        infoView.setCycleArray(listWithinSeven);

        return infoView;
    }

    /**
     * 每日签到
     *
     * @param token
     * @param user
     * @return
     */
    @Override
    public long signUp(String token, TUser user) {
        // 判空
        if (user == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "用户为空！");
        }

        // id
        Long id = user.getId();

        // 判空
        if (id == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "id为空！");
        }

        // TODO 从数据库获得即时的用户
        user = userDao.selectByPrimaryKey(id);

        // 从未签到标记
        boolean flag = false;

        // 查询签到相关记录
        List<TUserTask> tasks = userTaskDao.findlatestSignUps(id);

        TUserTask userTask = null;
        if (tasks.isEmpty()) {
            flag = true;
        } else {
            userTask = tasks.get(0); // 最后签到实体类
        }

        long reward = AppConstant.SIGN_UP_BONUS;
        Integer targetNum = 1;
        long special = 3;
        if (!flag) {
            // 查询连续签到天数与最后签到日
            String status = DateUtil.curtMillesVsYesMilles(userTask.getCreateTime()); // 最后签到日类型(昨天、今天、其他)

            // 计数器
            int count = 0;

            for (int i = 0; i < tasks.size(); i++) {
                count++;
                TUserTask thisDic = tasks.get(i);
                Long thisTimeStamp = thisDic.getCreateTime();
                TUserTask nextDic;
                Long nextTimeStamp = 0l;
                if (i != tasks.size() - 1) {
                    nextDic = tasks.get(i + 1);
                    nextTimeStamp = nextDic.getCreateTime();
                }
                if (!DateUtil.oneMillesVsAnother(thisTimeStamp, nextTimeStamp)) {
                    break;
                }
            }

            if (!status.equals(AppConstant.LAST_SIGN_UP_DAY_YESTERDAY)) {
                count--;
            }

            targetNum = count % 7; // TODO 计算连续签到天数
            // 最后签到日信息
            String dayCountStr = String.valueOf(targetNum);

            // 最后签到日为今天，提示 -> 请勿重复签到
            if (StringUtil.equals(AppConstant.LAST_SIGN_UP_DAY_TODAY, status)) {
                throw new MessageException("请勿重复签到");
            }

            // 最后签到日为昨天
            // -> 计数等于6，计数为7，给出特殊奖励
            // -> 计数等于7，计数为0,给出普通奖励
            // -> else，计数++，给出普通奖励
            if (StringUtil.equals(AppConstant.LAST_SIGN_UP_DAY_YESTERDAY, status)) {
                // 处理计数
                if (StringUtil.equals(AppConstant.SIGN_UP_EDGE, dayCountStr)) { // 7 -> 归零
                    targetNum = 0;
                } else {
                    targetNum = targetNum + 1;
                }

                // 处理奖励
                if (StringUtil.equals(AppConstant.SIGN_UP_ALMOST_EDGE, dayCountStr)) { // 6 -> 特殊
                    // 特殊奖励
                    special = new Random().nextInt(6) + 10; // 特殊奖励 //TODO
                    reward = special;
                }
            }

            // else,计数=1,给出普通奖励
            if (StringUtil.equals(AppConstant.LAST_SIGN_UP_DAY_OTHERS, status)) {
                targetNum = 1;
            }

            userTask.setTargetNum(targetNum);
            userTask.setId(idGenerator.nextId());
            if (targetNum == 7) {
                userTask.setValue(String.valueOf(special));
            }
            long currentTimeMillis = System.currentTimeMillis();
            userTask.setCreateTime(currentTimeMillis);
            userTask.setCreateUser(id);
            userTask.setCreateUserName(user.getName());
            userTask.setUpdateTime(currentTimeMillis);
            userTask.setUpdateUser(id);
            userTask.setUpdateUserName(user.getName());
            userTask.setIsValid(AppConstant.IS_VALID_YES);
            userTaskDao.insert(userTask);
        }

        // 从未签到
        if (flag) {
            // 插入一条新的记录
            insertSignUpInfo(user);
        }

        // update & reward & growth
//        reward(token, user, reward);  //TODO 完成奖励待添加

        return reward;
    }

    /**
     * 用户反馈
     *
     * @param user
     */
    @Override
    public void feedBack(TUser user, TReport report) {
//        orderService.feedBack();  //TODO 调用订单模块的用户反馈接口
    }

    /**
     * 任务信息查询
     * @param user
     * @return
     */
    @Override
    public Set<Integer> taskList(TUser user) {
        List<Integer> resultList = new ArrayList<>();
        Set<Integer> resultSet = new TreeSet<>();
        List<TUserTask> userTasks = userTaskDao.findOnesTasks(user.getId());
        for(TUserTask userTask:userTasks) {
            //签到 -> createTime为当日
            if(userTask.getType().equals(TaskEnum.TASK_SIGNUP.getType()) && !DateUtil.isToday(userTask.getCreateTime())) {
                continue;
            }
            resultList.add(userTask.getType());
            resultSet.add(userTask.getType());
        }
        return resultSet;
    }


    /**
     * 红包退回
     * @param user
     * @param bonusPackageId
     */
    @Override
    public void sendBackBonusPackage(TUser user, Long bonusPackageId) {
        //修改红包记录
        TBonusPackage bonusPackage = bonusPackageDao.selectByPrimaryKey(bonusPackageId);
        bonusPackage.setId(bonusPackageId);
        bonusPackage.setIsValid(AppConstant.IS_VALID_NO);
        bonusPackageDao.updateByPrimaryKey(bonusPackage);
        long currentTimeMillis = System.currentTimeMillis();
        //插入退款流水
        TUserTimeRecord userTimeRecord = new TUserTimeRecord();
        userTimeRecord.setId(idGenerator.nextId());
        userTimeRecord.setType(PaymentEnum.PAYMENT_TYPE_BONUS_PAC_SEND_BACK.getCode());
        userTimeRecord.setTime(bonusPackage.getTime());
        userTimeRecord.setUserId(user.getId());
        userTimeRecord.setTargetId(bonusPackageId);
        userTimeRecord.setCreateUser(user.getId());
        userTimeRecord.setCreateUserName(user.getName());
        userTimeRecord.setCreateTime(currentTimeMillis);
        userTimeRecord.setUpdateUser(user.getId());
        userTimeRecord.setUpdateUserName(user.getName());
        userTimeRecord.setUpdateTime(currentTimeMillis);
        userTimeRecord.setIsValid(AppConstant.IS_VALID_YES);
        userTimeRecordDao.insert(userTimeRecord);

        //返还红包金额
        user = userDao.selectByPrimaryKey(user.getId());
        user.setSurplusTime(user.getSurplusTime() + bonusPackage.getTime());
        user.setCreateTime(currentTimeMillis);
        user.setCreateUser(user.getId());
        user.setCreateUserName(user.getName());
        user.setUpdateTime(currentTimeMillis);
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());
        userDao.updateByPrimaryKey(user);
    }

    /**
     * 获取key-value值
     * @param key
     * @return
     */
    @Override
    public TPublish getPublishValue(String key) {
//        return orderService.getPublishValue(keœy);
        TPublish publish = new TPublish();
        publish.setValue("[{\"id\":\"2100001\",\"name\":\"bug\"},{\"id\":\"2100002\",\"name\":\"建议\"},{\"id\":\"2100003\",\"name\":\"五星好评\"}]");
        return publish;
    }

    /**
     * 发送短信
     * @param telephone
     * @return
     */
    @Override
    public AjaxResult genrateSMSCode(String telephone) {
        AjaxResult result = new AjaxResult();
        // 如果存在
        Long interval = getUserTokenInterval(); // TODO 可以修改时间周期
        if (redisUtil.hasKey("time" + telephone)) {
            if (redisUtil.hasKey("count" + telephone)) {
                long time = redisUtil.getExpire("time" + telephone);// 获取剩余时间
                // 刷新次数
                if (time < 1) {
                    redisUtil.set("count" + telephone, 0, interval);
                }
                // 短信发送次数限制
                int actualCount = (int) redisUtil.get("count" + telephone);
                int count = AppConstant.SMS_SEND_LIMIT;
                // 次数超过限制
                if (actualCount > count) {
                    throw new MessageException("你的短信次数已用完！请之后重试！");
                }
                redisUtil.set("count" + telephone, ++actualCount, interval);
            }
        }

        // 查询是否未到预设的发送间隔 TODO 有效时间为600秒而发送间隔为60s
        if (redisUtil.hasKey(telephone)) {
            long expire = redisUtil.getExpire(telephone); // TODO 获取剩余时间(为0则永久有效)
            long expectedTime = AppConstant.SMS_EXPIRED - AppConstant.SMS_INTERVAL_MILLIS / 1000;
            if (expire > expectedTime) {
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,
                        AppConstant.SMS_INTERVAL_MILLIS / 1000 + "秒内请勿重复发送短信验证码!");
            }
        }

//        String debug = "debug"; //TODO 后续根据配置文件读取

        // 生成6位随机数
        String validCode = "666666";
        if (StringUtil.equals(AppConstant.DEBUG_STATUS_FALSE, debug)) { // 表示当前运行环境为生产
            validCode = UUIDGenerator.messageCode();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", telephone);
        params.put(AppConstant.VALID_CODE, validCode);

        String resMsg;
        if (StringUtil.equals(AppConstant.DEBUG_STATUS_TRUE, debug)) { // 表示当前运行环境为调试
            resMsg = "true";
        } else {
            resMsg = smsService.execute(params);
        }

        if (StringUtil.equals("true", resMsg)) {
            // 将验证码写入缓存
            redisUtil.set(telephone, validCode, AppConstant.SMS_EXPIRED);

            // 设置间隔与次数限制
            redisUtil.set("time" + telephone, telephone, interval);
            redisUtil.set("count" + telephone, 1, interval);

            result.setMsg("发送成功");
            result.setSuccess(true);
        } else {
            result.setErrorCode("发送失败");
            result.setSuccess(false);
        }

        return result;
    }

    /**
     * 校验短信验证码
     * @param telephone
     * @param validCode
     */
    @Override
    public void checkSMS(String telephone, String validCode) {
        if (StringUtil.equals(null, telephone)) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "手机号码不能为空！");
        }
        String content = (String) redisUtil.get(telephone);

        if (content == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "短信验证码已过期！");
        }

        if (!StringUtil.equals(validCode, content)) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "短信校验未通过");
        }

        // 短信校验通过
        // 将验证码失效
        redisUtil.del(telephone);
    }

    /**
     * 回馈邀请人
     * @param inviterId
     * @param mineId
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void payInviter(Long inviterId, Long mineId) {
        // 查询是否已经回馈完成
        List<TTypeDictionaries> dics = typeDictionariesDao.selectInviteRecords(mineId,inviterId);
        if (dics != null && !dics.isEmpty()) {
            return;
        }

        // 加入邀请人记录
        Long targetId = idGenerator.nextId();
        TTypeDictionaries dictionaries = new TTypeDictionaries();
        dictionaries.setId(targetId);
        dictionaries.setEntityId(mineId);
        dictionaries.setTargetId(inviterId);
        dictionaries.setType(DictionaryEnum.INVITER.getType());
        dictionaries.setSubType(DictionaryEnum.INVITER.getSubType());
        long currentTimeMillis = System.currentTimeMillis();
        dictionaries.setCreateTime(currentTimeMillis);
        dictionaries.setCreateUser(mineId);
        dictionaries.setCreateUserName(AppConstant.CREATE_USER_NAME_UNKNOWN);
        dictionaries.setUpdateTime(currentTimeMillis);
        dictionaries.setUpdateUser(mineId);
        dictionaries.setUpdateUserName(AppConstant.CREATE_USER_NAME_UNKNOWN);
        dictionaries.setIsValid(AppConstant.IS_VALID_YES);
        typeDictionariesDao.insert(dictionaries);

        // 插入一条邀请成功的系统消息 REMARK:SYSMSG
        TUser me = userDao.selectByPrimaryKey(mineId);
        /*insertSysMsg(inviterId, SysMsgEnum.INVITER.getTitle(),
                String.format(SysMsgEnum.INVITER.getContent(), me.getName()));*/    //TODO 插入系统消息

        // 回馈
        // 插入账单流水
        TUserTimeRecord record = new TUserTimeRecord();
        Long bonus = PaymentEnum.PAYMENT_TYPE_INVITE_BONUS.getBonus();
        record.setId(idGenerator.nextId());
        record.setUserId(inviterId);
        record.setType(PaymentEnum.PAYMENT_TYPE_INVITE_BONUS.getCode());
        record.setTime(bonus);
//		record.setTargetId(targetId); // 与邀请记录id关联
        record.setTargetId(mineId); // 直接与被邀请人id关联
        record.setCreateTime(currentTimeMillis);
        record.setCreateUser(inviterId);
        record.setCreateUserName(AppConstant.CREATE_USER_NAME_UNKNOWN);
        record.setUpdateTime(currentTimeMillis);
        record.setUpdateUser(inviterId);
        record.setUpdateUserName(AppConstant.CREATE_USER_NAME_UNKNOWN);
        record.setIsValid(AppConstant.IS_VALID_YES);
        userTimeRecordDao.insert(record);

        // 用户更新
        TUser inviter = userDao.selectByPrimaryKey(inviterId);
        inviter.setSurplusTime(inviter.getSurplusTime() + bonus);

        // 使邀请人获得成长值奖励
        inviter = growthValueService.addGrowthValue(inviter, GrowthValueEnum.GROWTH_TYPE_INVITE_BONUS.getCode());

        userDao.updateByPrimaryKey(inviter); // TODO
        // TODO 刷新缓存

        String key = "str" + inviterId;
        if (redisUtil.hasKey(key)) {
            String inviterToken = (String) redisUtil.get(key);
            flushRedisUser(inviterToken, inviter);
        }
    }

    /**
     * 分享（查看二维码）
     * @param user
     * @param serviceId
     * @param option
     * @param token
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public ShareServiceView share(TUser user, String serviceId, String option, String token, String userId) {
        if ((option == "2" || option == "3") && serviceId == null || option == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "服务id和操作不能为空！");
        }

        Long inviterId = user.getId();

        DetailServiceReturnView serviceDetail = null;

        // scene & page
        String scene = "";
        String page = "";
        Integer subType = 0;

        switch (option) {
            case "0": // 邀请好友
                // 获取邀请码
                user = userDao.selectByPrimaryKey(inviterId);
                String inviteCode = user.getInviteCode();
                scene = String.valueOf(inviterId) + "," + userId + "," + "" + "," + inviteCode;
                page = pageValueInvite;
                subType = DictionaryEnum.SHARE_INVITE.getSubType();
                break;

            case "1": // 个人分享
                scene = String.valueOf(inviterId) + "," + userId + "," + "" + "," + "";
                page = pageValuePerson;
                subType = DictionaryEnum.SHARE_PERSON.getSubType();
                break;

            case "2":
                scene = String.valueOf(inviterId) + "," + "" + "," + String.valueOf(serviceId);
                page = pageValueService;
//                serviceDetail = serviceService.serviceDetail(Long.valueOf(serviceId), user);  //TODO 调用订单模块的商品或者订单详情接口
                subType = DictionaryEnum.SHARE_SERVICE.getSubType();
                break;

            case "3":
                scene = String.valueOf(inviterId) + "," + "" + "," + String.valueOf(serviceId);
                page = pageValueHelp;
//                serviceDetail = serviceService.serviceDetail(Long.valueOf(serviceId), user);    //TODO 调用订单模块的商品或者订单详情接口
                subType = DictionaryEnum.SHARE_HELP.getSubType();
                break;

            case "4":
                scene = "" + "," + "" + "," + "" + "," + "" + "," + getOwnCompanyId(inviterId);
                // TODO page企业申请页面
                page = pageValueCompany;// TODO
                subType = DictionaryEnum.SHARE_COMPANY.getSubType();
                break;
        }

        if (scene == "" || page == "") {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "option有误！错误的option:" + option);
        }

        Long id = idGenerator.nextId();

        TTypeDictionaries dictionaries = new TTypeDictionaries();
        dictionaries.setId(id);
        dictionaries.setEntityId(inviterId);
        dictionaries.setValue(scene);
        dictionaries.setType(DictionaryEnum.SHARE.getType());
        dictionaries.setSubType(subType);
        dictionaries.setTargetId(Long.valueOf(option));

        // creater & updater
        long currentTimeMillis = System.currentTimeMillis();
        dictionaries.setCreateTime(currentTimeMillis);
        dictionaries.setCreateUser(inviterId);
        dictionaries.setCreateUserName(user.getName());
        dictionaries.setUpdateTime(currentTimeMillis);
        dictionaries.setUpdateUser(inviterId);
        dictionaries.setUpdateUserName(user.getName());
        dictionaries.setIsValid(AppConstant.IS_VALID_YES);
        typeDictionariesDao.insert(dictionaries);

        // 场景值
        String recordId = String.valueOf(dictionaries.getId());

        // 生成二维码
        String imgUrl = wechatService.genQRCode(recordId, page);

        ShareServiceView serviceView = new ShareServiceView();

        serviceView.setDetailServiceReturnView(serviceDetail);

        serviceView.setUrl(imgUrl);

        // 获得分享奖励
        user = growthValueService.addGrowthValue(user, GrowthValueEnum.GROWTH_TYPE_SHARE_BONUS.getCode());

        // 刷新缓存
        flushRedisUser(token, user);

        return serviceView;
    }

    /**
     * 微信授权基本信息更新
     * @param user
     * @param token
     */
    @Override
    public void wechatInfoAuth(TUser user, String token) {
        // 实名判断 TODO
        if (ifAlreadyCert(user.getId())) {
            user.setSex(null);
        }

        TUser idHolder = (TUser) redisUtil.get(token);
        user.setId(idHolder.getId());
        user.setAuthStatus(AppConstant.WECHAT_BASIC_AUTH_STATUS_YES);
        userDao.updateByPrimaryKey(user);

        user = userDao.selectByPrimaryKey(user.getId());

        flushRedisUser(token, user);
    }

    /**
     * 根据id获取场景值
     * @param scene
     * @return
     */
    @Override
    public SceneView scene(Long scene) {
        if (scene == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "scene不能为空！");
        }
        List<TTypeDictionaries> dictionaries = typeDictionariesDao.selectByIdAndIsValid(scene,AppConstant.IS_VALID_YES);
        SceneView sceneView = new SceneView();
        if (!dictionaries.isEmpty()) {
            String inviterId = null;
            String serviceId = null;
            String userId = null;
            String inviteCode = null;
            String companyId = null;
            TTypeDictionaries dic = dictionaries.get(0);
            Integer subType = dic.getSubType();
            String[] split = dic.getValue().split(",");
            if (DictionaryEnum.SHARE_INVITE.getSubType().equals(subType)
                    || DictionaryEnum.SHARE_PERSON.getSubType().equals(subType)) {
                inviterId = split[0];
                userId = split[1];
                if (split.length == 4) {
                    inviteCode = split[3];
                    if (inviteCode == "") {
                        inviteCode = null;
                    }
                }
            } else if (DictionaryEnum.SHARE_COMPANY.getSubType().equals(subType)) {
                companyId = split[4];
            } else {
                inviterId = split[0];
                serviceId = split[2];
            }
            sceneView.setUserId(userId);
            sceneView.setInviterId(inviterId);
            sceneView.setServiceId(serviceId);
            sceneView.setInviteCode(inviteCode);
            sceneView.setCompanyId(companyId);
        }
        return sceneView;
    }

    /**
     * 生成邀请码
     * @param token
     * @param inviteCode
     */
    @Override
    public void generateInviteCode(String token, String inviteCode) {
        TUser user = (TUser) redisUtil.get(token);
        Long userId = user.getId();
        if (checkInviteCode(inviteCode)) {
            user = userDao.selectByPrimaryKey(userId);
            // 未激活
            if (user.getInviteCode() == null) {
                // 激活
                user.setInviteCode(RandomUtil.generateUniqueChars());
                userDao.updateByPrimaryKey(user);
                flushRedisUser(token, user);
            }
        }
    }

    /**
     * 重置密码(组织)
     * @param telephone
     * @param validCode
     * @param password
     */
    @Override
    public void modifyPwd(String telephone, String validCode, String password) {
        // 处理前端返回的密码(AES密码)
        password = AESCommonUtil.encript(password);

        // 校验
        checkSMS(telephone, validCode);

        TUser user = getCompanyAccountByTelephone(telephone);
        user.setPassword(password);
        userDao.updateByPrimaryKey(user);
    }

    /**
     * 申请加入组织
     * @param user
     * @param companyId
     */
    @Override
    public void joinCompany(TUser user, Long companyId) {
        // 查询组织是否存在
        TCompany company = companyDao.selectByPrimaryKey(companyId);

        if (company == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该组织不存在,加入失败!");
        }

        boolean isUpdate = false;
        // 查询是否有被拒绝记录TODO
        List<TUserCompany> userCompanies = userCompanyDao.selectByUserIdAndCompanyId(user.getId(),companyId);
        Long formerId = null;
        if (!userCompanies.isEmpty()) {
            for (TUserCompany userCompany : userCompanies) {
                if (AppConstant.JOIN_STATE_COMPANY_PASS.equals(userCompany.getState())) { // 如果已经通过
                    throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经加入该组织！请勿重复申请！");
                }
                if (AppConstant.JOIN_STATE_COMPANY_NOT_YET.equals(userCompany.getState())) { // 如果待审核
                    throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您已经提交过申请！请耐心等待!");
                }
                if (AppConstant.JOIN_STATE_COMPANY_REFUSE.equals(userCompany.getState())) { // 已经有一条被拒绝记录
                    formerId = userCompany.getId();
                    isUpdate = true;
                }
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        TUserCompany userCompany = new TUserCompany();
        if (formerId == null) {
            formerId = idGenerator.nextId();
        }
        userCompany.setId(formerId);
        userCompany.setUserId(user.getId());
        userCompany.setTeamName(user.getName());
        userCompany.setCompanyId(companyId);
        userCompany.setCompanyName(companyDao.selectByPrimaryKey(companyId).getName()); // 公司名
        userCompany.setCompanyJob(AppConstant.JOB_COMPANY_MEMBER); // 成员类型
        userCompany.setState(AppConstant.JOIN_STATE_COMPANY_NOT_YET); // 待审核
        Long groupId = null;
        // 查询默认分组
        List<TGroup> groups = groupDao.selectByCompanyIdAndAuth(companyId,AppConstant.GROUP_AUTH_DEFAULT);
        if (!groups.isEmpty()) {
            groupId = groups.get(0).getId();
        }
        userCompany.setGroupId(groupId);
        if (isUpdate) {
            // updater
            userCompany.setUpdateTime(currentTimeMillis);
            userCompany.setUpdateUser(user.getId());
            userCompany.setUpdateUserName(user.getName());
            userCompanyDao.updateByPrimaryKey(userCompany);
        } else {
            // creater & updater
            userCompany.setCreateTime(currentTimeMillis);
            userCompany.setCreateUser(user.getId());
            userCompany.setCreateUserName(user.getName());
            userCompany.setUpdateTime(currentTimeMillis);
            userCompany.setUpdateUser(user.getId());
            userCompany.setUpdateUserName(user.getName());
            userCompanyDao.insert(userCompany);
        }
    }

    /**
     * 通过手机号获取用户(组织账号)
     * @param telephone
     * @return
     */
    private TUser getCompanyAccountByTelephone(String telephone) {
        List<TUser> userList = userDao.selectUserTelByJurisdictionAndIsCompany(telephone,AppConstant.JURISDICTION_NORMAL,AppConstant.IS_COMPANY_ACCOUNT_YES);

        TUser user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
           /* if (AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(user.getAvaliableStatus())) {
                throw new MessageException("当前用户被封禁!禁止登录！");
            }*/
        }
        return user;
    }

    /**
     * 校验邀请码
     * @param inviteCode
     * @return
     */
    private boolean checkInviteCode(String inviteCode) {
        boolean flag = false;

        List<TUser> userList = userDao.selectByInviteCode(inviteCode);
        if (!userList.isEmpty()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能描述: 插入一条签到记录(用户第一次签到)
     * 作者: 许方毅
     * 创建时间: 2018年11月12日 下午5:02:38
     *
     * @param user
     */
    private void insertSignUpInfo(TUser user) {
        // id
        Long id = user.getId();
        // name
        String name = user.getName();

        //构建实体
        TUserTask userTask = new TUserTask();
        userTask.setId(snowflakeIdWorker.nextId());
        userTask.setUserId(id);
        userTask.setType(TaskEnum.TASK_SIGNUP.getType());
        userTask.setTargetNum(1);
        // creater & updater
        long currentTimeMillis = System.currentTimeMillis();
        userTask.setCreateTime(currentTimeMillis);
        userTask.setCreateUser(id);
        userTask.setCreateUserName(name);
        userTask.setUpdateTime(currentTimeMillis);
        userTask.setUpdateUser(id);
        userTask.setUpdateUserName(name);
        userTask.setIsValid(AppConstant.IS_VALID_YES);

        userTaskDao.insert(userTask);
    }


    /**
     * 刷勋缓存
     *
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
     *
     * @param telephone
     * @return
     */
    private List<TUser> getUserByTelephone(String telephone) {
        return userDao.queryUsersByTelephone(telephone);
    }

    /**
     * 技能校验
     *
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


    /**
     * 功能描述: 获取组织账号对应组织编号
     * 作者: 许方毅
     * 创建时间: 2019年1月14日 下午1:45:04
     * @param id
     * @return
     */
    private Long getOwnCompanyId(Long id) {
        List<TUserCompany> userCompanies = userCompanyDao.selectByUserIdAndCompanyjob(id,AppConstant.JOB_COMPANY_CREATER);
        if(userCompanies.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"没有对应组织！");
        }
        return userCompanies.get(0).getCompanyId();
    }


}
