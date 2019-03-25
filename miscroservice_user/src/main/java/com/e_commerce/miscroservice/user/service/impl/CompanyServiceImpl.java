package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.order.controller.OrderCommonController;
import com.e_commerce.miscroservice.order.dao.OrderDao;
import com.e_commerce.miscroservice.user.dao.CompanyDao;
import com.e_commerce.miscroservice.user.dao.UserAuthDao;
import com.e_commerce.miscroservice.user.dao.UserCompanyDao;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.service.CompanyService;
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

        // 查询我作为接单者的所有订单关系
        List<TOrderRelationship> orderRelationships = orderService.selectOrderrelationshipListByReceiptUserId(userId);
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
            StrServiceView view = BeanUtil.copy(order, StrServiceView.class);
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

        AuthView authView = BeanUtil.copy(auth, AuthView.class);

        authView.setUserTel(user.getUserTel());

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("authView",authView);
        resultMap.put("companyView",companyDao.selectLatestByUserId(id));
        return resultMap;
    }


}
