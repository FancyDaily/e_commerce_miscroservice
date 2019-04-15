package com.e_commerce.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqTemplate;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.Category;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.entity.service.TimerScheduler;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.enums.colligate.MqChannelEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.TimerSchedulerTypeEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.commons.util.colligate.*;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.service.impl.BaseService;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.e_commerce.miscroservice.product.service.ProductService;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import com.e_commerce.miscroservice.user.service.GrowthValueService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.service.apiImpl.SendSmsService;
import com.e_commerce.miscroservice.user.vo.*;
import com.e_commerce.miscroservice.user.wechat.service.WechatService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_PASS;
import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_USER_NAME_PREFIX;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Autowired
    private AuthorizeRpcService authorizeRpcService;

    @Autowired
    private SendSmsService smsService;

    @Autowired
    private OrderCommonController orderService;

    @Autowired
    private MessageCommonController messageService;

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
    private TypeRecordDao typeRecordDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProductCommonController productService;

    @Value("${debug}")
    private String debug;

    @Value("${page.invite}")
    private String pageValueInvite;

    @Value("${page.person}")
    private String pageValuePerson;

    @Value("${page.service}")
    private String pageValueService;

    @Value("${page.help}")
    private String pageValueHelp;

    @Value("${page.company}")
    private String pageValueCompany;

    @Value("${page.join}")
    private String pageValueJoin;

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

        if (StringUtil.equals(AppConstant.PAYMENTS_OPTION_OUT, option)) { // 支出
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

        List<TUserFreeze> userFreezes = userFreezeDao.queryUserFreezeDESC(id, lastTime);

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
        user = userDao.selectByPrimaryKey(user.getId());

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
            if (theView.getDetailUrls() != null && theView.getDetailUrls().contains(",")) {    //多张图
                theView.setDetailUrlArray(theView.getDetailUrls().split(","));
            } else if (theView.getDetailUrls() != null && !theView.getDetailUrls().contains(",")) {    //单张图
                String[] array = {theView.getDetailUrls()};
                theView.setDetailUrlArray(array);
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
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public long skillAdd(TUser user, TUserSkill skill) {
        //校验
        skillPass(user, skill, false);

//        skill.setId(idGenerator.nextId()); // 生成主键
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

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                TUser finalUser = userDao.selectByPrimaryKey(user.getId());
                //成长值
                finalUser = taskComplete(finalUser, GrowthValueEnum.GROWTH_TYPE_UNREP_SKILL);

                //维护t_user表 skill字段
                String formerSkills = finalUser.getSkill();
                if (formerSkills == null) {    //一个技能都没有
                    formerSkills = "";
                }

                if (!formerSkills.equals("")) {
                    formerSkills += ",";
                }

                String latestSkills = new StringBuilder(formerSkills).append(skill.getName()).toString();
                finalUser.setSkill(latestSkills);
                //updater
                finalUser.setUpdateTime(System.currentTimeMillis());
                finalUser.setUpdateUser(finalUser.getId());
                finalUser.setUpdateUserName(finalUser.getName());
                userDao.updateByPrimaryKey(finalUser);
                //TODO 刷新缓存

                super.afterCompletion(status);
            }
        });
        return skill.getId();
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

        //维护t_user表 skill字段
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                List<TUserSkill> latestSkills = userSkillDao.queryOnesSkills(user.getId());
                StringBuilder builder = new StringBuilder();
                for (TUserSkill skill : latestSkills) {
                    builder = builder.append(skill.getName()).append(",");
                }
                String skills = builder.toString();
                if (skills.endsWith(",")) {
                    skills = skills.substring(0, skills.length() - 1);
                }
                TUser finalUser = new TUser();
                finalUser = userDao.selectByPrimaryKey(user.getId());
                finalUser.setSkill(skills);
                //updater
                finalUser.setUpdateTime(System.currentTimeMillis());
                finalUser.setUpdateUser(finalUser.getId());
                finalUser.setUpdateUserName(finalUser.getName());
                userDao.updateByPrimaryKey(finalUser);
                //TODO 刷新缓存

                super.afterCompletion(status);
            }
        });
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
        Long userId = user.getId();
        if (orderId == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单Id不能为空!");
        }

        TOrderRelationship orderRelationship = orderService.selectCollectByOrderIdAndUserId(userId,orderId);
        //如果订单关系不存在，创建一条
        if (orderRelationship == null) {
            TOrder order = orderService.selectOrderById(orderId);
            if (order == null) {
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "订单不存在！");
            }
            orderRelationship = new TOrderRelationship();
            orderRelationship.setServiceId(order.getServiceId());
            orderRelationship.setOrderId(order.getId());
            orderRelationship.setServiceType(order.getType());
            orderRelationship.setFromUserId(order.getCreateUser());
            orderRelationship.setReceiptUserId(userId);
            orderRelationship.setSignType(OrderRelationshipEnum.SIGN_TYPE_NO.getType());
            orderRelationship.setStatus(OrderRelationshipEnum.STATUS_NO_STATE.getType());
            orderRelationship.setServiceReportType(OrderRelationshipEnum.SERVICE_REPORT_IS_NO.getType());
            orderRelationship.setOrderReportType(OrderRelationshipEnum.ORDER_REPORT_IS_NO.getType());
            orderRelationship.setServiceCollectionType(OrderRelationshipEnum.SERVICE_COLLECTION_IS_TURE.getType());
            orderRelationship.setServiceCollectionTime(System.currentTimeMillis());
            orderRelationship.setServiceName(order.getServiceName());
            orderRelationship.setStartTime(order.getStartTime());
            orderRelationship.setEndTime(order.getEndTime());
            orderRelationship.setTimeType(order.getTimeType());
            orderRelationship.setCollectTime(order.getCollectTime());
            orderRelationship.setCollectType(order.getCollectType());
            orderRelationship.setCreateUser(order.getCreateUser());
            orderRelationship.setCreateUserName(order.getCreateUserName());
            orderRelationship.setCreateTime(order.getCreateTime());
            orderRelationship.setUpdateUser(order.getCreateUser());
            orderRelationship.setUpdateUserName(order.getCreateUserName());
            orderRelationship.setUpdateTime(order.getCreateTime());
            orderRelationship.setIsValid(AppConstant.IS_VALID_YES);
            //如果为创建者
            if(order.getCreateUser().equals(user.getId())) {
                orderRelationship.setReceiptUserId(null);
            }

            orderService.insertOrderRelationship(orderRelationship);
            return;
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
        if (userId == null) {
            userId = user.getId();
        }

        UserPageView result = new UserPageView();
        //基本信息
        TUser theUser = userDao.selectByPrimaryKey(userId);
        Integer isCompanyAccount = theUser.getIsCompanyAccount();
        if(AppConstant.IS_COMPANY_ACCOUNT_YES.equals(isCompanyAccount)) {   //如果是组织账号
            result.setCompanyAccount(true);
            TCompany ownCompany = getOwnCompany(theUser.getId());
            for(CompanyTypeEnum theEnum:CompanyTypeEnum.values()) {
                if(theEnum.getCode().equals(ownCompany.getType())) {
                    //获取组织性质
                    result.setCompanyType(theEnum.getName());
                    break;
                }
            }
        }
        DesensitizedUserView view = BeanUtil.copy(theUser, DesensitizedUserView.class);
        //关注状态
        Integer attenStatus = userFollowDao.queryAttenStatus(user.getId(), userId);
        view.setIsAtten(attenStatus);
        result.setDesensitizedUserView(view);
        user = userDao.selectByPrimaryKey(user.getId());
        //求助列表
        QueryResult helps = getOnesAvailableItems(userId, 1, 8, false, user);

        //服务列表
        QueryResult services = getOnesAvailableItems(userId, 1, 8, true, user);

        //技能列表
        user = new TUser();
        user.setId(userId);
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
    public QueryResult pageService(Long userId, Integer pageNum, Integer pageSize, boolean isService, TUser me) {
        me = userDao.selectByPrimaryKey(me.getId());
        return getOnesAvailableItems(userId, pageNum, pageSize, isService, me);
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

        List<TOrderRelationship> orderRelationships = orderService.selectEndOrdertionshipListByuserId(userId);
        List<Long> orderIds = new ArrayList<>();
        for (TOrderRelationship orderRelationship : orderRelationships) {
            orderIds.add(orderRelationship.getOrderId());
        }

        //判空
        if (orderIds.isEmpty()) {
            return new QueryResult();
        }

        //分页
        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);

        //查找符合条件的订单记录
        List<TOrder> orders = orderService.selectOrdersInIdsByViewer(orderIds, user);

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

    private QueryResult getOnesAvailableItems(Long userId, Integer pageNum, Integer pageSize, boolean isService, TUser me) {
        List<TOrder> orders = new ArrayList<>();
        Page<Object> startPage = new Page<>();
        startPage = PageHelper.startPage(pageNum, pageSize);
        boolean flag = false;
        if (userId != me.getId()) {    //查看别人的主页
            orders = orderService.selectOdersByUserId(userId, isService, me);
        } else {
            orders = orderService.selectOdersByUserId(userId,isService);
            flag = true;
        }

        List<UserPageServiceVO> resultList = new ArrayList<>();
        for(TOrder order:orders) {
            //查询自己与对方订单的报名状态
            String status = orderService.queryIsReceipt(me.getId(), order.getId(),me.getId());
            UserPageServiceVO copy = BeanUtil.copy(order, UserPageServiceVO.class);
            copy.setReceiptStatus(status);
            resultList.add(copy);
        }

        QueryResult queryResult = new QueryResult();
        queryResult.setTotalCount(startPage.getTotal());
        queryResult.setResultList(resultList);
        return queryResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
//        userFreeze.setId(idGenerator.nextId());
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
     * @param user
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void skillDelete(TUser user, Long id) {
        if (id == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "技能id不能为空");
        }
        userSkillDao.delete(id);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                List<TUserSkill> latestSkills = userSkillDao.queryOnesSkills(user.getId());
                StringBuilder builder = new StringBuilder();
                for (TUserSkill skill : latestSkills) {
                    builder = builder.append(skill.getName()).append(",");
                }
                String skills = builder.toString();
                if (skills.endsWith(",")) {
                    skills = skills.substring(0, skills.length() - 1);
                }

                //同步用户表中的技能
                TUser finalUser = null;
                finalUser = userDao.selectByPrimaryKey(user.getId());    //DELMARK
                finalUser.setSkill(skills);
                //updater
                finalUser.setUpdateTime(System.currentTimeMillis());
                finalUser.setUpdateUser(finalUser.getId());
                finalUser.setUpdateUserName(finalUser.getName());

                userDao.updateByPrimaryKey(finalUser);
                //TODO 刷新缓存
                super.afterCompletion(status);
            }
        });
    }

    @Override
    public DesensitizedUserView info(TUser user, Long userId) {
        if (userId == null) {
            userId = user.getId();
        }
        TUser findUser = userDao.info(userId);
        if (findUser == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该用户不存在！");
        }
        DesensitizedUserView view = BeanUtil.copy(findUser, DesensitizedUserView.class);
        String companyNames = view.getCompanyNames();
        if (companyNames != null) {
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
        }
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
                if (!getUsersByTelephone(telephone).isEmpty()) {
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

        // 如果为修改昵称 -> 同步修改服务表里的创建者昵称
        String name = user.getName();
        if (name != null && !name.equals(idHolder.getName())) {    //进行了修改昵称
            // 调用订单模块的方法 同步修改订单相关昵称
            orderService.synOrderCreateUserName(user.getId(), user.getName());
        }

        final TUser[] finalUser = new TUser[1];
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                finalUser[0] = completeReward(user);    //个人信息完整度奖励

                final TUser tUser = finalUser[0];
                // 如果为组织账号的个人账号,并且进行的是修改手机号操作 => 增加一步，同步修改组织账号的手机号
                if (updateData != null && updateData.getUserTel() != null) {
                    TUser companyAccount = userDao.queryDoppelganger(idHolder); //TODO 查找组织账号
                    if (companyAccount != null && !idHolder.getId().equals(companyAccount.getId())
                            && !idHolder.getUserTel().equals(updateData.getUserTel())) { // 当前为组织账号的个人账号进行手机号修改
                        companyAccount.setUserTel(telephone);
                        // updater
                        companyAccount.setUpdateTime(currentTimeMillis);
                        companyAccount.setUpdateUser(tUser.getId());
                        companyAccount.setUpdateUserName(tUser.getName());
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
                flushRedisUser(token, tUser);

                super.afterCompletion(status);
            }
        });

        return token;
    }


    /**
     * 功能描述: 获得完整度任务奖励(包含判断,包含账单)
     * 作者: 许方毅
     * 创建时间: 2018年11月12日 下午5:56:52
     *
     * @param user
     */
    private TUser completeReward(TUser user) {
        Long userId = user.getId();

        // 与数据库同步
        user = userDao.selectByPrimaryKey(userId);

        // 查询是否有任务完成记录
        List<TUserTask> pageTask = userTaskDao.findTasksByTypeAndUserId(TaskEnum.TASK_PAGE.getType(), user.getId());
        if (!pageTask.isEmpty()) {
            return user;
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
            completeNum += num;
        }
        //公司
        String workPlace = user.getWorkPlace();
        if (workPlace != null && !workPlace.isEmpty()) {
            int num = PersonalIntegrity.COMPANY.getNum();
            completeNum += num;
        }

        //昵称
        String name = user.getName();
        if (name != null && !name.isEmpty()) {
            int num = PersonalIntegrity.NAME.getNum();
            completeNum += num;
        }

        //职位
        String occupation = user.getOccupation();
        if (occupation != null && !occupation.isEmpty()) {
            int num = PersonalIntegrity.POST.getNum();
            completeNum += num;
        }

        String college = user.getCollege();
        if (college != null && !college.isEmpty()) {
            int num = PersonalIntegrity.EDUCATION.getNum();
            completeNum += num;
        }

        String remarks = user.getRemarks();
        if (remarks != null && !remarks.isEmpty()) {
            int num = PersonalIntegrity.REMARKS.getNum();
            completeNum += num;
        }

        if (completeNum >= completeTaskNum) {
            // 获取任务奖励
            Long reward = TaskEnum.TASK_PAGE.getReward();
            taskComplete(user, GrowthValueEnum.GROWTH_TYPE_UNREP_PAGE);  //TODO 成长值相关
        }

        user.setIntegrity(completeNum);
        userDao.updateByPrimaryKey(user);
        return user;
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
        //判穷
        if (bonusPackage.getTime() > (user.getSurplusTime() - user.getFreezeTime())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的余额不足!");
        }

        long currentTimeMillis = System.currentTimeMillis();
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
        //判穷
        if (bonusPackage.getTime() > (user.getSurplusTime() - user.getFreezeTime())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的余额不足!");
        }

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
//        record.setId(idGenerator.nextId());
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
     * @param bonusId {
     *                "success": true,
     *                "errorCode": "",
     *                "msg": "",
     *                "data": {
     *                "id": 103524652990595072,    //红包id
     *                "userId": 68813260748488704, //发布人id
     *                "description": "766468686",  //描述
     *                "time": 100, //金额
     *                "createTime": 1552464600668,
     *                "isValid": "1",
     *                "userHeadPortraitPath": "https://timebank-prod-img.oss-cn-hangzhou.aliyuncs.com/person/15446050826379.png",  //头像
     *                "name": "马晓晨"    //名字
     *                }
     *                }
     * @return
     */
    @Override
    public BonusPackageVIew bonusPackageInfo(TUser user, Long bonusId) {
        if (bonusId == null) {
            return null;
        }
        TBonusPackage info = bonusPackageDao.info(bonusId);
        if (info == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该红包不存在！");
        }
        BonusPackageVIew copy = BeanUtil.copy(info, BonusPackageVIew.class);
        TUser theUser = userDao.selectByPrimaryKey(info.getCreateUser());
        copy.setUserHeadPortraitPath(theUser.getUserHeadPortraitPath());
        copy.setName(theUser.getName());
        return copy;
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
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该红包已失效!");
        }

        if (AppConstant.IS_VALID_NO.equals(bonusRecord.getIsValid())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "红包已失效！");
        }

        //判权
        if (bonusRecord.getCreateUser().equals(user.getId())) {
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
//        userTimeRecord.setId(snowflakeIdWorker.nextId());
        userTimeRecord.setUserId(user.getId());
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
       /* String content = String.format(SysMsgEnum.BONUS_PACKAGE_DONE.getContent(), bonusRecord.getDescription(),
                user.getId());
        Long targetUserId = bonusRecord.getUserId();*/

        //插入一条系统消息 调用order模块的接口
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
    public QueryResult collectList(TUser user, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        List<TOrderRelationship> orderRelationships = orderService.selectCollectList(user.getId());

        List<Long> idList = new ArrayList<>();
        Map<Long,Long> collectTimeMap = new HashMap<>();
        for (TOrderRelationship orderRelationship : orderRelationships) {
            Long orderId = orderRelationship.getOrderId();
            idList.add(orderId);
            collectTimeMap.put(orderId,orderRelationship.getServiceCollectionTime());
        }

        if (idList.isEmpty()) {
            return new QueryResult<>();
        }

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TOrder> orders = orderService.selectOrdersInOrderIdsInStatus(idList, AppConstant.COLLECTION_AVAILABLE_STATUS_ARRAY);

        List<Long> productIds = new ArrayList<>();
        for(TOrder order:orders) {
            productIds.add(order.getServiceId());
        }
        //获取封面图
        Map<Long, String> productCoverPic = productService.getProductCoverPic(productIds);
        List<CollectionView> collectionViews = new ArrayList<>();
        for(TOrder order:orders) {
            Long serviceId = order.getServiceId();
            String coverPic = productCoverPic.get(serviceId);
            Long collectionTime = collectTimeMap.get(order.getId());
            CollectionView collectionView = BeanUtil.copy(order, CollectionView.class);
            collectionView.setCoverPic(coverPic);
            if(collectionTime!=null) {
                collectionView.setCollectionTime(DateUtil.timeStamp2Date(collectionTime));
            }
            collectionViews.add(collectionView);
        }

        QueryResult queryResult = new QueryResult();
        queryResult.setResultList(collectionViews);
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
    @Transactional(rollbackFor = Throwable.class)
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
//        userAuth.setId(String.valueOf(idGenerator.nextId()));
        // creater
        Long timeStamp = System.currentTimeMillis();
        userAuth.setUserId(user.getId());
        userAuth.setCardId(cardId);
        userAuth.setCardName(cardName);
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
        Map<String, Object> birAgeSex = IDCardUtil.getBirAgeSex(cardId);
        user.setBirthday(Long.valueOf((String) birAgeSex.get("birthday")));

        // updater
        user.setUpdateTime(timeStamp);
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());

        // 插入系统消息
//      insertSysMsg(user.getId(), SysMsgEnum.AUTH.getTitle(), SysMsgEnum.AUTH.getContent()); //插入系统消息

        // 实名认证奖励(插入账单流水记录)
//        insertReward(user, PaymentEnum.PAYMENT_TYPE_CERT_BONUS);  //TODO 插入实名认证奖励(插入账单流水记录)

        // 实名认证任务完成(插入任务记录)
//        addMedal(user, DictionaryEnum.TASK_AUTH.getType(), DictionaryEnum.TASK_AUTH.getSubType(),   ////TODO 插入实名认证奖励(插入任务记录)
//                AppConstant.TARGET_ID_TASK_AUTH);

        //TODO 成长值记录
        taskComplete(user, GrowthValueEnum.GROWTH_TYPE_UNREP_AUTH);

        userDao.updateByPrimaryKey(user);

        user = userDao.selectByPrimaryKey(user.getId());

//      flushRedisUser(token, user);  //刷新缓存
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
        ifAuthCompany(user, company);
        // 插入或更新一条待审核的企业记录
        insertOrUpdateCompany(user, company);
    }

    private void ifAuthCompany(TUser user, TCompany company) {
//		String code = company.getCode();
        String name = company.getName();
        if (StringUtil.isEmpty(name)) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "公司名称不能为空");
        }

        List<TCompany> companies = companyDao.selectExistUserCompany(name, user.getId(), AppConstant.CORP_CERT_STATUS_YES);
        if (companies != null && companies.size() > 0) {
            throw new MessageException("公司已存在!请联系管理员邀请加入!");
        }
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
//        company.setId(idGenerator.nextId());
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
     * 每日签到
     *
     * @param token
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
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

            targetNum = count % 7;
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
                if (StringUtil.equals(AppConstant.SIGN_UP_ALMOST_HALF_EDGE, dayCountStr)) { // 2 -> 特殊
                    // 特殊奖励
                    special = 3; // 特殊奖励
                    reward = special;
                }

                // 处理奖励
                if (StringUtil.equals(AppConstant.SIGN_UP_ALMOST_EDGE, dayCountStr)) { // 6 -> 特殊
                    // 特殊奖励
                    special = 5; // 特殊奖励
                    reward = special;
                }
            }

            // else,计数=1,给出普通奖励
            if (StringUtil.equals(AppConstant.LAST_SIGN_UP_DAY_OTHERS, status)) {
                targetNum = 1;
            }

            userTask.setTargetNum(targetNum);
//            userTask.setId(idGenerator.nextId());
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
            userTask.setId(null);
            userTaskDao.insert(userTask);
        }

        // 从未签到
        if (flag) {
            // 插入一条新的记录
            insertSignUpInfo(user);
        }

        //插入一条成长值流水
        insertGrowthValueRecords(user, GrowthValueEnum.GROWTH_TYPE_REP_SIGN_UP, reward);

        //成长值 & 等级提升 & 授信额度提升
        levelUp(user, (int) reward);

        return reward;
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
     * 插入一条任务记录
     *
     * @param user
     * @param taskEnum
     * @param special
     */
    private void insertTaskRecords(TUser user, TaskEnum taskEnum, Long special) {
        long currentTimeMillis = System.currentTimeMillis();
        TUserTask userTask = new TUserTask();
//        userTask.setId(idGenerator.nextId());
        userTask.setUserId(user.getId());
        userTask.setType(taskEnum.getType());
        //creater & updater
        userTask.setTargetId(String.valueOf(taskEnum.getType()));
        userTask.setCreateTime(currentTimeMillis);
        userTask.setCreateUser(user.getId());
        userTask.setCreateUserName(user.getName());
        userTask.setUpdateTime(currentTimeMillis);
        userTask.setUpdateUser(user.getId());
        userTask.setUpdateUserName(user.getName());
        userTask.setIsValid(AppConstant.IS_VALID_YES);
        userTaskDao.insert(userTask);
    }

    /**
     * 插入一条成长值记录
     *
     * @param user
     * @param growthValueEnum
     * @param reward
     */
    private void insertGrowthValueRecords(TUser user, GrowthValueEnum growthValueEnum, Long reward) {
        long currentTimeMillis = System.currentTimeMillis();
        TTypeRecord typeRecord = new TTypeRecord();
//        typeRecord.setId(idGenerator.nextId());
        typeRecord.setUserId(user.getId());
        typeRecord.setType(growthValueEnum.getCode());
        typeRecord.setSubType(growthValueEnum.getSubCode());
        typeRecord.setTitle(growthValueEnum.getMessage());
        typeRecord.setContent(growthValueEnum.getMessage());
        if (reward == null) {
            reward = Long.valueOf(growthValueEnum.getPrice());
        }
        typeRecord.setNum(reward);
        //creater & updater
        typeRecord.setCreateTime(currentTimeMillis);
        typeRecord.setCreateUser(user.getId());
        typeRecord.setCreateUserName(user.getName());
        typeRecord.setUpdateTime(currentTimeMillis);
        typeRecord.setUpdateUser(user.getId());
        typeRecord.setUpdateUserName(user.getName());
        typeRecord.setIsValid(AppConstant.IS_VALID_YES);
        typeRecordDao.insert(typeRecord);
    }

    /**
     * 用户反馈
     *
     * @param user
     */
    @Override
    public void feedBack(TUser user, TReport report) {
        final long currentTimeMillis = System.currentTimeMillis();
        //creater & updater
        report.setType(ReportEnum.TYPE_SYSTEM.getType());
        report.setStatus(ReportEnum.STATUS_PENDING_DISPOSAL.getType());
        report.setCreateUser(user.getId());
        report.setCreateUserName(user.getName());
        report.setCreateTime(currentTimeMillis);
        report.setUpdateUser(user.getId());
        report.setUpdateUserName(user.getName());
        report.setUpdateTime(currentTimeMillis);
        report.setIsValid(AppConstant.IS_VALID_YES);
        orderService.saveTreport(report);

    }

    /**
     * 红包退回
     *
     * @param user
     * @param bonusPackageId
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void sendBackBonusPackage(TUser user, Long bonusPackageId) {
        //修改红包记录
        TBonusPackage bonusPackage = bonusPackageDao.selectByPrimaryKey(bonusPackageId);
        //红包不存在
        if (bonusPackage == null || AppConstant.IS_VALID_NO.equals(bonusPackage.getIsValid())) {
            return;
        }
        bonusPackage.setId(bonusPackageId);
        bonusPackage.setIsValid(AppConstant.IS_VALID_NO);
        bonusPackageDao.updateByPrimaryKey(bonusPackage);
        long currentTimeMillis = System.currentTimeMillis();
        //插入退款流水
        TUserTimeRecord userTimeRecord = new TUserTimeRecord();
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

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                //返还红包金额
                TUser finalUser = null;
                finalUser = userDao.selectByPrimaryKey(user.getId());
                finalUser.setSurplusTime(finalUser.getSurplusTime() + bonusPackage.getTime());
                finalUser.setCreateTime(currentTimeMillis);
                finalUser.setCreateUser(finalUser.getId());
                finalUser.setCreateUserName(finalUser.getName());
                finalUser.setUpdateTime(currentTimeMillis);
                finalUser.setUpdateUser(finalUser.getId());
                finalUser.setUpdateUserName(finalUser.getName());
                userDao.updateByPrimaryKey(finalUser);

                super.afterCompletion(status);
            }
        });
    }

    /**
     * 获取key-value值
     *
     * @param key
     * @return
     */
    @Override
    public TPublish getPublishValue(String key) {
        String value = messageService.getValue(key);
        TPublish publish = new TPublish();
//        publish.setValue("[{\"id\":\"2100001\",\"name\":\"bug\"},{\"id\":\"2100002\",\"name\":\"建议\"},{\"id\":\"2100003\",\"name\":\"五星好评\"}]");
        publish.setValue(value);
        return publish;
    }

    /**
     * 发送短信
     *
     * @param telephone
     * @return
     */
    @Override
    public AjaxResult genrateSMSCode(String telephone) {
        AjaxResult result = new AjaxResult();
        Long interval = getUserTokenInterval(); // TODO 可以修改时间周期
/*
        // 如果存在
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

*/

        // 查询是否未到预设的发送间隔 => 有效时间为600秒而发送间隔为60s
        if (redisUtil.hasKey(telephone)) {
            long expire = redisUtil.getExpire(telephone); // 获取剩余时间(为0则永久有效)
            long expectedTime = AppConstant.SMS_EXPIRED - AppConstant.SMS_INTERVAL_MILLIS / 1000;
            if (expire > expectedTime) {
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,
                        AppConstant.SMS_INTERVAL_MILLIS / 1000 + "秒内请勿重复发送短信验证码!");
            }
        }

        // 生成6位随机数
        String validCode = "666666";
        if (ApplicationContextUtil.isDevEnviron()) { // 表示当前运行环境为生产
            validCode = UUIDGenerator.messageCode();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", telephone);
        params.put(AppConstant.VALID_CODE, validCode);

        String resMsg;
        if (!ApplicationContextUtil.isDevEnviron()) { // 表示当前运行环境为调试
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
     * 发送短信(指定内容)
     *
     * @param telephone
     * @return
     */
    @Override
    public boolean genrateSMSWithContent(String telephone, String content) {
        boolean result = false;
        // 生成6位随机数
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", telephone);
        params.put("content", content);

        String resMsg;
        if (!ApplicationContextUtil.isDevEnviron()) { // 表示当前运行环境为调试
            resMsg = "true";
        } else {
            resMsg = smsService.sendServMsg(params);
        }

        if (StringUtil.equals("true", resMsg)) {
            result = true;
        }

        return result;
    }

    /**
     * 校验短信验证码
     *
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
     *
     * @param inviterId
     * @param mineId
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void payInviter(Long inviterId, Long mineId) {
        // 查询是否已经回馈完成
        List<TTypeDictionaries> dics = typeDictionariesDao.selectInviteRecords(mineId, inviterId);
        if (dics != null && !dics.isEmpty()) {
            return;
        }

        // 加入邀请人记录
//        Long targetId = idGenerator.nextId();
        TTypeDictionaries dictionaries = new TTypeDictionaries();
//        dictionaries.setId(targetId);
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

        // 回馈
        // 使邀请人获得成长值奖励
        TUser inviter = userDao.selectByPrimaryKey(inviterId);
        inviter = taskComplete(inviter, GrowthValueEnum.GROWTH_TYPE_REP_INVITE);//TODO 任务完成

        // TODO 刷新缓存
        String key = "str" + inviterId;
        if (redisUtil.hasKey(key)) {
            String inviterToken = (String) redisUtil.get(key);
            flushRedisUser(inviterToken, inviter);
        }
    }

    /**
     * 分享（查看二维码）
     *
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
                //成长值
                taskComplete(user, GrowthValueEnum.GROWTH_TYPE_REP_SHARE_PRODUCT);
                break;

            case "3":
                scene = String.valueOf(inviterId) + "," + "" + "," + String.valueOf(serviceId);
                page = pageValueHelp;
//                serviceDetail = serviceService.serviceDetail(Long.valueOf(serviceId), user);    //TODO 调用订单模块的商品或者订单详情接口
                subType = DictionaryEnum.SHARE_HELP.getSubType();
                //成长值
                taskComplete(user, GrowthValueEnum.GROWTH_TYPE_REP_SHARE_PRODUCT);
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

        TTypeDictionaries dictionaries = new TTypeDictionaries();
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

        // 刷新缓存
        flushRedisUser(token, user);

        return serviceView;
    }

    /**
     * 微信授权基本信息更新
     *
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
     *
     * @param scene
     * @return
     */
    @Override
    public SceneView scene(Long scene) {
        if (scene == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "scene不能为空！");
        }
        List<TTypeDictionaries> dictionaries = typeDictionariesDao.selectByIdAndIsValid(scene, AppConstant.IS_VALID_YES);
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
     *
     * @param token
     * @param inviteCode
     */
    @Override
    public void generateInviteCode(String token, String inviteCode) {
        TUser user = UserUtil.getUser(token);
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
     *
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
     *
     * @param user
     * @param companyId
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void joinCompany(TUser user, Long companyId) {
        // 查询组织是否存在
        TCompany company = companyDao.selectByPrimaryKey(companyId);

        if (company == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该组织不存在,加入失败!");
        }

        boolean isUpdate = false;
        // 查询是否有被拒绝记录TODO
        List<TUserCompany> userCompanies = userCompanyDao.selectByUserIdAndCompanyId(user.getId(), companyId);
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
        userCompany.setId(formerId);
        userCompany.setUserId(user.getId());
        userCompany.setTeamName(user.getName());
        userCompany.setCompanyId(companyId);
        userCompany.setCompanyName(companyDao.selectByPrimaryKey(companyId).getName()); // 公司名
        userCompany.setCompanyJob(AppConstant.JOB_COMPANY_MEMBER); // 成员类型
        userCompany.setState(AppConstant.JOIN_STATE_COMPANY_NOT_YET); // 待审核
        Long groupId = null;
        // 查询默认分组
        List<TGroup> groups = groupDao.selectByCompanyIdAndAuth(companyId, AppConstant.GROUP_AUTH_DEFAULT);
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
     * 组织时间轨迹
     *
     * @param user
     * @param year
     * @param month
     * @param type
     * @return
     */
    @Override
    public CompanyPaymentView queryPayment(TUser user, String year, String month, String type) {
        // 与数据库同步   //TODO 测试 年月起止时间戳有点问题
        user = userDao.selectByPrimaryKey(user.getId());

        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);
        // 判空
        Map<String, Object> monthBetween = new HashMap<>();
        Long monthStartStamp = System.currentTimeMillis();
        // 处理年月信息
        if (year != null && month != null) {
            monthStartStamp = Long.valueOf(DateUtil.dateToStamp(year + "-" + month + "-" + "1"));
        }
        monthBetween = DateUtil.getMonthBetween(monthStartStamp);
        Long beginStamp = Long.valueOf((String) monthBetween.get("begin"));
        Long endStamp = Long.valueOf((String) monthBetween.get("end"));

        List<SinglePaymentView> freezeView = new ArrayList<>();
        if ("3".equals(type)) { // 冻结
            List<TUserFreeze> userFreezes = userFreezeDao.selectByUserIdBetween(userId, beginStamp, endStamp);
            if (!userFreezes.isEmpty()) {
                for (TUserFreeze userFreeze : userFreezes) {
                    SinglePaymentView view = BeanUtil.copy(userFreeze, SinglePaymentView.class);
                    view.setIdString(String.valueOf(userFreeze.getId()));
                    view.setServIdString(String.valueOf(userFreeze.getOrderId())); // 订单id
                    view.setServiceName(userFreeze.getServiceName()); // 名称
                    view.setType("求助");// TODO 冻结都是求助类型
                    view.setTime(DateUtil.timeStamp2Seconds(userFreeze.getCreateTime())); // 发生日期
                    view.setTotalTime(Integer.valueOf(String.valueOf(userFreeze.getFreezeTime()))); // 冻结时间
                    view.setPayOrGainString(timeChange(userFreeze.getFreezeTime())); // TODO 格式化的冻结时间(不带正负号)
                    freezeView.add(view);
                }
            }
        }

        // 查询详细
        List<TUserTimeRecord> userTimeRecords = userTimeRecordDao.selectTimeRecordByUserIdBetweenASC(userId, beginStamp, endStamp);
        // 收入列表
        List<TUserTimeRecord> inList = new ArrayList<>();
        // 支出列表
        List<TUserTimeRecord> outList = new ArrayList<>();
        // 最终结果
        CompanyPaymentView view = new CompanyPaymentView();
        // target_id为键
//		Map<Long, Map<String,Object>> joinerMap = new HashMap<Long, Map<String,Object>>();
//		Map<String, Object> map = new HashMap<String,Object>();
        // 键值为target_id
        Map<Long, List<Long>> joinerMap = new HashMap<>();
        Map<Long, List<String>> joinerNameMap = new HashMap<>();
        Map<Long, TOrder> receiptMap = new HashMap<>();
        Map<Long, Integer> totalMap = new HashMap<>();

        List<Long> targetIdList = new ArrayList<>(); // 包含已经添加进最终结果的id

        int monthly_in = 0;
        int monthly_out = 0;
        // 遍历赋值
        for (TUserTimeRecord userTimeRecord : userTimeRecords) {
            Long targetId = userTimeRecord.getTargetId();
            int count = 0; // 对同一分组统计金额
            if (totalMap.containsKey(targetId)) {
                count = totalMap.get(targetId);
            }
            Long joinerId = null;
            // 收入
            if (userId.equals(userTimeRecord.getUserId())) {
                joinerId = userTimeRecord.getFromUserId();
            }
            // 支出
            if (userId.equals(userTimeRecord.getFromUserId())) {
                joinerId = userTimeRecord.getUserId();
            }
            List<Long> joinerIds = new ArrayList<>();
            List<String> joinerNameList = new ArrayList<>();
            // 判断map
            if (joinerMap.containsKey(targetId)) { // 获取原集合(添加元素)
                joinerIds = joinerMap.get(targetId);
                joinerNameList = joinerNameMap.get(targetId);
            }
            joinerIds.add(joinerId);
            String name = userDao.selectByPrimaryKey(joinerId).getName();
            joinerNameList.add(name);
            // 根据targetId找到订单信息
            TOrder order = orderService.selectOrderById(targetId);
            // 加入或刷新map
            joinerNameMap.put(targetId, joinerNameList);
            joinerMap.put(targetId, joinerIds);
            receiptMap.put(targetId, order);

            // 收入
            if (userId.equals(userTimeRecord.getUserId())) {
                monthly_in += userTimeRecord.getTime();
                inList.add(userTimeRecord);
            }
            // 支出
            if (userId.equals(userTimeRecord.getFromUserId())) {
                monthly_out += userTimeRecord.getTime();
                outList.add(userTimeRecord);
            }
            count += userTimeRecord.getTime();
            totalMap.put(targetId, count);

        }
        // 处理与合并
        List<SinglePaymentView> finalInlist = new ArrayList<>();
        List<SinglePaymentView> finalOutList = new ArrayList<>();
        for (int i = inList.size() - 1; i >= 0; i--) {
            TUserTimeRecord in = inList.get(i);
            Long targetId = in.getTargetId();
            // 判断targetId是否在列
            if (!targetIdList.contains(targetId)) {
                TOrder order = receiptMap.get(targetId);
                SinglePaymentView thView = BeanUtil.copy(in, SinglePaymentView.class);
                thView.setIdString(String.valueOf(in.getId())); // 没什么用的流水id
                thView.setServiceName(order.getServiceName());// 服务名字
                thView.setServReceiptStatus(String.valueOf(order.getStatus()));// 订单状态
                thView.setTime(DateUtil.timeStamp2Seconds(in.getCreateTime())); // 最后产生金额的时间（同一服务当月）
                thView.setJoinMembers(joinerNameMap.get(targetId));
                thView.setServIdString(String.valueOf(order.getId())); // TODO 订单id（或应当为服务id）
                thView.setType(AppConstant.SERV_TYPE_HELP.equals(order.getType()) ? "求助" : "服务");
                thView.setPayOrGainString("+" + timeChange(Long.valueOf(totalMap.get(targetId))));
                finalInlist.add(thView);
                targetIdList.add(targetId);
            }
        }
        for (int i = outList.size() - 1; i >= 0; i--) {
            TUserTimeRecord out = outList.get(i);
            Long targetId = out.getTargetId();
            // 判断targetId是否在列
            if (!targetIdList.contains(targetId)) {
                TOrder order = receiptMap.get(targetId);
                SinglePaymentView thisView = BeanUtil.copy(out, SinglePaymentView.class);
                thisView.setIdString(String.valueOf(out.getId())); // 没什么用的流水id
                thisView.setServiceName(order.getServiceName());// 服务名字
                thisView.setServReceiptStatus(String.valueOf(order.getStatus()));// 订单状态
                thisView.setTime(DateUtil.timeStamp2Seconds(out.getCreateTime())); // 最后产生金额的时间（同一服务当月）
                thisView.setJoinMembers(joinerNameMap.get(targetId));
                thisView.setServIdString(String.valueOf(order.getId())); // TODO 订单id（或应当为服务id）
                thisView.setType(AppConstant.SERV_TYPE_HELP.equals(order.getType()) ? "求助" : "服务");
                thisView.setPayOrGainString("-" + timeChange(Long.valueOf(totalMap.get(targetId))));
                finalOutList.add(thisView);
                targetIdList.add(targetId);
            }
        }
        // 合并
        List<SinglePaymentView> totalList = new ArrayList<>();
        for (SinglePaymentView in : finalInlist) {
            totalList.add(in);
        }
        for (SinglePaymentView out : finalOutList) {
            totalList.add(out);
        }

        // 装载最终结果
        view.setAvailable_time((int) (user.getSurplusTime() - user.getFreezeTime()));
        view.setMonthly_in(monthly_in);
        view.setMonthly_out(monthly_out);
        view.setFreeze_time(Integer.valueOf(String.valueOf(user.getFreezeTime())));

        List<SinglePaymentView> finalList = new ArrayList<>();

        // 判断是全部、in还是out
        if ("0".equals(type)) {
            finalList = totalList;
        } else if ("1".equals(type)) {
            finalList = finalInlist;
        } else if ("2".equals(type)) {
            finalList = finalOutList;
        } else if ("3".equals(type)) {
            finalList = freezeView;
        }
        // 排序
        Collections.sort(finalList, new Comparator<SinglePaymentView>() {

            @Override
            public int compare(SinglePaymentView o1, SinglePaymentView o2) {
                return (int) (o2.getCreateTime() - o1.getCreateTime());
            }
        });

        view.setSinglePaymentViews(finalList);

        return view;
    }

    /**
     * 任务完成(增加成长值，并未插入任务记录)
     *
     * @param user            成长值增加对象
     * @param growthValueEnum 枚举类型
     */
    @Override
    public TUser taskComplete(TUser user, GrowthValueEnum growthValueEnum) {
        //根据类型校验最大值、当日最大值
        try {
            growthValueEnum = checkMax(growthValueEnum, user);
        } catch (MessageException e) {
            return user;
        }

        //成长值奖励（成长值流水、成长值提升、等级提升、授信额度提升）
        Integer price = growthValueEnum.getPrice(); //数额

        //插入一条成长值流水
        insertGrowthValueRecords(user, growthValueEnum);

        //成长值 & 等级提升 & 授信额度提升
        return levelUp(user, price);
    }

    /**
     * 任务完成(批量) -> 增加成长值、插入任务记录等
     *
     * @param user            成长值增加对象
     * @param growthValueEnum 枚举类型
     */
    @Override
    public TUser taskComplete(TUser user, GrowthValueEnum growthValueEnum, Integer counts) {
        //根据类型校验最大值、当日最大值
        counts = checkMax(growthValueEnum, user, counts);

        //成长值奖励（成长值流水、成长值提升、等级提升、授信额度提升）
        Integer price = growthValueEnum.getPrice() * counts; //数额

        //插入多条成长值流水
        for (int i = 0; i < counts; i++) { //TODO 可能修改为批量插入
            insertGrowthValueRecords(user, growthValueEnum);
        }

        //成长值 & 等级提升 & 授信额度提升
        return levelUp(user, price);
    }

    /**
     * 确认是否为我的红包
     *
     * @param user
     * @param bonusPackageId
     * @return
     */
    @Override
    public Map<String, Object> isMyBonusPackage(TUser user, Long bonusPackageId) {
        if (bonusPackageId == null || user == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isMine", bonusPackageDao.isMine(user.getId(), bonusPackageId));
        return resultMap;
    }

    /**
     * 组织版登录
     *
     * @param telephone
     * @param password
     * @return
     */
    @Override
    public Map<String, Object> loginGroupByPwd(String telephone, String password) {
        // 对前端给的password作处理(AES)
        password = AESCommonUtil.encript(password);
        TUser user = null;

        List<TUser> users = userDao.selectByUserTelByPasswordByIsCompanyAccYes(telephone, password, AppConstant.IS_COMPANY_ACCOUNT_YES);
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        if (user == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户名或密码错误,或非组织账号");
        }
        // 登录
        Long userId = user.getId();

        // 使得之前的token失效
        String redisKey = "str" + userId;
        if (redisUtil.hasKey(redisKey)) {
            String lastToken = (String) redisUtil.get(redisKey);
            redisUtil.del(lastToken);
        }

        String token = genToken(user);
        redisUtil.set(token, user, getUserTokenInterval());
        redisUtil.set(String.valueOf(user.getId()), user, getUserTokenInterval());

        redisUtil.set(redisKey, token, getUserTokenInterval()); // 登录状态的凭证
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(AppConstant.USER_TOKEN, token);
        // String化
        DesensitizedUserView userView = BeanUtil.copy(user, DesensitizedUserView.class);
        userView.setIdStr(String.valueOf(userView.getId()));
        resultMap.put(AppConstant.USER, userView);

        return resultMap;
    }

    /**
     * 手机号验证码登录(个人账号)
     *
     * @param telephone
     * @param validCode
     * @param uuid      设备号
     * @return
     */
    @Override
    public Map<String, Object> loginUserBySMS(String telephone, String validCode, String uuid) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean isRegister = false;
        checkSMS(telephone, validCode);
        TUser user = new TUser();
        TUser tmpUser = null;
        if (!isUser(telephone)) { // 注册并登录
            user.setUserTel(telephone);
            // 注册
            user.setDeviceId(uuid);
            tmpUser = rigester(user);
            isRegister = true;
        }
        user = getUserAccountByTelephone(telephone);

        if (tmpUser != null) {
            user.setToken(tmpUser.getToken());
        }

        //处理封禁
        if (AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(user.getAvaliableStatus())) {
            throw new MessageException("当前用户被封禁!禁止登录！");
        }

        //激活假用户
        if (AppConstant.IS_FAKE_YES.equals(user.getIsFake())) { // 如果为假用户
            user.setIsFake(AppConstant.IS_FAKE_NO); // TODO 真实用户
            userDao.updateByPrimaryKey(user);
        }

        // 登录
        Long userId = user.getId();

        // 使得之前的token失效 //TODO
        String redisKey = "str" + userId;
        if (redisUtil.hasKey(redisKey)) {
            String lastToken = (String) redisUtil.get(redisKey);
            redisUtil.del(lastToken);
        }

        String token = genToken(user);
        redisUtil.set(token, user, getUserTokenInterval());
        redisUtil.set(String.valueOf(user.getId()), user, getUserTokenInterval());
        redisUtil.set(redisKey, token, getUserTokenInterval()); // 登录状态的凭证
        resultMap.put(AppConstant.USER_TOKEN, token);
        // String化
        DesensitizedUserView userView = BeanUtil.copy(user, DesensitizedUserView.class);
        userView.setIdStr(String.valueOf(userView.getId()));
        resultMap.put(AppConstant.USER, userView);

        /*//设置token
        if (user.getToken() == null) {
            Token tokenDto = authorizeRpcService.load(DEFAULT_USER_NAME_PREFIX + user.getId(), DEFAULT_PASS, uuid);
            if (tokenDto != null && tokenDto.getToken() != null && !"".equals(tokenDto.getToken())) {
                user.setToken(tokenDto.getToken());
                //设置token
                resultMap.put(com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil.TOKEN, user.getToken());
            }
        }*/
        resultMap.put("isRegister",false);
        if(isRegister) {
            resultMap.put("isRegister",true);
        }
        return resultMap;
    }

    /**
     * 注册用户
     *
     * @param user
     */
    @Override
    public TUser rigester(TUser user) {
        // 默认昵称
        String defaultName = RandomUtil.getDefaultName();
        // 默认账号
        String defaultAccount = defaultName.substring(2);

        if (user == null) {
            user = new TUser();
        }
        user.setUserAccount(defaultAccount);
        user.setJurisdiction(AppConstant.JURISDICTION_NORMAL);
        user.setAccreditStatus(AppConstant.ACCREDIT_STATUS_DEFAULT);
        user.setAvaliableStatus(AppConstant.AVALIABLE_STATUS_AVALIABLE);
        user.setRemarks("");

        // 头像、性别、昵称等从微信获取的字段
        if (StringUtil.isEmpty(user.getUserHeadPortraitPath())) {
            user.setUserHeadPortraitPath(AppConstant.DEFAULT_HEADURL);
        }

        if (!(user.getSex() != null && user.getSex() > -1)) {
            user.setSex(AppConstant.DEFAULT_SEX);
        }

        if (StringUtil.isEmpty(user.getName())) {
            user.setName(RandomUtil.getDefaultName());
        }

        // nums
        user.setGrowthValue(0l); // 成长值
        user.setFollowNum(0);
        user.setReceiptNum(0);

        user.setSeekHelpNum(0);
        user.setServeNum(0);
        user.setSeekHelpPublishNum(0);
        user.setServePublishNum(0);
        user.setPayNum(0);

        user.setServTotalEvaluate(0);
        user.setServCreditEvaluate(0);
        user.setServMajorEvaluate(0);
        user.setServAttitudeEvaluate(0);
        user.setHelpTotalEvaluate(0);
        user.setHelpCreditEvaluate(0);
        user.setHelpMajorEvaluate(0);
        user.setHelpAttitudeEvaluate(0);

        // 冻结金额
        user.setFreezeTime(0L);
        // 总余额
        user.setSurplusTime(0l);

        // 实名相关
        user.setAuthenticationStatus(AppConstant.DEFAULT_AUTH_STATUS);
        user.setAuthenticationType(AppConstant.DEFAULT_AUTH_TYPE);

        // others
        user.setUserPicturePath(AppConstant.DEFAULT_BACKGROUNDPIC);
        user.setOccupation(AppConstant.DEFAULT_OCCUPATION);
        user.setMaxEducation(AppConstant.DEFAULT_EDUCATION);
        user.setLevel(AppConstant.DEFAULT_LEVEL);
        user.setMasterStatus(AppConstant.MASTER_STATUS_DEFAULT); // 达人标记

        // creater & updater
        long currentTimeMillis = System.currentTimeMillis();
        user.setCreateTime(System.currentTimeMillis());
        user.setCreateUserName(user.getName());
        user.setUpdateTime(currentTimeMillis);
        user.setUpdateUserName(user.getName());

        // 有效性
        user.setIsValid(AppConstant.IS_VALID_YES);

        // 插入一条用户记录
        userDao.insert(user);
        user.setCreateUser(user.getId());
        user.setUpdateUser(user.getId());

        // 插入注册的系统消息
        messageService.messageSave(null, user, AppConstant.NOTICE_TITLE_RIGESTER, AppConstant.NOTICE_CONTENT_RIGESTER, user.getId(), currentTimeMillis);

        // 注册完成任务
        taskComplete(user, GrowthValueEnum.GROWTH_TYPE_UNREP_REGISTER);

        //注册用户中心数据
        Token token = authorizeRpcService.reg(DEFAULT_USER_NAME_PREFIX + user.getId(), DEFAULT_PASS, user.getId().toString(), user.getDeviceId(), Boolean.FALSE);

        if (token != null&&token.getToken()!=null) {
            user.setToken(token.getToken());
        }

        return user;
    }

    private String genToken(TUser user) {
        String val = String.valueOf(user.getId());
        String token = TokenUtil.genToken(val);
        return token;
    }

    /**
     * 是否为已注册用户(不区分个人还是组织账号)
     *
     * @param telephone
     * @return
     */
    private boolean isUser(String telephone) {
        boolean result = false;
        List<TUser> userList = userDao.selectByTelephone(telephone);
        if (userList != null && !userList.isEmpty()) {
            result = true;
        }
        return result;
    }

    private Integer checkMax(GrowthValueEnum growthValueEnum, TUser user, Integer counts) {

        //原始基数
        Integer originalCounts = counts;

        //判空
        if (growthValueEnum == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "类型为空！");
        }

        if (counts < 1) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "数量不合法！");
        }

        int code = growthValueEnum.getCode();

        Integer superCountTotal = 0;
        Integer superCountToday = 0;
        Integer countTotal = 0;
        Integer countToday = 0;
        GrowthValueEnum superGrowthValueEnum = null;

        // 日期信息
        long currentTimeMillis = System.currentTimeMillis();
        Long beginStamp = DateUtil.getStartStamp(currentTimeMillis);
        Long endStamp = DateUtil.getEndStamp(currentTimeMillis);

        List<Integer> needTOConfirmList = Arrays.asList(AppConstant.GROWTH_VALUE_ENUM_NEED_CONFIRM_ARRAY);
        //如果为特定类型
        if (needTOConfirmList.contains(code)) {
            if (code == GrowthValueEnum.GROWTH_TYPE_REP_HELP_DONE.getCode() || code == GrowthValueEnum.GROWTH_TYPE_REP_SERV_DONE.getCode()) {  //如果为互助完成
                superGrowthValueEnum = GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_ITEM_DONE;
            } else if (code == GrowthValueEnum.GROWTH_TYPE_REP_COMMENT.getCode()) {
                superGrowthValueEnum = GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_COMMENT;
            } else if (code == GrowthValueEnum.GROWTH_TYPE_REP_PUBLIC_WELFARE_ACTY_DONE.getCode()) {
                superGrowthValueEnum = GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_PUBLIC_WELFARE_ACTY_DONE;
            }
        }
        List<TTypeRecord> typeRecords = null;

        if (superGrowthValueEnum != null) {
            typeRecords = typeRecordDao.selectByTypeAndUserId(growthValueEnum.getCode(), superGrowthValueEnum.getCode(), user.getId());
        } else {
            typeRecords = typeRecordDao.selectByTypeAndUserId(growthValueEnum.getCode(), user.getId());
        }

        //遍历筛选次数
        for (TTypeRecord typeRecord : typeRecords) {
            Integer num = typeRecord.getNum().intValue();
            if (!typeRecord.getType().equals(growthValueEnum.getCode())) {    //一次型
                superCountTotal += num;
                if (typeRecord.getCreateTime() >= beginStamp && typeRecord.getCreateTime() < endStamp) {
                    superCountToday += num;
                }
            } else {    //重复
                countTotal += num;
                if (typeRecord.getCreateTime() >= beginStamp && typeRecord.getCreateTime() < endStamp) {
                    countToday += num;
                }
            }
        }

        boolean flag = false;
        Integer dailyMaxIn = growthValueEnum.getDailyMaxIn();//TODO dailyMaxIn是否要减少(首天是否可以获得6次奖励) 可能借助于redis
        Integer maxIn = growthValueEnum.getMaxIn();
        if (maxIn == -1) {
            maxIn = 2147483647;
        }
        if (countTotal >= maxIn || countToday >= dailyMaxIn) {   //总或者今日达到上限
            counts = 0;
        } else {
            int inteval = dailyMaxIn - countToday;
            int countsNum = counts * growthValueEnum.getPrice();
            int resultCounts = inteval / growthValueEnum.getPrice();
            if (inteval > 0 && countsNum - inteval >= 0) { //任务当前未完成，且次数在不被消耗的前提下能够完成任务
                flag = true;
            }
            counts = countsNum < inteval ? counts : resultCounts;
        }

        boolean superFlag = false;
        if (superGrowthValueEnum != null) { //特定类型
            if (counts > 0 && superCountTotal < superGrowthValueEnum.getMaxIn() && superCountToday < superGrowthValueEnum.getDailyMaxIn()) {   //如果一次型没有记录存在
                //插入一条成长值流水
                insertGrowthValueRecords(user, superGrowthValueEnum);
                if (originalCounts <= growthValueEnum.getDailyMaxIn() / growthValueEnum.getPrice()) {
                    counts--; //消耗掉一次插入机会
                }
                superFlag = true;
                //TODO dailyMaxIn是否要减少(首天是否可以获得6次奖励) 可能借助于redis

                //TODO 插入一条任务完成的流水。superGrowthValueEnum对应的任务类型
                insertTaskRecords(user, superGrowthValueEnum.getTaskCode());
            }
            if (!superFlag && flag) { //次数不被消耗的前提下(已完成首次任务)能够完成任务
                //TODO 插入一条任务完成的流水。growthValueEnum对应的任务类型
                insertTaskRecords(user, growthValueEnum.getTaskCode());
            }
        } else {
            if (flag) {
                //TODO 插入一条任务完成的流水。growthValueEnum对应的任务类型
                insertTaskRecords(user, growthValueEnum.getTaskCode());
            }
        }
        return counts;
    }

    private void insertTaskRecords(TUser user, int taskCode) {
        //只要是能插入记录，就是没完成，在上一级以及校验过任务完成情况
        long currentTimeMillis = System.currentTimeMillis();
        TUserTask userTask = new TUserTask();
//        userTask.setId(idGenerator.nextId());
        userTask.setUserId(user.getId());
        userTask.setType(taskCode);

        // 遍历枚举
        Integer value = null;
        for (GrowthValueEnum growthValueEnum : GrowthValueEnum.values()) {
            if (growthValueEnum.getTaskCode() == taskCode) {
                value = growthValueEnum.getPrice();
                break;
            }
        }
        userTask.setValue(String.valueOf(value));
        userTask.setTargetId(String.valueOf(taskCode));
        //creater & updater
        userTask.setCreateTime(currentTimeMillis);
        userTask.setCreateUser(user.getId());
        userTask.setCreateUserName(user.getName());
        userTask.setUpdateTime(currentTimeMillis);
        userTask.setUpdateUser(user.getId());
        userTask.setUpdateUserName(user.getName());
        userTask.setIsValid(AppConstant.IS_VALID_YES);
        userTaskDao.insert(userTask);
    }

    private TUser levelUp(TUser user, Integer price) {
        //增加成长值之前同步数据
//		user = userDao.selectByPrimaryKey(user.getId());	//TODO

        Long growthValue = user.getGrowthValue();

        //成长值增加
        user.setGrowthValue(growthValue + price);

        //等级提升
        Integer level = user.getLevel();
        //授信提升
        Long creditLimit = user.getCreditLimit();
        if (growthValue >= LevelEnum.LEVEL_ONE.getMin() && growthValue < LevelEnum.LEVEL_ONE.getMax()) {
            level = LevelEnum.LEVEL_ONE.getLevel();
            creditLimit = LevelEnum.LEVEL_ONE.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_TWO.getMin() && growthValue < LevelEnum.LEVEL_TWO.getMax()) {
            level = LevelEnum.LEVEL_TWO.getLevel();
            creditLimit = LevelEnum.LEVEL_TWO.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_THREE.getMin() && growthValue < LevelEnum.LEVEL_THREE.getMax()) {
            level = LevelEnum.LEVEL_THREE.getLevel();
            creditLimit = LevelEnum.LEVEL_THREE.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_FOUR.getMin() && growthValue < LevelEnum.LEVEL_FOUR.getMax()) {
            level = LevelEnum.LEVEL_FOUR.getLevel();
            creditLimit = LevelEnum.LEVEL_FOUR.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_FIVE.getMin() && growthValue < LevelEnum.LEVEL_FIVE.getMax()) {
            level = LevelEnum.LEVEL_FIVE.getLevel();
            creditLimit = LevelEnum.LEVEL_FIVE.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_SIX.getMin() && growthValue < LevelEnum.LEVEL_SIX.getMax()) {
            level = LevelEnum.LEVEL_SIX.getLevel();
            creditLimit = LevelEnum.LEVEL_SIX.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_SEVEN.getMin() && growthValue < LevelEnum.LEVEL_SEVEN.getMax()) {
            level = LevelEnum.LEVEL_SEVEN.getLevel();
            creditLimit = LevelEnum.LEVEL_SEVEN.getCredit();
        } else if (growthValue >= LevelEnum.LEVEL_EIGHT.getMin()) {
            level = LevelEnum.LEVEL_EIGHT.getLevel();
            creditLimit = LevelEnum.LEVEL_EIGHT.getCredit();
        }

        user.setLevel(level);
        user.setCreditLimit(creditLimit);

        //更新
        //updater
        user.setUpdateTime(System.currentTimeMillis());
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());
        userDao.updateByPrimaryKey(user);
        return user;
    }

    private GrowthValueEnum checkMax(GrowthValueEnum growthValueEnum, TUser user) {
        Long userId = user.getId();
        //判空
        if (growthValueEnum == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "类型为空！");
        }

        int code = growthValueEnum.getCode();

        // 日期信息
        String today = DateUtil.timeStamp2Date(System.currentTimeMillis());
        Map<String, Object> ym2BetweenStamp = DateUtil.ym2BetweenStamp(today);
        Long beginStamp = Long.valueOf((String) ym2BetweenStamp.get("begin"));
        Long endStamp = Long.valueOf((String) ym2BetweenStamp.get("end"));

        List<Integer> needTOConfirmList = Arrays.asList(AppConstant.GROWTH_VALUE_ENUM_NEED_CONFIRM_ARRAY);

        //如果为特定类型
        if (needTOConfirmList.contains(code)) {
            if (code == GrowthValueEnum.GROWTH_TYPE_REP_HELP_DONE.getCode() || code == GrowthValueEnum.GROWTH_TYPE_REP_SERV_DONE.getCode()) {  //如果为互助完成
                growthValueEnum = GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_ITEM_DONE;
            } else if (code == GrowthValueEnum.GROWTH_TYPE_REP_COMMENT.getCode()) {
                growthValueEnum = GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_COMMENT;
            } else if (code == GrowthValueEnum.GROWTH_TYPE_REP_PUBLIC_WELFARE_ACTY_DONE.getCode()) {
                growthValueEnum = GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_PUBLIC_WELFARE_ACTY_DONE;
            }
            boolean flag = true;
            //查询成长值记录
            List<TTypeRecord> typeRecords = typeRecordDao.selectByTypeAndUserId(growthValueEnum.getCode(), userId);

            Integer maxIn = growthValueEnum.getMaxIn();
            Integer dayMaxIn = growthValueEnum.getDailyMaxIn();
            Long total = 0l;
            Long dayTotal = 0l;
            //判断总额
            for (TTypeRecord typeRecord : typeRecords) {
                total += typeRecord.getNum();
                if (typeRecord.getCreateTime() >= beginStamp && typeRecord.getCreateTime() < endStamp) { //统计今日的成长值流水
                    dayTotal += typeRecord.getNum();
                }
            }

            //判断是否超出总额
            if (maxIn != -1 && total >= maxIn) {
                flag = false;
            }

            //判断是否超出今日总额
            if (dayMaxIn != -1 && dayTotal >= dayMaxIn) {
                flag = false;
            }

            if (dayMaxIn != -1) {
                if (dayMaxIn - dayTotal < growthValueEnum.getPrice()) { //本次将要完成任务 super
                    //TODO 插入一条任务完成的记录
                    insertTaskRecords(user, growthValueEnum.getTaskCode());
                }
            }

            if (flag) {
                return growthValueEnum;
            }
        }

        //查询成长值记录
        List<TTypeRecord> typeRecords = typeRecordDao.selectByTypeAndUserId(growthValueEnum.getCode(), userId);

        Integer maxIn = growthValueEnum.getMaxIn();
        Integer dayMaxIn = growthValueEnum.getDailyMaxIn();
        Long total = 0l;
        Long dayTotal = 0l;
        //判断总额
        for (TTypeRecord typeRecord : typeRecords) {
            total += typeRecord.getNum();
            if (typeRecord.getCreateTime() >= beginStamp && typeRecord.getCreateTime() < endStamp) { //统计今日的成长值流水
                dayTotal += typeRecord.getNum();
            }
        }

        //判断是否超出总额
        if (maxIn != -1 && total >= maxIn) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该类型已经达到成长值获取上限！");
        }

        //判断是否超出今日总额
        if (dayMaxIn != -1 && dayTotal >= dayMaxIn) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该类型已经达到成长值获取今日上限！");
        }

        if (dayMaxIn != -1) {
            if (dayTotal < dayMaxIn && dayTotal + growthValueEnum.getPrice() >= dayMaxIn) { //本次将要完成任务
                //TODO 插入一条任务完成的记录
                insertTaskRecords(user, growthValueEnum.getTaskCode());
            }
        }

        return growthValueEnum;
    }

    private int insertGrowthValueRecords(TUser user, GrowthValueEnum growthValueEnum) {
        long currentTimeMillis = System.currentTimeMillis();
        TTypeRecord typeRecord = new TTypeRecord();
//        typeRecord.setId(idGenerator.nextId());
        typeRecord.setUserId(user.getId());
        typeRecord.setType(growthValueEnum.getCode());
        typeRecord.setSubType(growthValueEnum.getSubCode());
        typeRecord.setTitle(growthValueEnum.getMessage());
        typeRecord.setContent(growthValueEnum.getMessage());
        typeRecord.setNum(Long.valueOf(growthValueEnum.getPrice()));
        //creater & updater
        typeRecord.setCreateTime(currentTimeMillis);
        typeRecord.setCreateUser(user.getId());
        typeRecord.setCreateUserName(user.getName());
        typeRecord.setUpdateTime(currentTimeMillis);
        typeRecord.setUpdateUser(user.getId());
        typeRecord.setUpdateUserName(user.getName());
        typeRecord.setIsValid(AppConstant.IS_VALID_YES);
        return typeRecordDao.insert(typeRecord);
    }

    /**
     * 通过手机号获取用户(组织账号)
     *
     * @param telephone
     * @return
     */
    private TUser getCompanyAccountByTelephone(String telephone) {
        List<TUser> userList = userDao.selectUserTelByJurisdictionAndIsCompany(telephone, AppConstant.JURISDICTION_NORMAL, AppConstant.IS_COMPANY_ACCOUNT_YES);

        TUser user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
            /*if (AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(user.getAvaliableStatus())) {
                throw new MessageException("当前用户被封禁!禁止登录！");
            }*/
        }
        return user;
    }

    /**
     * 校验邀请码
     *
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
//        userTask.setId(snowflakeIdWorker.nextId());
        userTask.setUserId(id);
        userTask.setType(TaskEnum.TASK_SIGN_UP.getType());
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
     * 刷新缓存
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
    public List<TUser> getUsersByTelephone(String telephone) {
        return userDao.queryUsersByTelephone(telephone);
    }

    /**
     * 任务大厅
     *
     * @param user
     * @return
     */
    @Override
    public TaskHallView taskHall(TUser user) {
        //获取等级、成长值信息
        user = userDao.selectByPrimaryKey(user.getId());
        LevelView levelView = new LevelView();
        levelView.setGrowthValue(user.getGrowthValue());
        Integer level = user.getLevel();
        levelView.setLevelNum(level);
        LevelEnum currentLevel = null;
        LevelEnum nextLevel = null;
        boolean flag = false;
        for (LevelEnum levelEnum : LevelEnum.values()) {
            if (levelEnum.getLevel().equals(level)) {
                currentLevel = levelEnum;
                flag = true;
                continue;
            }
            if (flag) {
                nextLevel = levelEnum;
                break;
            }
        }
        levelView.setLevelName(currentLevel.getName());
        if (nextLevel != null) {
            levelView.setUpToLevelUp(nextLevel.getMin() - user.getGrowthValue());
        }

        //获取url
        String url = "";
        String key = "levelMedal";
        String value = messageService.getValue(key);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<LevelMedalView> levelMedalViews = objectMapper.readValue(value, new TypeReference<List<LevelMedalView>>() {
            });
            for (LevelMedalView levelMedal : levelMedalViews) {
                if (levelMedal.getLevelNum().equals(currentLevel.getLevel())) {
                    url = levelMedal.getUrl();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("解析字典表" + key + "关键字的json出错，" + e.getMessage());
        }

        levelView.setUrl(url);    //等级对应勋章地址
        TaskHallView taskHallView = new TaskHallView();
        taskHallView.setLevelView(levelView);

        //任务信息
        Map<String, Object> taskMap = queryTasks(user);
        List<NoobTask> noobTasks = (List<NoobTask>) taskMap.get("noobTasks");
        List<DailyTask> dailyTasks = (List<DailyTask>) taskMap.get("dailyTasks");

        Map<Long, DailyTask> doneTaskMap = new HashMap<>();
        for (NoobTask noobTask : noobTasks) {
            DailyTask dailyTask = new DailyTask();
            dailyTask.setDone(noobTask.isDone());
            if (!noobTask.isDone()) {
                dailyTask.setCurrentNum(0);
            } else {
                dailyTask.setCurrentNum(1);
            }
            doneTaskMap.put(noobTask.getTargetId(), dailyTask);
        }

        for (DailyTask dailyTask : dailyTasks) {
            doneTaskMap.put(dailyTask.getTargetId(), dailyTask);
        }

        //装载任务数据
        String taskKey = "task";
        String taskValue = messageService.getValue(taskKey);
        List<DailyTask> resultList = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<DailyTask> dailyTaskList = objectMapper.readValue(taskValue, new TypeReference<List<DailyTask>>() {
            });
            for (DailyTask task : dailyTaskList) {
                DailyTask dailyTask = doneTaskMap.get(task.getTargetId());
                if (dailyTask != null) {
                    task.setDone(dailyTask.isDone());
                    task.setCurrentNum(dailyTask.getCurrentNum());
                }
                resultList.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("解析字典表" + key + "关键字的json出错，" + e.getMessage());
        }

        noobTasks = new ArrayList<NoobTask>();
        dailyTasks = new ArrayList<DailyTask>();
        final List<Integer> noobTaskList = Arrays.asList(AppConstant.NOOB_TASK_ARRAY);
        for (DailyTask task : resultList) {
            if (noobTaskList.contains(task.getTargetId().intValue())) {
                NoobTask noobTask = new NoobTask();
                noobTask.setName(task.getName());
                noobTask.setBonus(task.getBonus());
                noobTask.setTargetId(task.getTargetId());
                noobTask.setDone(task.isDone());
                noobTasks.add(noobTask);
            } else {
                dailyTasks.add(task);
            }
        }

        taskHallView.setNoobTasks(noobTasks);
        taskHallView.setDailyTasks(dailyTasks);

        return taskHallView;
    }

    private Map<String, Object> queryTasks(TUser user) {
        Map<Integer, TaskEnum> taskEnumMap = new HashMap<>();
        Map<Integer, Integer> growthTaskCodeMap = new HashMap<>();
        for(GrowthValueEnum growthValueEnum:GrowthValueEnum.values()) {
            growthTaskCodeMap.put(growthValueEnum.getCode(),growthValueEnum.getTaskCode());
        }
        for (TaskEnum taskEnum : TaskEnum.values()) {
            taskEnumMap.put(taskEnum.getType(), taskEnum);
        }

        //结果
        Map<String, Object> resultMap = new HashMap<>();
        List<NoobTask> noobTasks = new ArrayList<>();
        List<DailyTask> dailyTasks = new ArrayList<>();
        Integer[] noobTaskArray = AppConstant.NOOB_TASK_ARRAY;
//      Integer[] dailyTaskArray = AppConstant.DAILY_TASK_ARRAY;
        List<Integer> noobTaskList = Arrays.asList(noobTaskArray);
//      List<Integer> dailyTaskList = Arrays.asList(dailyTaskArray);

        long currentTimeMillis = System.currentTimeMillis();
        Long beginStamp = DateUtil.getStartStamp(currentTimeMillis);
        Long endStamp = DateUtil.getEndStamp(currentTimeMillis);

        List<TTypeRecord> dailyGrowthRecords = growthValueService.findOnesGrowthRecords(user.getId());
        //map
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, NoobTask> noobTaskMap = new HashMap<>();
        Map<Integer, DailyTask> dailyTaskMap = new HashMap<>();
        //遍历装载map
        for (TTypeRecord dailyGrowthRecord : dailyGrowthRecords) {
            if (!noobTaskList.contains(dailyGrowthRecord.getType())) {   //如果为非菜鸟类型
                if (!(dailyGrowthRecord.getCreateTime() > beginStamp && dailyGrowthRecord.getCreateTime() < endStamp)) { //如果不是今天
                    continue;
                }
            }
            Integer counts = map.get(dailyGrowthRecord.getType());
            if (counts == null) {
                counts = 0;
            }
            map.put(growthTaskCodeMap.get(dailyGrowthRecord.getType()), ++counts);
        }

        List<TUserTask> tasks = userTaskDao.findOnesTasks(user.getId());
        //遍历筛选出新手任务、日常任务
        for (TUserTask task : tasks) {
            Integer type = task.getType();
            TaskEnum taskEnum = taskEnumMap.get(type);
            String targetId = task.getTargetId();
            if (noobTaskList.contains(type)) {
                NoobTask noobTask = new NoobTask();
                noobTask.setTargetId(Long.valueOf(targetId));
                noobTask.setName(taskEnum.getDesc());
                noobTask.setBonus(taskEnum.getReward().intValue());
                noobTask.setDone(true);
                noobTaskMap.put(type, noobTask);
                continue;
            }
            if(task.getCreateTime() > beginStamp && task.getCreateTime() < endStamp) {
                DailyTask dailyTask = new DailyTask();
                dailyTask.setTargetId(Long.valueOf(type));
                dailyTask.setName(taskEnum.getDesc());
                dailyTask.setBonus(taskEnum.getReward().intValue());
                dailyTask.setTotalNum(taskEnum.getDailyMaxNum());
                dailyTask.setCurrentNum(taskEnum.getDailyMaxNum());
                if (dailyTask.getTotalNum().equals(dailyTask.getCurrentNum())) {
                    dailyTask.setDone(true);
                }
                dailyTaskMap.put(type, dailyTask);
            }
        }

        for (TaskEnum taskEnum : TaskEnum.values()) {
            Integer type = taskEnum.getType();
            NoobTask noobTask = noobTaskMap.get(type);  //菜鸟任务
            DailyTask dailyTask = dailyTaskMap.get(type);
            if (noobTask != null) {
                noobTasks.add(noobTask);
            }
            if (dailyTask != null) {
                dailyTasks.add(dailyTask);
            }
            //如果两者都不存在
            Integer counts = map.get(type); //成长值记录
            if (noobTask == null && dailyTask == null) {
                if (noobTaskList.contains(type)) { //菜鸟任务
                    noobTask = new NoobTask();
                    if (counts != null && counts != 0) {
                        noobTask.setTargetId(taskEnum.getTargetId());
                        noobTask.setName(taskEnum.getDesc());
                        noobTask.setBonus(taskEnum.getReward().intValue());
                        noobTask.setDone(true);
                        noobTasks.add(noobTask);
                    }
                } else {    //日常任务
                    if (counts != null && counts != 0) {
                        Integer dailyMaxNum = taskEnum.getDailyMaxNum();
                        dailyTask = new DailyTask();
                        dailyTask.setTargetId(taskEnum.getTargetId());
                        dailyTask.setName(taskEnum.getDesc());
                        dailyTask.setBonus(taskEnum.getReward().intValue());
                        dailyTask.setCurrentNum(counts);
                        dailyTask.setTotalNum(taskEnum.getDailyMaxNum());
                        if (counts >= dailyMaxNum) {
                            dailyTask.setDone(true);
                        }
                        dailyTasks.add(dailyTask);
                    }
                }
            }
        }
        resultMap.put("noobTasks", noobTasks);
        resultMap.put("dailyTasks", dailyTasks);
        return resultMap;
    }

    /**
     * 根据手机号获取个人账号
     *
     * @param telephone
     * @return
     */
    @Override
    public TUser getUserAccountByTelephone(String telephone) {
        List<TUser> userList = userDao.selectUserTelByJurisdiction(telephone, AppConstant.JURISDICTION_NORMAL);
        TUser user = null;
        if (userList != null && !userList.isEmpty()) {
            for (TUser thisUser : userList) {
                if (!AppConstant.IS_COMPANY_ACCOUNT_YES.equals(thisUser.getIsCompanyAccount())) {
                    user = thisUser;
                }
            }
           /* if (AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(user.getAvaliableStatus())) {   //TODO 封禁
                throw new MessageException("当前用户被封禁!禁止登录！");
            }*/
        }
        return user;
    }

    /**
     * 用户登出
     *
     * @param token
     */
    @Override
    public void logOut(String token) {
        if (StringUtil.isNotEmpty(token) && redisUtil.hasKey(token)) {
            TUser user = UserUtil.getUser(token);
            String redisKey = "str" + user.getId();
            redisUtil.del(redisKey);// 删除登录凭证
            redisUtil.del(token);// 删除访问凭证
        }
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
        if (name.length() > 18) {
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
     * 获取组织账号对应组织编号
     * @param id
     * @return
     */
    private Long getOwnCompanyId(Long id) {
        List<TUserCompany> userCompanies = userCompanyDao.selectByUserIdAndCompanyjob(id, AppConstant.JOB_COMPANY_CREATER);
        if (userCompanies.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "没有对应组织！");
        }
        return userCompanies.get(0).getCompanyId();
    }

    /**
     * 获取组织账号对应组织实体
     * @param id
     * @return
     */
    private TCompany getOwnCompany(Long id) {
        List<TUserCompany> userCompanies = userCompanyDao.selectByUserIdAndCompanyjob(id, AppConstant.JOB_COMPANY_CREATER);
        if (userCompanies.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "没有对应组织！");
        }
        Long companyId = userCompanies.get(0).getCompanyId();
        return companyDao.selectByPrimaryKey(companyId);
    }

    @Override
    public void addPublishTimes(TUser user, int type) {
        TUser tUser = new TUser();
        tUser.setId(user.getId());
        int seekHelpPublishNum = user.getSeekHelpPublishNum();
        int servicePublishNum = user.getServePublishNum();
        if (ProductEnum.TYPE_SEEK_HELP.getValue() == type) {
            tUser.setSeekHelpPublishNum(seekHelpPublishNum + 1);
        } else {
            tUser.setServePublishNum(servicePublishNum + 1);
        }
        userDao.updateByPrimaryKey(tUser);
    }

    @Override
    public boolean isCareUser(Long userId, Long userFollowId) {
        return userFollowDao.countUserFollow(userId, userFollowId).equals(1L) ? true : false;
    }

    /**
     * 组织每日时间流水查询(今日需求、服务情况)
     *
     * @param user
     * @return
     */
    @Override
    public CompanyDailyPaymentView queryPaymentToDay(TUser user) {
        //与今日有关的所有订单以及收支情况
        user = userDao.selectByPrimaryKey(user.getId());

        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);
        // 获取今日起止时间戳
        long currentTimeMillis = System.currentTimeMillis();
        long startStamp = DateUtil.getStartStamp(currentTimeMillis);
        long endStamp = DateUtil.getEndStamp(currentTimeMillis);

        List<TOrder> orders = orderService.selectDailyOrders(userId);    //查找当天派生出的订单

        //构建订单ids
        List<Long> orderIds = new ArrayList<>();

        //初始化 orderId-order map && serviceId-paymentView map
        Map<Long, TOrder> orderMap = new HashMap<>();
        Map<Long, PaymentView> paymentViewMap = new HashMap<>();

        //装载orderId-order map
        for (TOrder order : orders) {
            //装载订单ids
            orderIds.add(order.getId());

            //装载订单
            orderMap.put(order.getId(), order);
            Long serviceId = order.getServiceId();
            PaymentView paymentView = paymentViewMap.get(serviceId);
            if (paymentView == null) {
                paymentView = new PaymentView();
            }

            //装载数据
            if (paymentView.getIdString() == null) {
                paymentView.setIdString(String.valueOf(order.getServiceId()));
                paymentView.setServiceType(order.getType());
                paymentView.setServiceName(order.getServiceName());
                paymentView.setCollectTime(order.getCollectTime());
                paymentView.setServiceTypeName(getServiceValue(order.getServiceTypeId()));
            }

            //装载参与人数（确认人选）
            Integer confirmNum = paymentView.getConfirmNum();
            if (confirmNum == null) {
                confirmNum = 0;
            }
            confirmNum += order.getConfirmNum();
            paymentView.setConfirmNum(confirmNum);
            paymentViewMap.put(serviceId, paymentView);
        }

        if(orderIds.isEmpty()) {
            return new CompanyDailyPaymentView();
        }

        List<TUserTimeRecord> userTimeRecords = userTimeRecordDao.selectByUserIdInOrderIds(userId, orderIds);  //查找当日生成的流水

        //初始化 serviceId-payOrGainNum map
        Map<Long, Long> payOrGainNumMap = new HashMap<>();
        for (TUserTimeRecord userTimeRecord : userTimeRecords) {
            Long time = userTimeRecord.getTime();
            Long targetId = userTimeRecord.getTargetId();    //订单id
            TOrder order = orderMap.get(targetId);
            Long serviceId = order.getServiceId();
            Long payOrGainNum = payOrGainNumMap.get(serviceId);
            if (payOrGainNum == null) {
                payOrGainNum = 0l;
            }
            if(userTimeRecord.getCreateTime()>=startStamp && userTimeRecord.getCreateTime()<=endStamp) {
                payOrGainNum += time;
            }
            payOrGainNumMap.put(serviceId, payOrGainNum);
        }

        //初始化结果集
        List<PaymentView> serviceViews = new ArrayList<>();
        List<PaymentView> helpViews = new ArrayList<>();

        //装载每项商品赚取或支出总时间 并 划分服务求助
        for (Long serviceId : paymentViewMap.keySet()) {
            //装载每项商品赚取或支出总时间
            Long payOrGainNum = payOrGainNumMap.get(serviceId);
            if(payOrGainNum==null) {
                payOrGainNum = 0l;
            }
            PaymentView paymentView = paymentViewMap.get(serviceId);
            paymentView.setTotalTime(payOrGainNum);    //总收入或者支出时间
            //区分服务还是求助
            if (ProductEnum.TYPE_SEEK_HELP.getValue() == paymentView.getServiceType().intValue()) {    //求助
                helpViews.add(paymentView);
                continue;
            }
            serviceViews.add(paymentView);
        }

        //装载结果
        CompanyDailyPaymentView resultView = new CompanyDailyPaymentView();
        resultView.setHelpPaymentViews(helpViews);
        resultView.setServPaymentViews(serviceViews);

        return resultView;
    }

    /**
     * 直接创建一个红包
     * @param user
     * @param bonusPackage
     * @return
     */
    @Override
    public TBonusPackage generateBonusPackage(TUser user, TBonusPackage bonusPackage) {
        //余额变动
        user = userDao.selectByPrimaryKey(user.getId());    //最新数据

        Long time = bonusPackage.getTime();
        Long currentMills = System.currentTimeMillis();
        //判穷
        if (time > (user.getSurplusTime() - user.getFreezeTime())) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "您的余额不足!");
        }

        long currentTimeMillis = System.currentTimeMillis();
        bonusPackage.setUserId(user.getId());
        bonusPackage.setCreateTime(currentTimeMillis);
        bonusPackage.setUpdateTime(currentTimeMillis);
        bonusPackage.setCreateUser(user.getId());
        bonusPackage.setUpdateUser(user.getId());
        bonusPackage.setCreateUserName(user.getName());
        bonusPackage.setUpdateUserName(user.getName());
        bonusPackage.setIsValid(AppConstant.IS_VALID_YES);
        bonusPackageDao.insert(bonusPackage);

        user.setSurplusTime(user.getSurplusTime() - time);
        //updater
        user.setUpdateTime(currentMills);
        user.setUpdateUser(user.getId());
        user.setUpdateUserName(user.getName());
        userDao.updateByPrimaryKey(user);

        //流水
        TUserTimeRecord record = new TUserTimeRecord();
        record.setFromUserId(user.getId());
        record.setTime(time);
        record.setType(PaymentEnum.PAYMENT_TYPE_BONUS_PACKAGE_OUT.getCode());
        record.setTargetId(bonusPackage.getId());
        //creater & updater
        record.setCreateTime(currentMills);
        record.setCreateUser(user.getId());
        record.setCreateUserName(user.getName());
        record.setUpdateTime(currentMills);
        record.setUpdateUser(user.getId());
        record.setUpdateUserName(user.getName());
        record.setIsValid(AppConstant.IS_VALID_YES);
        userTimeRecordDao.insert(record);

        TimerScheduler timerScheduler = new TimerScheduler();
        timerScheduler.setType(TimerSchedulerTypeEnum.BONUS_PACKAGE_SEND_BACK_TASK.toNum());
        timerScheduler.setName("红包" + UUID.randomUUID().toString());
        String ymdStr = DateUtil.timeStamp2Seconds(currentTimeMillis + DateUtil.interval);
        String[] split = ymdStr.split(" ");
        String[] ymd = split[0].split("-");
        String[] times = split[1].split(":");
        String month = ymd[1];
        if(month.startsWith("0")) {
            month = month.substring(1,month.length());
        }
        StringBuilder appender = new StringBuilder().append(times[2]).append(" ").append(times[1]).append(" ").append(times[0]).append(" ");
        String cron = appender.append(ymd[2]).append(" ").append(month).append(" ? ").append(ymd[0]).toString();

        timerScheduler.setCron(cron);

        Map<String,Object> param = new HashMap<>();
        param.put("bonusPackage",bonusPackage);
        timerScheduler.setParams(JSON.toJSONString(param));

        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName() ,JSONObject.toJSONString(timerScheduler));

        return bonusPackage;
    }

    /**
     * 获取服务类型名
     *
     * @param serviceTypeId 服务类型id
     * @return
     */
    private String getServiceValue(Long serviceTypeId) {
        String url = "";
        String key = "category";
        ObjectMapper objectMapper = new ObjectMapper();
        String value = messageService.getValue(String.valueOf(key));
        //解析json
        try {
            List<Category> listType = objectMapper.readValue(value, new TypeReference<List<Category>>() {
            });
            for (int i = 0; i < listType.size(); i++) {
                Category jsonEntity = listType.get(i);
                if (Long.parseLong(jsonEntity.getId()) == serviceTypeId.longValue()) {
                    return jsonEntity.getSortName();
                }
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("解析字典表allType关键字的json出错，" + e.getMessage());
            return "";
        }
    }

    /**
     * 功能描述: 通过手机号获取用户
     * 作者: 许方毅
     * 创建时间: 2018年10月29日 下午2:57:03
     * @param telephone
     * @return
     */
    @Override
    public TUser getUserByTelephone(String telephone) {
        List<TUser> userList = userDao.selectByTelephoneAndJurisdiction(telephone, AppConstant.JURISDICTION_NORMAL);
        TUser user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
        }
        return user;
    }

    /**
     * 功能描述: 根据用户姓名返回多个用户id
     * 作者: 许方毅
     * 创建时间: 2018年12月17日 下午4:25:13
     * @param param
     * @return
     */
    @Override
    public List<Long> getUsersByName(String param) {
        List<TUser> users = userDao.selectByName(param);
        List<Long> userIds = new ArrayList<>();
        for (TUser user : users) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    /**
     * 功能描述: 查询所有用户
     * 作者: 许方毅
     * 创建时间: 2018年12月18日 下午2:30:19
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<UserDetailView> list(String param, Integer pageNum, Integer pageSize) {
        // 分页
        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        QueryResult<UserDetailView> queryResult = new QueryResult<UserDetailView>();

        // 初始化返回集
        List<TUser> userList = new ArrayList<TUser>();

        // 处理请求参数
        String regex_num = "^\\d+$";
        String regex_tel = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";
        if (param == null) { // TODO 空串或多个空格
            userList = userDao.selectByJurisdictionAndCreateTimeDesc(AppConstant.JURISDICTION_NORMAL);
        } else {
            if (param.matches(regex_num)) {
                if (param.matches(regex_tel)) { // 手机号
                    TUser user = getUserByTelephone(param);
                    if(user!=null) {
                        userList.add(user);
                    }
                } else { // 服务/求助ID
                    TOrder order = orderService.selectOrderById(Long.valueOf(param));
                    TUser user = userDao.selectByPrimaryKey(order.getCreateUser());
                    if(user!=null && AppConstant.JURISDICTION_NORMAL.equals(user.getJurisdiction())) {	//只展示普通用户
                        userList.add(user);
                    }
                }
            } else { // 姓名
                // 初始化查询条件
                userList = userDao.selectByNameAndJurisdictionCreateTimeDesc(param,AppConstant.JURISDICTION_NORMAL);
            }
        }

        List<UserDetailView> viewList = new ArrayList<UserDetailView>();
        // 装载身份证信息
        List<TUserAuth> userAuths = userAuthDao.selectAll();
        Map<String, Object> authMap = new HashMap<String, Object>();
        for (TUserAuth userAuth : userAuths) {
            authMap.put(String.valueOf(userAuth.getUserId()), userAuth.getCardId());
        }

        // String化
        for (TUser user : userList) {
            UserDetailView view = BeanUtil.copy(user, UserDetailView.class);
            view.setIdString(String.valueOf(view.getId()));
            // 对用户可用性作健壮处理
            if (view.getAvaliableStatus() == null) {
                view.setAvaliableStatus(AppConstant.AVALIABLE_STATUS_AVALIABLE);
            }

            // 获取身份证号码
            String cardId = (String) authMap.get(String.valueOf(user.getId()));
            if (cardId != null) {
                view.setCardId(cardId);
            } else {
                view.setCardId("无");
            }
            viewList.add(view);
        }

		/*//按注册时间倒序
		Collections.sort(viewList, new Comparator<UserDetailView>() {

			@Override
			public int compare(UserDetailView o1, UserDetailView o2) {
				return (int) (o2.getCreateTime() - o1.getCreateTime());
			}

		});*/

        queryResult.setResultList(viewList);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }

    /**
     * 功能描述: 查看用户详情
     * 作者: 许方毅
     * 创建时间: 2018年12月18日 下午4:32:36
     * @param userId
     * @return
     */
    @Override
    public UserDetailView info(Long userId) {
        TUser user = userDao.selectByPrimaryKey(userId);
        UserDetailView view =BeanUtil.copy(user, UserDetailView.class);
        view.setIdString(String.valueOf(view.getId()));
        // 评价(平均分)
        Double averageScore = 0.0;
        if (user.getServeNum() != 0) {
            averageScore = (user.getServAttitudeEvaluate() + user.getServMajorEvaluate() + user.getServAttitudeEvaluate()) / 3.0
                    / user.getServeNum();
        }
        view.setAverageScore(averageScore);
        // 可用状态(健壮)
        if (user.getAvaliableStatus() == null) {
            view.setAvaliableStatus(AppConstant.AVALIABLE_STATUS_AVALIABLE);
        }
        // 在列服务、求助查询,统计次数
        int onSaleServCnt = 0;
        int onSaleHelpCnt = 0;
        List<TService> services = productDao.selectByUserId(userId);
        for (TService service : services) {
            if (ProductEnum.TYPE_SEEK_HELP.getValue() == service.getType()) {
                onSaleHelpCnt++;
            } else if (ProductEnum.TYPE_SERVICE.getValue() == service.getType()) {
                onSaleServCnt++;
            }
        }
        view.setHelpOnSaleNum(Integer.valueOf(onSaleHelpCnt));
        view.setServOnSaleNum(Integer.valueOf(onSaleServCnt));

        // 身份证号码
        List<TUserAuth> userAuths = userAuthDao.selectByUserId(userId);
        if (!userAuths.isEmpty()) {
            TUserAuth userAuth = userAuths.get(0);
            view.setCardId(userAuth.getCardId());
        } else {
            view.setCardId("无"); // TODO
        }

        return view;
    }

    /**
     * 功能描述: 更新用户可用状态
     * 作者: 许方毅
     * 创建时间: 2018年12月26日 下午3:28:32
     * @param userId
     * @param avaliableStatus
     */
    @Override
    public void changeAvailableStatus(Long userId, String avaliableStatus, String userType, TUser manager) { // TODO 是否要判断下用户的存在性
        long currentTimeMillis = System.currentTimeMillis();
        TUser user = new TUser();
        user.setId(userId);
        if(avaliableStatus!=null) {
            user.setAvaliableStatus(avaliableStatus);
        }
        if(userType!=null) {
            user.setUserType(userType);
        }
        // updater
        user.setUpdateTime(currentTimeMillis);
//        user.setUpdateUser(manager.getId());
//        user.setUpdateUserName(manager.getName());
        userDao.updateByPrimaryKey(user);

        if(AppConstant.AVALIABLE_STATUS_NOT_AVALIABLE.equals(avaliableStatus)) {	//禁用 => 强制下线
            String redisKey = "str" + userId;
            String token = (String) redisUtil.get(redisKey);
            if(token!=null) {
                redisUtil.del(token);
                redisUtil.del(redisKey);
                redisUtil.del(String.valueOf(userId));
            }
        }

        // 日志记录
        String logContent = "";
        // 处理结果
        String result = AppConstant.AVALIABLE_STATUS_AVALIABLE.equals(avaliableStatus) ? "\"恢复\"" : "\"禁用\"";
        if(userType!=null) {
            result = "3".equals(userType)?"\"认定为公益组织\"":"\"认定为普通组织\"";
        }
        // 处理时间
        String time = changeTime(currentTimeMillis);
//        logContent += "管理员:" + manager.getName() + "(ID:" + manager.getId() + ")" + " 于 " + time + " 对 编号为" + userId
//                + "的用户 的可用状态" + "进行了 => " + result;
//        logger.warn(logContent);
    }

    /**
     * 功能描述: 密码登录
     * 作者: 许方毅
     * 创建时间: 2018年12月28日 下午2:38:36
     * @param account
     * @param password
     * @throws Exception
     */
    @Override
    public void loginPwd(String account, String password, HttpServletResponse response) throws Exception {
        Integer COOKIE_INTERVAL = 60 * 60 * 24 * 15;
        // 处理密码 -> 对已经在前端md5加密过的数据，对称加密一次
        password = AESCommonUtil.encript(password);

        TUser user = null;
        List<TUser> users = userDao.selectByUserAccountAndPasswordAndJurisdiction(account,password,AppConstant.JURISDICTION_ADMIN);
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        if (user == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户名或密码错误,或没有对应的权限");
        }
        // 生成token，向cookie存放值
        String token = TokenUtil.genToken(String.valueOf(user.getId()));
        // 向redis存放token和用户信息
        redisUtil.set(token, user, COOKIE_INTERVAL); // 应当与cookie时效统一
        redisUtil.set(String.valueOf(user.getId()), user, COOKIE_INTERVAL);
        redisUtil.set("str" + user.getId(), token, COOKIE_INTERVAL);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(COOKIE_INTERVAL); // cookie有效时效
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
