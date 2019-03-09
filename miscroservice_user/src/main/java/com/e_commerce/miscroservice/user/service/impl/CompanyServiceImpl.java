package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserCompany;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.user.dao.CompanyDao;
import com.e_commerce.miscroservice.user.dao.UserCompanyDao;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.service.CompanyService;
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
        user = userDao.selectByPrimaryKey(userId);
        if(AppConstant.AUTH_TYPE_CORP.equals(user.getAuthenticationType())) {
            //寻找对应的组织账号
            TUser companyUser = userDao.queryDoppelganger(user);
            if(companyUser!=null) {
                userId = companyUser.getId();
            }
        }

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);

        List<TUserCompany> select = userCompanyDao.queryByUserIdDESC(userId);

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
            StrUserCompanyView userCompanyView = BeanUtil.copy(company, StrUserCompanyView.class);
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
            StrServiceView strServiceView = BeanUtil.copy(order, StrServiceView.class);
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
//
//        // 查询我作为接单者的所有订单关系
//        List<TOrder> orders = orderService.selectOrderrelationshipListByReceiptUserId(userId);
//
//
//        List<Long> idList = new ArrayList<Long>();
//        for (TServiceReceipt serviceReceipt : serviceReceipts) {
//            idList.add(serviceReceipt.getServiceId());
//        }
//
//        if(idList.isEmpty()) {
//            QueryResult<StrServiceView> queryResult = new QueryResult<>();
//            queryResult.setResultList(new ArrayList<>());
//            return queryResult;
//        }
//
//        // 查询单位申请人(OLD) => 直接查询组织账号
//        Long masterId = null;
//
//        //查询组织账号id
//        TUserCompanyExample userCompanyExample = new TUserCompanyExample();
//        TUserCompanyExample.Criteria userCompanyCriteria = userCompanyExample.createCriteria();
//        userCompanyCriteria.andCompanyIdEqualTo(companyId);	//组织id
//        userCompanyCriteria.andCompanyJobEqualTo(AppConstant.JOB_COMPANY_CREATER);	//创建者
//        userCompanyCriteria.andIsValidEqualTo(AppConstant.IS_VALID_YES);	//TODO 有效
//        List<TUserCompany> userCompanies = userCompanyDao.selectByExample(userCompanyExample);
//        if(!userCompanies.isEmpty()) {
//            masterId = userCompanies.get(0).getUserId();
//        }
//
//        // exclude list
//        List<Integer> excludeList = new ArrayList<>();
//        excludeList.add(AppConstant.SERV_STATUS_UNAUDITED);// 1
//        excludeList.add(AppConstant.SERV_STATUS_FAILPASS);// 9
//        excludeList.add(AppConstant.SERV_STATUS_MASTERCANCLE);// 6 TODO 是否显示已被取消的项目
//        excludeList.add(AppConstant.SERV_STATUS_GUESTCANCLE);// 8
//        excludeList.add(AppConstant.SERV_STATUS_OUT_OF_TIME);// 7
//
//        // PageHelper
//        PageHelper.startPage(0, pageSize); // TODO 分页
//
//        // 查询组织账号发布的活动
//        TServiceExample serviceExample = new TServiceExample();
//        TServiceExample.Criteria serviceCriteria = serviceExample.createCriteria();
//        serviceExample.setOrderByClause("create_time desc,update_time asc");// 排序规则
////		serviceCriteria.andTypeEqualTo(AppConstant.PUBLISH_TYPE_ACTY);
//        serviceCriteria.andSourceEqualTo(AppConstant.SERV_TYPE_CORP);
//        serviceCriteria.andUserIdEqualTo(masterId); // 活动方
//        serviceCriteria.andStatusNotIn(excludeList);// 筛选状态
//        serviceCriteria.andIdIn(idList);
//        serviceCriteria.andIsValidEqualTo(AppConstant.IS_VALID_YES); // 有效
//        serviceCriteria.andCreateTimeLessThan(lastTime); // TODO 分页参数
//        List<TService> services = serviceDao.selectByExample(serviceExample);
//
//        //String化
//        List<StrServiceView> views = new ArrayList<StrServiceView>();
//        for(TService service:services) {
//            StrServiceView view = BeanUtil.copy(service, StrServiceView.class);
//            view.setIdString(String.valueOf(view.getId()));
//            views.add(view);
//        }
//
//        PageInfo<TService> pageInfo = new PageInfo<TService>(services);
//        queryResult.setResultList(views);
//        queryResult.setTotalCount(pageInfo.getTotal());
        QueryResult<StrServiceView> queryResult = new QueryResult<StrServiceView>();

        /*
         * //OLD 遍历与比较 -> 结果集 // resultList List<TService> resultList = new
         * ArrayList<>();
         *
         * // id列表 List<Long> strList = new ArrayList<>(); for (TServiceReceipt
         * serviceReceipt : serviceReceipts) {
         * strList.add(serviceReceipt.getServiceId()); }
         *
         * // 匹配构建结果集 for (TService service : services) { if
         * (strList.contains(service.getId())) { resultList.add(service); } }
         */

        return queryResult;
    }


}
