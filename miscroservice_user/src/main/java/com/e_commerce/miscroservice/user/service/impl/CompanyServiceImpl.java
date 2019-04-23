package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.OrderRelationshipEnum;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.user.po.TUserAuth;
import com.e_commerce.miscroservice.user.po.TUserCompany;
import com.e_commerce.miscroservice.user.service.CompanyService;
import com.e_commerce.miscroservice.user.service.apiImpl.SendSmsService;
import com.e_commerce.miscroservice.user.vo.AuthView;
import com.e_commerce.miscroservice.user.vo.StrServiceView;
import com.e_commerce.miscroservice.user.vo.StrUserCompanyView;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserCompanyDao userCompanyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderCommonController orderService;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private MessageCommonController messageService;

    @Autowired
    private SendSmsService smsService;

    /**
     * 获取加入的组织列表
     * @param user
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<StrUserCompanyView> getCompanyList(TUser user, Long userId, Integer pageNum, Integer pageSize) {
        // 默认分页参数
        if (pageSize == null) {
            pageSize = 0;
        }
        if (pageNum == null) {
            pageNum = 1;

        }

        //判断要查询的对象
        if(userId==null) {
            userId = user.getId();
        }

        //判断是否为组织的创建者
        Long companyUserId = null;
        user = userDao.selectByPrimaryKey(userId);
        if(AppConstant.AUTH_TYPE_CORP.equals(user.getAuthenticationType())) {
            //寻找对应的组织账号
            TUser companyUser = userDao.queryDoppelganger(user);
            if(companyUser!=null) {
                companyUserId = companyUser.getId();
            }
        }

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);

        List<TUserCompany> select = userCompanyDao.queryByUserIdsDESC(userId,companyUserId);

        //numberCountMap
        Map<Long,Integer> numberCountMap = new HashMap<>();
        for(TUserCompany company:select) {
            // 统计
            Integer numberCount = 0;
            // 人数
            List<TUserCompany> userCompanies = userCompanyDao.selectByCompanyId(company.getCompanyId());
            numberCount = userCompanies.size();
            numberCountMap.put(company.getCompanyId(), numberCount);
        }

        //String化
        List<StrUserCompanyView> companies = new ArrayList<>();
        for (TUserCompany company : select) {
            StrUserCompanyView userCompanyView = company.copyStrUserCompanyView();
            userCompanyView.setCompanyIdString(String.valueOf(userCompanyView.getCompanyId()));
            userCompanyView.setNum(numberCountMap.get(userCompanyView.getCompanyId()));
            companies.add(userCompanyView);
        }

        QueryResult<StrUserCompanyView> queryResult = new QueryResult<>();
        queryResult.setResultList(companies);
        queryResult.setTotalCount(startPage.getTotal());
        return queryResult;
    }


    /**
     * 获取单位名下的活动列表
     * @param companyId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<StrServiceView> getActivityList(Long companyId, Integer pageNum, Integer pageSize) {
        //只有该单位的负责人才有发布权限。(后续可能会加入授权功能，与管理者等共享发布权限) => 2019.1.30 组织版上线后，只有组织账号才有发布权限

        // 判空
        if (companyId == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"组织编号不能为空!");
        }

        if (pageNum == null) {
            pageNum = 1;
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        Long userId = null;
        //查询组织账号id
        List<TUserCompany> userCompanies = userCompanyDao.findRecordOfCompanyAccount(companyId);

        if(!userCompanies.isEmpty()) {
            userId = userCompanies.get(0).getUserId();
        }

        // includeArray
        Integer[] availableStatusArray = AppConstant.AVAILABLE_STATUS_ARRAY;


        // PageHelper
        Page<Object> startPage = PageHelper.startPage(0, pageSize);

        // 查询名下发布的以组织名义的活动
        List<TOrder> orders = orderDao.selectBySourceAndUserIdAndStatuses(AppConstant.SERV_TYPE_CORP,userId,availableStatusArray);

        // list
        List<StrServiceView> serviceViews = new ArrayList<StrServiceView>();
        // String化
        for (TOrder order : orders) {
            com.e_commerce.miscroservice.user.po.TOrder tOrder = new com.e_commerce.miscroservice.user.po.TOrder();
            tOrder = tOrder.exchangeOrder(order);
            StrServiceView strServiceView = tOrder.copyStrServiceViewByExchange();
            strServiceView.setIdString(String.valueOf(strServiceView.getId()));
            strServiceView.setServiceIdString(String.valueOf(strServiceView.getServiceId()));
            serviceViews.add(strServiceView);
        }

        QueryResult<StrServiceView> queryResult = new QueryResult<>();
        queryResult.setResultList(serviceViews);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }

    /**
     * 获取该单位下我参与的活动
     * @param userId
     * @param companyId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<StrServiceView> getMyActivityList(Long userId, Long companyId, Integer pageNum, Integer pageSize) {
        // 判空
        if (pageNum == null) {
            pageNum = 1;
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        // 非空校验
        if (companyId == null || (companyId != null && companyId <= 0)) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "组织Id不能为空！");
        }

        // 查询我作为接单者的所有订单关系
        List<TOrderRelationship> orderRelationships = orderService.selectOrderRelationshipByReceiptUserIdNotEqStatus(userId, OrderRelationshipEnum.STATUS_NO_STATE.getType());
        List<Long> idList = new ArrayList<>();
        for(TOrderRelationship orderRelationship:orderRelationships) {
            idList.add(orderRelationship.getOrderId());
        }

        if(idList.isEmpty()) {
            return new QueryResult<StrServiceView>();
        }

        // 查询单位申请人(OLD) => 直接查询组织账号
        Long masterId = null;

        //查询组织账号id
        List<TUserCompany> userCompanies = userCompanyDao.findRecordOfCompanyAccount(companyId);

        if(!userCompanies.isEmpty()) {
            masterId = userCompanies.get(0).getUserId();
        }

        // PageHelper
        Page<Object> startPage = PageHelper.startPage(0, pageSize);

        List<TOrder> orders = orderDao.selectBySourceAndUserIdAndStatusesInIds(ProductEnum.SOURCE_GROUP.getValue(), masterId, AppConstant.AVAILABLE_STATUS_ARRAY, idList);

        //String化
        List<StrServiceView> views = new ArrayList<StrServiceView>();
        for(TOrder order:orders) {
            com.e_commerce.miscroservice.user.po.TOrder tOrder = new com.e_commerce.miscroservice.user.po.TOrder();
            tOrder = tOrder.exchangeOrder(order);
            StrServiceView view = tOrder.copyStrServiceViewByExchange();
            view.setIdString(String.valueOf(view.getId()));
            view.setServiceIdString(String.valueOf(view.getServiceId()));
            views.add(view);
        }

        QueryResult<StrServiceView> queryResult = new QueryResult<>();
        queryResult.setResultList(views);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }

    /**
     * 查询认证信息
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> companyInfo(TUser user) {
        Long id = user.getId();
        List<TUserAuth> auths = userAuthDao.selectByUserId(id);
        TUserAuth auth = new TUserAuth();
        if (!auths.isEmpty()) {
            auth = auths.get(0);
        }

        AuthView authView = auth.copyAuthView();

        authView.setUserTel(user.getUserTel());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("authView",authView);
        resultMap.put("companyView",companyDao.selectLatestByUserId(id));
        return resultMap;
    }

    @Override
    public void modify(Long companyId, String option) {
        long currentTimeMillis = System.currentTimeMillis();
        /**
         * 1.通过审核
         */
        TCompany company = companyDao.selectByPrimaryKey(companyId);
        if (company == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "公司不存在！");
        }
        //防重复审核 TODO 已经被拒绝的和已经审核通过的不做操作(或许改成所有状态都能进行审核，即重复审核)
        if (AppConstant.CORP_CERT_STATUS_YES.equals(company.getStatus()) || AppConstant.CORP_CERT_STATUS_NO.equals(company.getStatus())) {
            return;
        }
        if ("1".equals(option)) {
            /*
             * 修改t_company为已认证
             */

            company.setStatus(AppConstant.CORP_CERT_STATUS_YES);
            company.setUpdateTime(currentTimeMillis);
            company.setUpdateUser(0l);
            company.setUpdateUserName("默认管理员");
            companyDao.updateByPrimaryKey(company);

            /*
             * 查找t_user,修改为单位认证类型
             */
            TUser user = userDao.selectByPrimaryKey(company.getUserId());

            Long userId = user.getId();
            user.setAuthenticationType(AppConstant.AUTH_STATUS_YES);
            user.setAuthenticationStatus(AppConstant.AUTH_TYPE_CORP);
            //updater
            user.setUpdateTime(currentTimeMillis);
            user.setUpdateUser(user.getId());
            user.setUpdateUserName(user.getName());
            userDao.updateByPrimaryKey(user);

            //重复创建判断
            List<TUser> userList = userDao.selectByUserTelAndJurisAndIsCompany(user.getUserTel(), AppConstant.JURISDICTION_NORMAL, AppConstant.IS_COMPANY_ACCOUNT_YES);
            if (!userList.isEmpty()) {
                return;
            }

            /*
             * 创建组织账号 => 组织账号使用了和个人账号相同的userTel	//TODO 是否使用组织特制user_account
             */
            TUser companyUser = user;
            companyUser.setPassword("KuXmFYLVw/0TSr0IvWrOhPwZB7gk0sagJmQGMQLqDyT/o9Ah0OxrNEeNQz5T 2P2s");    //默认密码
            companyUser.setIsCompanyAccount(AppConstant.IS_COMPANY_ACCOUNT_YES);    //组织账号
            companyUser.setName(company.getName());    //组织账号使用组织名字
            companyUser.setVxOpenId(null);
            companyUser.setFollowNum(0);
            companyUser.setReceiptNum(0);
            companyUser.setLevel(1);
            companyUser.setGrowthValue(0l);
            companyUser.setSeekHelpNum(0);
            companyUser.setServeNum(0);
            companyUser.setPublicWelfareTime(0l);
            companyUser.setServTotalEvaluate(0);
            companyUser.setServCreditEvaluate(0);
            companyUser.setServMajorEvaluate(0);
            companyUser.setServAttitudeEvaluate(0);
            companyUser.setInviteCode("COMPANY");
            companyUser.setAvaliableStatus(AppConstant.AVALIABLE_STATUS_AVALIABLE);
            companyUser.setIsFake(AppConstant.IS_FAKE_NO);    //真实账号
            companyUser.setSurplusTime(0l);
            companyUser.setFreezeTime(0l);
            companyUser.setCreditLimit(60l);
            companyUser.setCreateUser(0l);
            companyUser.setCreateUserName("默认管理员");
            companyUser.setCreateTime(currentTimeMillis);
            companyUser.setUpdateUser(0l);
            companyUser.setUpdateUserName("默认管理员");
            companyUser.setUpdateTime(currentTimeMillis);
            companyUser.setPraise(0);
            userDao.insert(companyUser);

            /*
             * 创建t_user_company记录
             */
            TUserCompany userCompany = new TUserCompany();
            userCompany.setCompanyId(companyId);
            userCompany.setUserId(companyUser.getId());    //使用组织账号的id
            userCompany.setCompanyName(company.getName());
            userCompany.setCompanyJob(AppConstant.JOB_COMPANY_CREATER);
            // creater & updater
            userCompany.setCreateTime(currentTimeMillis);
            userCompany.setCreateUser(0l);    //以下使用了管理员的名字和id
            userCompany.setCreateUserName("默认管理员");
            userCompany.setUpdateTime(currentTimeMillis);
            userCompany.setUpdateUser(0l);
            userCompany.setUpdateUserName("默认管理员");
            userCompany.setIsValid(AppConstant.IS_VALID_YES);
            userCompanyDao.insert(userCompany);

            /*
             * 创建t_group记录（默认分组）
             */
            TGroup group = new TGroup();
            group.setCompanyId(companyId);
            group.setGroupName("默认分组");
            group.setAuth(0);
            group.setCreateTime(currentTimeMillis);
            group.setCreateUser(userId);
            group.setCreateUserName(user.getName());
            group.setUpdateTime(currentTimeMillis);
            group.setUpdateUser(userId);
            group.setUpdateUserName(user.getName());
            group.setIsValid(AppConstant.IS_VALID_YES);
            groupDao.insert(group);

            System.currentTimeMillis();
            //系统通知
            String noticeTitle = "您申请认证的组织\"" + company.getName() + "\"现已通过审核";    //TODO
            String noticeMessage = "您可以使用本手机号登录组织版了！初始密码为:123456。请尽快前往网页端组织版更改密码，防止被人盗取。";    //TODO
            messageService.messageSave(0l, companyUser, noticeTitle, noticeMessage, userId, currentTimeMillis);

            //短信通知
            String telephone = user.getUserTel();
            String content = "【壹晓时】您申请认证的组织\"" + company.getName() + "\"现已通过审核。现在可以使用本手机号登录组织版，初始密码:123456。请尽快前往网页端（https://organization.xiaoshitimebank.com）更改密码，防止他人盗取。";    //TODO 网址
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", telephone);
            params.put("content", content);
            smsService.sendServMsg(params);    //发送短信通知

//            //日志记录
//            String logContent = "";
//            // 处理时间
//            String time = changeTimeSeconds(currentTimeMillis);
//            logContent += "管理员:" + manager.getName() + "(ID:" + manager.getId() + ")" + " 于 " + time + " 对 编号为" + userId
//                    + " 的用户 " + "申请的公司认证: " + company.getName() + "(ID为 " + companyId + " )进行了\"审核\" => 结果:通过";
//            logger.warn(logContent);
        }
    }

}
