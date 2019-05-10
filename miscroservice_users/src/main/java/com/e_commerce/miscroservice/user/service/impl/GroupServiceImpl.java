package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.*;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
import com.e_commerce.miscroservice.commons.enums.application.PaymentEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.POIUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.order.service.impl.BaseService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.dao.ProductDao;
import com.e_commerce.miscroservice.user.dao.*;
import com.e_commerce.miscroservice.commons.entity.application.TCompany;
import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.user.po.TUserCompany;
import com.e_commerce.miscroservice.commons.enums.application.GroupEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.NoAuthChangeException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.user.dao.CompanyDao;
import com.e_commerce.miscroservice.user.dao.GroupDao;
import com.e_commerce.miscroservice.user.dao.UserCompanyDao;
import com.e_commerce.miscroservice.user.service.GroupService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.CompanyRecentView;
import com.e_commerce.miscroservice.user.vo.SmartUserView;
import com.e_commerce.miscroservice.user.vo.UserCompanyView;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import com.e_commerce.miscroservice.user.vo.BaseGroupView;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupServiceImpl extends BaseService implements GroupService {

    Log logger = Log.getInstance(GroupServiceImpl.class);

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserCompanyDao userCompanyDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTimeRecordDao userTimeRecordDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserService userService;

    /**
     * 组织概况
     * @param user
     * @return
     */
    @Override
    public UserCompanyView gatherInfo(TUser user) {
        // 与数据库同步
        user = userDao.selectByPrimaryKey(user.getId());

        Long userId = user.getId();
        // 获取组织id、组织账号id
        Long companyId = getOwnCompanyId(userId);
        // 当月总发布
        List<TService> services = getPublishedList(userId,true);
        int monthlyHelpCnt = 0;
        int monthlyServCnt = 0;
        for (TService service : services) {
            // 本月发布需求数
            if (AppConstant.SERV_TYPE_HELP.equals(service.getType())) {
                monthlyHelpCnt++;
                // 本月发布服务数
            } else {
                monthlyServCnt++;
            }
        }
        // 本月新增成员数
        Integer newlyJoinMemberCnt = 0;
        newlyJoinMemberCnt = getNewMemberCnt(companyId, true);
        // 组织成员数
        Integer memberCnt = 0;
        memberCnt = getNewMemberCnt(companyId, false);
        // 可用互助时
        Long surplusTime = user.getSurplusTime();
        Long freezeTime = user.getFreezeTime();
        Integer avaliableTime = (int) (surplusTime - freezeTime);

        UserCompanyView userCompanyView = new UserCompanyView();
        userCompanyView.setIdString(String.valueOf(companyId));
        userCompanyView.setMonthlyHelpCnt(monthlyHelpCnt);
        userCompanyView.setMonthlyServCnt(monthlyServCnt);
        userCompanyView.setNewlyJoinMemberCnt(newlyJoinMemberCnt);
        userCompanyView.setMemberCnt(memberCnt);
        userCompanyView.setAvaliable_time(avaliableTime);
        return userCompanyView;
    }

    /**
     * 获取新加入的用户数
     * @param companyId
     * @param isThisMonth
     * @return
     */
    private Integer getNewMemberCnt(Long companyId, boolean isThisMonth) {
        // 用户id集合
        Map<String, Object> monthBetween = DateUtil.getMonthBetween(System.currentTimeMillis());
        final Long begin = Long.valueOf((String) monthBetween.get("begin"));
        final Long end = Long.valueOf((String) monthBetween.get("end"));

        List<TUserCompany> resultList = new ArrayList<>();
        if (isThisMonth) {
            resultList = userCompanyDao.selectJoinMembersByCompanyIdBetween(companyId,begin,end);
        } else {
            resultList = userCompanyDao.selectJoinMembersByCompanyId(companyId);
        }

        return resultList.size();
    }

    /**
     * 获取当月发布的所有服务、需求集合
     * @param userId
     * @return
     */
    private List<TService> getPublishedList(Long userId,boolean isThisMonth) {
        if(isThisMonth) {
            // 处理当前月份
            Map<String, Object> monthBetween = DateUtil.getMonthBetween(System.currentTimeMillis());
            return productDao.selectByCompanyAccountInStatusBetween(userId,AppConstant.COMPANY_PUBLISHED_STATUS_ARRAY,Long.valueOf((String) monthBetween.get("begin")),Long.valueOf((String) monthBetween.get("end")));
        }
        return productDao.selectByCompanyAccountInStatus(userId,AppConstant.COMPANY_PUBLISHED_STATUS_ARRAY);
    }

    /**
     * 获取指定年份发布的所有服务、需求集合
     * @param userId
     * @return
     */
    private List<TService> getPublishedList(Long userId,int year) {
        // 处理当前年份
        Map<String, Object> yearBetween = DateUtil.y2BetweenStamp(year);
        return productDao.selectByCompanyAccountInStatusBetween(userId,AppConstant.COMPANY_PUBLISHED_STATUS_ARRAY,(long) yearBetween.get("betLeft"),(long) yearBetween.get("betRight"));
    }

    /**
     * 获取当年组织成员(TUserCompany)
     * @param companyId
     * @param year
     * @return
     */
    private List<TUserCompany> getUserCompanies(Long companyId, int year) {
        Map<String, Object> yearBetween = DateUtil.y2BetweenStamp(year);
        return userCompanyDao.selectBycompanyIdAndCompanyJobAndStateBetween(companyId,AppConstant.JOB_COMPANY_MEMBER,AppConstant.JOIN_STATE_COMPANY_PASS,(long) yearBetween.get("betLeft"),(long) yearBetween.get("betRight"));
    }

    /**
     * 获取组织账号对应组织编号
     * @param id
     * @return
     */
    @Override
    public Long getOwnCompanyId(Long id) {
        List<TUserCompany> userCompanies = userCompanyDao.selectByUserIdAndCompanyJob(id,AppConstant.JOB_COMPANY_CREATER);

        if(userCompanies.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"没有对应组织！");
        }
        return userCompanies.get(0).getCompanyId();
    }

    /**
     * 组织近况
     * @param user
     * @return
     */
    @Override
    public CompanyRecentView companyRecent(TUser user) {
        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);

        // 当月
        long currentTimeMillis = System.currentTimeMillis();
        String timeStamp2Date = DateUtil.timeStamp2Date(currentTimeMillis);
        String[] split = timeStamp2Date.split("-");
        int length = Integer.valueOf(split[1]);
        int year = Integer.valueOf(split[0]);

        // 服务、需求近况
        List<TService> services = getPublishedList(userId, year); // 当年的服务需求列表
        // 长度到当月
        int[] helpCnt = new int[length];
        int[] servCnt = new int[length];
        // 遍历统计
        for (TService service : services) {
            String theMonth = DateUtil.timeStamp2Month(service.getCreateTime());
            int index = Integer.valueOf(theMonth) - 1;
            if (AppConstant.SERV_TYPE_HELP.equals(service.getType())) { // 求助
                helpCnt[index] = ++helpCnt[index];
            } else if (AppConstant.SERV_TYPE_SERV.equals(service.getType())) { // 服务
                servCnt[index] = ++servCnt[index];
            }
        }

        // 成员新增近况
        List<TUserCompany> userCompanies = getUserCompanies(companyId, year); // 当年的成员新增近况
        // 长度到当月
        int[] memberCnt = new int[length];
        // 遍历统计
        for (TUserCompany userCompany : userCompanies) {
            String theMonth = DateUtil.timeStamp2Month(userCompany.getCreateTime());
            int index = Integer.valueOf(theMonth) - 1;
            memberCnt[index] = ++memberCnt[index];
        }

        CompanyRecentView resultView = new CompanyRecentView();
        resultView.setHelpRecent(helpCnt);
        resultView.setServRecent(servCnt);
        resultView.setAddMemberRecent(memberCnt);

        return resultView;
    }

    /**
     * 组内成员列表
     * @param user
     * @param groupId
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<SmartUserView> userList(TUser user, Long groupId, String param, Integer pageNum,
                                               Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }
        QueryResult<SmartUserView> queryResult = new QueryResult<>();
        String regex = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";
        List<SmartUserView> resultList = new ArrayList<>();
        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);

        String teamNameParam = null;
        Long groupIdParam = null;
        List<TUserCompany> userCompanies = new ArrayList<>();
        String uniqueGroupName = null; // 参数groupId对应的分组名
        if (param != null) {
            if (param != "" && !param.matches(regex)) { // 姓名
                teamNameParam = param;
            }
        }
        if (groupId != null) { // 查找组内成员
            groupIdParam = groupId;
            uniqueGroupName = groupDao.selectByPrimaryKey(groupId).getGroupName();
        }

        userCompanies = userCompanyDao.selectByCompanyIdAndStateAndTeamNameAndGroupId(companyId,AppConstant.JOIN_STATE_COMPANY_PASS,param,groupId);
        // id集合
        List<Long> userIds = new ArrayList<Long>(); // id集合 NULL MARK
        Map<String, Object> nameMap = new HashMap<String, Object>();
        Map<Long, String> groupMap = new HashMap<Long, String>();
        Map<String, Long> groupMapReverse = new HashMap<String, Long>();
        for (TUserCompany userCompany : userCompanies) {
            // 获取组内姓名
            nameMap.put(String.valueOf(userCompany.getUserId()), userCompany.getTeamName());
            userIds.add(userCompany.getUserId());
            if (uniqueGroupName == null) {
                // 制作userId => groupName映射
                Long idKey = userCompany.getUserId();
                Long theGroupId = userCompany.getGroupId();
                TGroup group = groupDao.selectByPrimaryKey(theGroupId);
                groupMap.put(idKey, group.getGroupName());
                groupMapReverse.put(group.getGroupName(), theGroupId);
            }
        }
        if (userIds.isEmpty()) {
            return queryResult;
        }

        // 分页
        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        // 用户
        String telephone = null;
        if (param != null) {
            if (param.matches(regex)) { // 手机号
                telephone = param;
            }
        }

        List<TUser> users = userDao.selectByTelephoneInInIds(telephone,userIds, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
        // 处理数据
        for (TUser thisUser : users) {
            com.e_commerce.miscroservice.user.po.TUser tUser = new com.e_commerce.miscroservice.user.po.TUser();

            SmartUserView view = tUser.copySmartUserView();
            Integer sex = thisUser.getSex();
            view.setIdString(String.valueOf(thisUser.getId()));
            String sexString = "";
            if (AppConstant.SEX_MALE.equals(sex)) {
                sexString = "男";
            } else if (AppConstant.SEX_FEMALE.equals(sex)) {
                sexString = "女";
            } else {
                sexString = "未设置";
            }
            view.setSexString(sexString);
            // 统计总共获得的互助时数目
            List<TUserTimeRecord> userTimeRecords = userTimeRecordDao.selectByUserId(thisUser.getId());
            Long allGain = 0l;
            for (TUserTimeRecord userTimeRecord : userTimeRecords) {
                allGain += userTimeRecord.getTime();
            }
            view.setGainTimeString(timeChange(allGain)); // 获得互助时
            view.setAgeString(thisUser.getAge() == null ? "未设置" : thisUser.getAge() + "岁"); // 年龄
            view.setSkill(thisUser.getSkill() == null ? "未设置" : thisUser.getSkill()); // 技能
            view.setTotalComment(
                    String.format("%.2f", (double) thisUser.getServTotalEvaluate() / 3 / thisUser.getServeNum())); // 保留2位小数
            view.setCorpNickName((String) nameMap.get(String.valueOf(thisUser.getId())));
            resultList.add(view);
        }

        List<SmartUserView> finalList = new ArrayList<>();

        // 获取所在组名与id
        for (SmartUserView theUser : resultList) {
            String groupName = groupMap.get(theUser.getId());
            theUser.setGroupName(groupName); // 所在分组名
            theUser.setGroupIdString(String.valueOf(groupMapReverse.get(groupName)));
            finalList.add(theUser);
        }

        queryResult.setResultList(resultList);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;

    }

    /**
     * 成员新增
     * @param user
     * @param groupId
     * @param userIds
     */
    @Override
    public void userAdd(TUser user, Long groupId, String userIds) {
        if(userIds==null || userIds.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"添加的对象不能为空！");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);
        TCompany company = companyDao.selectByPrimaryKey(companyId);
        String companyName = null;
        if(company!=null) {
            companyName = company.getName();	//组织名字
        }
        // 处理userIds
        String[] split = userIds.split(",");
        if (groupId == null) { // 加入默认分组
            // 查找默认分组
            List<TGroup> groups = groupDao.selectByCompanyIdAndAuth(companyId,AppConstant.GROUP_AUTH_DEFAULT);
            if (!groups.isEmpty()) {
                groupId = groups.get(0).getId();
            }
        }
        for (String thisId : split) {
            boolean done = false;
            TUser thisUser = userDao.selectByPrimaryKey(Long.valueOf(thisId));
            // 判重
            List<TUserCompany> userCompanies = userCompanyDao.selectByCompanyIdAndUserId(companyId,Long.valueOf(thisId));
            if (!userCompanies.isEmpty()) { // 存在相关记录
                for (TUserCompany userCompany : userCompanies) {
                    if (AppConstant.JOB_COMPANY_MEMBER.equals(userCompany.getCompanyJob())
                            && !AppConstant.JOIN_STATE_COMPANY_PASS.equals(userCompany.getState())) { // 存在非审核通过组织成员的记录
                        userCompany.setGroupId(groupId);
                        userCompany.setState(AppConstant.JOIN_STATE_COMPANY_PASS);
                        userCompany.setUpdateTime(currentTimeMillis);
                        userCompany.setUpdateUser(userId);
                        userCompany.setUpdateUserName(user.getName());
                        userCompanyDao.updateByPrimaryKey(userCompany);
                        break;
                    }
                }
                done = true;
            }

            if (!done) {
                // 加入组织
                TUserCompany userCompany = new TUserCompany();
                userCompany.setCompanyId(companyId); // 组织id
                userCompany.setCompanyName(companyName); //组织名
                userCompany.setUserId(Long.valueOf(thisId)); // 成员id
                userCompany.setGroupId(groupId);
                userCompany.setCompanyJob(AppConstant.JOB_COMPANY_MEMBER); // 角色为普通成员
                userCompany.setTeamName(thisUser.getName()); // 组内昵称默认为该用户的昵称
                userCompany.setState(AppConstant.JOIN_STATE_COMPANY_PASS); // TODO STATE REMARK
                userCompany.setCreateTime(currentTimeMillis);
                userCompany.setCreateUser(userId);
                userCompany.setCreateUserName(user.getName());
                userCompany.setUpdateTime(currentTimeMillis);
                userCompany.setUpdateUser(userId);
                userCompany.setUpdateUserName(user.getName());
                userCompany.setIsValid(AppConstant.IS_VALID_YES); // 有效
                userCompanyDao.insert(userCompany);
                done = true;
            }

            //维护t_user表中的company_id、company_name字段
            if(done) {
                //查找t_usercompany
                List<TUserCompany> tUserCompanies = userCompanyDao.selectByUserIdAndCompanyJob(thisUser.getId(), AppConstant.JOB_COMPANY_MEMBER);
                String companyIds = "";
                String companyNames = "";
                for(TUserCompany tUserCompany:tUserCompanies) {
                    companyIds += tUserCompany.getCompanyId();
                    companyIds += ",";
                    companyNames += tUserCompany.getCompanyName();
                    companyNames += ",";
                }
                if(companyIds.endsWith(",")) {
                    companyIds = companyIds.substring(0,companyIds.length()-1);
                }
                if(companyNames.endsWith(",")) {
                    companyNames = companyNames.substring(0,companyNames.length()-1);
                }
                thisUser.setCompanyIds(companyIds);
                thisUser.setCompanyNames(companyNames);
                //updater
                thisUser.setUpdateTime(System.currentTimeMillis());
                thisUser.setUpdateUser(user.getId());
                thisUser.setUpdateUserName(user.getName());
                userDao.updateByPrimaryKey(thisUser);
            }
        }
    }

    /**
     * 创建新成员
     * @param user
     * @param groupId
     * @param phone
     * @param userName
     * @param sex
     */
    @Override
    public void userInsert(TUser user, Long groupId, String phone, String userName, Integer sex) {
        if(userName!=null) {
            userName = userName.trim();
        }

        if(userName==null||userName=="") {
            return;
        }
        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);
        // 判断是否为存在的用户
        TUser userByTelephone = userService.getUserAccountByTelephone(phone, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
        if (userByTelephone == null) { // 创建假用户
            userByTelephone= new TUser();
            userByTelephone.setName(userName);
            userByTelephone.setUserTel(phone);
            userByTelephone.setSex(sex);
            userByTelephone.setIsFake(AppConstant.IS_FAKE_YES);
            userByTelephone = userService.rigester(userByTelephone, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
        }

        if (groupId == null) { // 加入默认分组
            // 查找默认分组
            List<TGroup> groups = groupDao.selectByCompanyIdAndAuth(companyId,AppConstant.GROUP_AUTH_DEFAULT);
            if (!groups.isEmpty()) {
                groupId = groups.get(0).getId();
            }
        }
        // 判重
        boolean done = false;
        long currentTimeMillis = System.currentTimeMillis();
        List<TUserCompany> userCompanies = userCompanyDao.selectByCompanyIdAndUserId(companyId,userByTelephone.getId());
        for(TUserCompany userCompany:userCompanies) {
            Integer state = userCompany.getState();
            if(AppConstant.JOIN_STATE_COMPANY_PASS.equals(state)) {
                throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该手机号已加入本组织!");
            }
            if(AppConstant.JOIN_STATE_COMPANY_NOT_YET.equals(state) || AppConstant.JOIN_STATE_COMPANY_REFUSE.equals(state)) {
                //更新该条记录 并记录 done为true
                userCompany.setState(AppConstant.JOIN_STATE_COMPANY_PASS);
                //updater
                userCompany.setUpdateTime(currentTimeMillis);
                userCompany.setUpdateUser(user.getId());
                userCompany.setUpdateUserName(user.getName());
                userCompanyDao.updateByPrimaryKey(userCompany);
                done = true;
            }
        }

        if(!done) {
            // 加入组织
            TUserCompany userCompany = new TUserCompany();
            userCompany.setCompanyId(companyId); // 组织id
            TCompany company = companyDao.selectByPrimaryKey(companyId);
            if(company!=null) {
                userCompany.setCompanyName(company.getName());	//TODO组织名字
            }
            userCompany.setUserId(userByTelephone.getId()); // 成员id
            userCompany.setGroupId(groupId);
            userCompany.setCompanyJob(AppConstant.JOB_COMPANY_MEMBER); // 角色为普通成员
            userCompany.setTeamName(userName); // 组内昵称
            userCompany.setState(AppConstant.JOIN_STATE_COMPANY_PASS); // TODO STATE REMARK
            userCompany.setCreateTime(currentTimeMillis);
            userCompany.setCreateUser(userId);
            userCompany.setCreateUserName(user.getName());
            userCompany.setUpdateTime(currentTimeMillis);
            userCompany.setUpdateUser(userId);
            userCompany.setUpdateUserName(user.getName());
            userCompany.setIsValid(AppConstant.IS_VALID_YES); // 有效
            userCompanyDao.insert(userCompany);
        }

        //维护t_user companyIds、companyNames字段
        TUser theUser = userByTelephone;
        String companyIds = theUser.getCompanyIds();
        String companyNames = theUser.getCompanyNames();
        if(companyIds==null) {
            companyIds = "";
        }
        if(!"".equals(companyIds)) {
            companyIds += ",";
        }
        if(companyNames==null) {
            companyNames = "";
        }
        if(!"".equals(companyNames)) {
            companyNames += ",";
        }

        companyIds += companyId;
        TUserCompany company = userCompanyDao.selectByPrimaryKey(companyId);
        companyNames += company.getCompanyName();

        if(companyIds.endsWith(",")) {
            companyIds = companyIds.substring(0,companyIds.length()-1);
        }
        if(companyNames.endsWith(",")) {
            companyNames = companyNames.substring(0,companyNames.length()-1);
        }

        theUser.setCompanyIds(companyIds);
        theUser.setCompanyNames(companyNames);
        //updater
        theUser.setUpdateTime(currentTimeMillis);
        theUser.setUpdateUser(user.getId());
        theUser.setUpdateUserName(user.getName());
        userDao.updateByPrimaryKey(theUser);
        userService.taskComplete(theUser, GrowthValueEnum.GROWTH_TYPE_UNREP_FIRST_JOIN_COMPANY,1);
    }

    /**
     * 删除组织成员
     * @param user
     * @param userIds
     */
    @Override
    public void delete(TUser user, String userIds) {
        if (userIds == null) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数userIds不能为空!");
        }
        // 判空
        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);
        // 处理userIds
        String[] split = userIds.split(",");
        ArrayList<Long> idList = new ArrayList<Long>();
        for (String singleUserId : split) {
            idList.add(Long.valueOf(singleUserId));
        }
        TUserCompany userCompany = new TUserCompany();
        userCompany.setIsValid(AppConstant.IS_VALID_NO); // 删除

        userCompanyDao.updateByUserIdsAndCompanyJobAndCompanyIdSetUserCompany(idList,AppConstant.JOB_COMPANY_MEMBER,companyId,userCompany);

        //维护t_user的company_id、company_name字段
        List<TUser> tUsers = userDao.selectInUserIds(split, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
        //构建maps
        Map<Long,TUser> userMap = new HashMap<>();
        for(TUser tUser:tUsers) {
            userMap.put(tUser.getId(),tUser);
        }

        List<TUserCompany> userCompanies = userCompanyDao.selectInUserIdAndCompanyJob(idList, AppConstant.JOB_COMPANY_MEMBER);
        Map<Long,List<Long>> idMap = new HashMap<>();
        Map<Long,List<String>> nameMap = new HashMap<>();
        for(TUserCompany usercompany:userCompanies) {
            Long usercompanyUserId = usercompany.getUserId();
            Long id = usercompany.getCompanyId();
            String name = usercompany.getCompanyName();
            List<Long> companyIdList = idMap.get(usercompanyUserId);
            List<String> companyNameList = nameMap.get(usercompanyUserId);
            companyIdList = new ArrayList();
            companyNameList = new ArrayList();
            companyIdList.add(id);
            companyNameList.add(name);
            idMap.put(usercompanyUserId,companyIdList);
            nameMap.put(usercompanyUserId,companyNameList);
        }

        for(Long id:idList) {
            TUser tUser = userMap.get(id);
            //统计查询该用户所有的组织
            List<Long> companyIdList = idMap.get(id);
            List<String> companyNameList = nameMap.get(id);
            String companyIds = "";
            String companyNames = "";
            if(companyIdList!=null) {
                for(Long corpId:companyIdList) {
                    companyIds += ",";
                    companyIds += corpId;
                }
            }
            if(companyIds.endsWith(",")) {
                companyIds = companyIds.substring(0,companyIds.length()-1);
            }

            if(companyNameList!=null) {
                for(String name:companyNameList) {
                    companyNames += ",";
                    companyNames += name;
                }
            }
            if(companyNames.endsWith(",")) {
                companyNames = companyNames.substring(0,companyNames.length()-1);
            }
            tUser.setCompanyIds(companyIds);
            tUser.setCompanyNames(companyNames);
            //updater
            tUser.setUpdateTime(System.currentTimeMillis());
            tUser.setUpdateUser(id);
            tUser.setUpdateUserName(tUser.getName());
            userDao.updateByPrimaryKey(tUser);
        }

    }

    /**
     * 成员审核通过
     * @param user
     * @param userIds
     */
    @Override
    public void userPass(TUser user, String userIds) {
        Long ownerId = user.getId();
        Long companyId = getOwnCompanyId(ownerId);
        TCompany tCompany = companyDao.selectByPrimaryKey(companyId);
        String companyName = tCompany.getName();
        String[] split = new String[1];
        //处理userIds
        if(userIds.contains(",")) {	//批量
            split = userIds.split(",");
        } else {	//单个
            split[0] = userIds;
        }

        List<TUser> users = userDao.selectInUserIds(split, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
        Map<Long,TUser> userMap = new HashMap<>();
        //构建map
        for(TUser tUser:users) {
            userMap.put(tUser.getId(),tUser);
        }

        for(String userId:split) {
            //加入组织
            TUserCompany userCompany = new TUserCompany();
            userCompany.setState(AppConstant.JOIN_STATE_COMPANY_PASS);
            userCompanyDao.updateByUserIdAndCompanyJobAndCompanyIdAndStateSetUserCompany(Long.valueOf(userId),AppConstant.JOB_COMPANY_MEMBER,companyId,AppConstant.JOIN_STATE_COMPANY_NOT_YET,userCompany);

            //维护t_user表 company_id、company_name字段
            TUser tUser = userMap.get(Long.valueOf(userId));      //从map获取
            if(tUser == null) {
                continue;
            }
            String companyIds = tUser.getCompanyIds();

            if(companyIds.contains(String.valueOf(companyId))) {    //如果已经存在
                continue;
            }

            //拼接组织id
            if(companyIds==null) {
                companyIds = "";
            }

            if(!companyIds.equals("")) {
                companyIds += ",";
            }

            companyIds = new StringBuilder(companyIds).append(companyId).toString();

            String companyNames = tUser.getCompanyNames();
            //拼接组织名
            if(companyNames==null) {
                companyNames = "";
            }

            if(!companyNames.equals("")) {
                companyNames += ",";
            }

            companyNames = new StringBuilder(companyNames).append(companyName).toString();

            tUser.setCompanyIds(companyIds);
            tUser.setCompanyNames(companyNames);
            //updater
            tUser.setUpdateTime(System.currentTimeMillis());
            tUser.setUpdateUser(user.getId());
            tUser.setUpdateUserName(user.getName());
            userDao.updateByPrimaryKey(tUser);
        }
    }

    /**
     * 成员审核拒绝
     * @param user
     * @param userIds
     */
    @Override
    public void userReject(TUser user, String userIds) {
        if(userIds==null || userIds.isEmpty()) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"选定对象不能为空!");
        }

        Long ownerId = user.getId();
        Long companyId = getOwnCompanyId(ownerId);
        String[] split = new String[1];
        //处理userIds
        if(userIds.contains(",")) {	//批量
            split = userIds.split(",");
        } else {	//单个
            split[0] = userIds;
        }
        for(String userId:split) {
            TUserCompany userCompany = new TUserCompany();
            userCompany.setState(AppConstant.JOIN_STATE_COMPANY_REFUSE);
            userCompanyDao.updateByUserIdAndCompanyJobAndCompanyIdAndStateSetUserCompany(Long.valueOf(userId),AppConstant.JOB_COMPANY_MEMBER,companyId,AppConstant.JOIN_STATE_COMPANY_NOT_YET,userCompany);
        }
    }

    /**
     * 批量成员导入
     * @param user
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public List<String> multiUserInsert(TUser user, MultipartFile file) throws IOException {
        Long companyId = getOwnCompanyId(user.getId());

        List<String[]> readExcel = POIUtil.readExcel(file);
        // 获取<分组名，id> map (不含默认分组)
        Map<String, Long> map = new HashMap<String, Long>();
        // 默认分组
        Long defaultGroupId = null;
        List<TGroup> groups = groupDao.selectByCompanyId(companyId);
        if (!groups.isEmpty()) {
            for (TGroup group : groups) {
                if (AppConstant.GROUP_AUTH_DEFAULT.equals(group.getAuth())) {
                    defaultGroupId = group.getId(); // 默认组id
                } else {
                    map.put(group.getGroupName(), group.getId()); // 映射键值对
                }
            }
        }

        List<String> errorList = new ArrayList<String>();
        // 处理返回的数据 固定的顺序：组内昵称 手机号 性别 分组名
        for (String[] readLine : readExcel) {
            String errorInfo = null;
            String name = readLine[0];
            String telephone = readLine[1];
            String sexString = readLine[2].trim();
            String groupName = readLine[3];
            Integer sex = 0;
            if ("男".equals(sexString)) {
                sex = 1;
            } else if ("女".equals(sexString)) {
                sex = 2;
            }
            Long finalGroupId = defaultGroupId;
            Long groupId = map.get(groupName);
            if (groupId != null) {
                finalGroupId = groupId;
            }
            try {
                // 调用插入方法
                userInsert(user, finalGroupId, telephone, name, sex);
            } catch (MessageException e) {
                // 统计未加入成功列表
                errorInfo = "姓名为：" + name + ",手机号为：" + telephone + "的用户加入失败！原因：" + e.getMessage();
                errorList.add(errorInfo);
            } catch (Exception e) {
                errorInfo = "姓名为：" + name + ",手机号为：" + telephone + "的用户加入失败！原因：" + "服务器内部错误，请稍后重试";
                logger.error(errInfo(e));
            }
        }
        return errorList;
    }

	@Override
	public List<BaseGroupView> listGroup(TUser user) {

		// 从当前的组织用户中获取组织的id号
		Long companyId = null;
		try {
			companyId = getOwnCompanyId(user.getId());
		} catch (MessageException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

//		TGroupExample groupExample = new TGroupExample();
//		TGroupExample.Criteria groupCriteria = groupExample.createCriteria();
//		groupCriteria.andCompanyIdEqualTo(companyId).andIsValidEqualTo(IS_VALID_YES);
//		List<TGroup> listGroup = groupDao.selectByExample(groupExample);
		// 将组织id 及 组织人数放到map中，为了提升下面的查询效率
		List<TGroup> listGroup = groupDao.listGroup(companyId);
		Map<Long, Integer> groupPersonNumMap = new HashMap<>();
		for (int i = 0; i < listGroup.size(); i++) {
			TGroup group = listGroup.get(i);
			// 初始人数都为0
			groupPersonNumMap.put(group.getId(), 0);
		}
		// 因为页面需要显示每个组织的人数，所以需要将组织封装view加入每个组织的人数
		// 查询出该组织下面的所有人
		List<TUserCompany> allCompanyPerson = userCompanyDao.listCompanyUser(companyId);
//		TUserCompanyExample userCompanyExample = new TUserCompanyExample();
//		TUserCompanyExample.Criteria createCriteria = userCompanyExample.createCriteria();
//		// 状态为2的用户才会审核通过
//		createCriteria.andIsValidEqualTo(IS_VALID_YES).andStateEqualTo(2).andGroupIdIsNotNull().andCompanyIdEqualTo(companyId);
//		List<TUserCompany> allCompanyPerson = userCompanyDao.selectByExample(userCompanyExample);
		// 匹配对应的groupId 在map中进行人数的+1操作
		for (TUserCompany userCompany : allCompanyPerson) {
			Integer groupPersonNum = groupPersonNumMap.get(userCompany.getGroupId());
			groupPersonNum++;
			groupPersonNumMap.put(userCompany.getGroupId(), groupPersonNum);
		}
		// 进行view的转换 并将分组人数加入
		List<BaseGroupView> listGroupView = new ArrayList<BaseGroupView>();
		for (TGroup group : listGroup) {
		    com.e_commerce.miscroservice.user.po.TGroup tGroup = new com.e_commerce.miscroservice.user.po.TGroup();
		    tGroup = tGroup.exchangeTGroup(group);

			BaseGroupView groupView = tGroup.copyBaseGroupView();
			Integer groupPersonNum = groupPersonNumMap.get(group.getId());
			groupView.setPersonNum(groupPersonNum);
			listGroupView.add(groupView);
		}
		return listGroupView;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(TGroup group, TUser user) {
		// 从当前的组织用户中获取组织的id号
		Long companyId = null;
		try {
			companyId = getOwnCompanyId(user.getId());
		} catch (MessageException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		group.setCompanyId(companyId);
		if (group.getCompanyId() == null || StringUtil.isEmpty(group.getGroupName())) {
			logger.error("新建分组时没有加入组织ID或者组织名字是空的");
			throw new IllegalArgumentException("参数不正确,请刷新重试");
		}
		// 校验传来的组织ID在数据库中是否有效
		TCompany company = companyDao.selectByPrimaryKey(group.getCompanyId());
		// 错误的组织ID传递进来
		boolean inaccurateCompanyId = (company == null || company.getIsValid().equals(AppConstant.IS_VALID_NO)
				|| !Objects.equals(1, company.getStatus()));
		if (inaccurateCompanyId) {
			logger.error("新建分组时传递的组织参数不正确 组织ID为{}", group.getCompanyId());
			throw new IllegalArgumentException("参数不正确,请刷新重试");
		}

		Long currentTime = System.currentTimeMillis();
		// 这个分组是用户创建的
//		group.setId(snowflakeIdWorker.nextId());
		group.setIsValid(AppConstant.IS_VALID_YES);
		group.setAuth(GroupEnum.AUTH_CREATED.getValue());
		group.setCreateTime(currentTime);
		group.setCreateUser(user.getId());
		group.setCreateUserName(user.getName());
		group.setUpdateUser(user.getId());
		group.setUpdateTime(currentTime);
		group.setUpdateUserName(user.getName());
		groupDao.insert(group);
		logger.info("组织新建分组id为{}，名称为{}", group.getId(), group.getGroupName());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateGroup(TGroup group, TUser user) throws NoAuthChangeException {
		Long currentTime = System.currentTimeMillis();
		if (StringUtil.isEmpty(group.getGroupName()) || group.getId() == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		TGroup updateGroup = groupDao.selectByPrimaryKey(group.getId());
		if (updateGroup.getAuth().equals(GroupEnum.AUTH_DEFAULT.getValue())) {
			throw new NoAuthChangeException("默认分组无法修改");
		}
		if (updateGroup.getIsValid().equals(AppConstant.IS_VALID_NO)) {
			throw new IllegalArgumentException("参数不正确");
		}
		// 旧的分组名称，为了记录在日志中
		String oldGroupName = updateGroup.getGroupName();
		updateGroup.setGroupName(group.getGroupName());
		updateGroup.setUpdateTime(currentTime);
		updateGroup.setUpdateUser(user.getId());
		updateGroup.setUpdateUserName(user.getName());
		groupDao.updateByPrimaryKeySelective(updateGroup);
		logger.info("组织修改分组id为{}，名称从{}改为{}", group.getId(), oldGroupName, group.getGroupName());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(Long groupId, TUser user) throws NoAuthChangeException {
		Long currentTime = System.currentTimeMillis();
		if (groupId == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		TGroup delGroup = groupDao.selectByPrimaryKey(groupId);
		if (delGroup.getAuth().equals(GroupEnum.AUTH_DEFAULT.getValue())) {
			throw new NoAuthChangeException("默认分组无法删除");
		}
		if (delGroup.getIsValid().equals(AppConstant.IS_VALID_NO)) {
			throw new IllegalArgumentException("参数不正确");
		}
		// 查看该分组下面是否有用户，如果有用户，则不能进行删除
		long countUser = userCompanyDao.countGroupUser(groupId);
//		TUserCompanyExample userCompanyExample = new TUserCompanyExample();
//		TUserCompanyExample.Criteria createCriteria = userCompanyExample.createCriteria();
//		// 状态为2的用户才会在这个分组里面
//		createCriteria.andIsValidEqualTo(IS_VALID_YES).andGroupIdEqualTo(groupId).andStateEqualTo(2);
//		long countUser = userCompanyDao.countByExample(userCompanyExample);
		if (countUser > 0) {
			throw new NoAuthChangeException("该分组下还有用户，无法删除，请将用户晓时互助到其他分组再进行删除");
		}
		delGroup.setIsValid(AppConstant.IS_VALID_NO);
		delGroup.setUpdateTime(currentTime);
		delGroup.setUpdateUser(user.getId());
		delGroup.setUpdateUserName(user.getName());
		groupDao.updateByPrimaryKeySelective(delGroup);
		logger.info("组织删除分组id为{}，名称为{}的分组", groupId, delGroup.getGroupName());
	}

    /**
     * 新成员列表
     * @param user
     * @param pageNum
     * @param pageSize
     * @param param
     * @param skill
     * @return
     */
    @Override
    public QueryResult<SmartUserView> userWaitToJoin(TUser user, Integer pageNum, Integer pageSize, String param, String skill) {
        List<SmartUserView> resultList = new ArrayList<SmartUserView>();
        Long userId = user.getId();
        Long companyId = getOwnCompanyId(userId);

        if (pageNum == null) {
            pageNum = 1;
        }
        QueryResult<SmartUserView> queryResult = new QueryResult<SmartUserView>();
        queryResult.setResultList(resultList);
        String regex = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";

        List<TUserCompany> userCompanies = userCompanyDao.selectByCompanyIdAndState(companyId,AppConstant.JOIN_STATE_COMPANY_NOT_YET);
        // ids
        List<Long> idList = new ArrayList<Long>();
        for (TUserCompany userCompany : userCompanies) {
            idList.add(userCompany.getUserId());
        }
        if (idList.isEmpty()) { // 处理空的情况
            return queryResult;
        }
        //分页
        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        String name = null;
        String telephone = null;
        if (param != null) {
            if (param != "" && !param.matches(regex)) { // 姓名
                name = param;
            }
            if (param.matches(regex)) { // 手机号
                telephone = param;
            }
        }

        List<TUser> users = userDao.selectByNameAndTelephoneLikeSkillInIds(name,telephone,skill,idList, ApplicationEnum.XIAOSHI_APPLICATION.toCode());
        // 处理数据
        for (TUser theUser : users) {
            com.e_commerce.miscroservice.user.po.TUser tUser = new com.e_commerce.miscroservice.user.po.TUser();
            tUser = tUser.exchangeTUser(theUser);
            SmartUserView userView = tUser.copySmartUserView();
            userView.setIdString(String.valueOf(userView.getId()));
            Integer sex = theUser.getSex();
            String sexString = "未设置";
            if (sex == 1) {
                sexString = "男";
            } else if (sex == 2) {
                sexString = "女";
            }
            userView.setSexString(sexString);
            // 统计总共获得的互助时数目
            List<TUserTimeRecord> userTimeRecords = userTimeRecordDao.selectByUserId(theUser.getId());
            Long allGain = 0l;
            for (TUserTimeRecord userTimeRecord : userTimeRecords) {
                allGain += userTimeRecord.getTime();
            }
            userView.setOccupation(theUser.getOccupation() == null || "".equals(theUser.getOccupation()) ? "未设置"
                    : theUser.getOccupation());
            userView.setGainTimeString(timeChange(allGain)); // 获得互助时
            userView.setAgeString(theUser.getAge() == null ? "未设置" : theUser.getAge() + "岁"); // 年龄
            userView.setSkill(theUser.getSkill() == null ? "未设置" : theUser.getSkill()); // 技能
            userView.setTotalComment(
                    String.format("%.2f", (double) theUser.getServTotalEvaluate() / 3 / theUser.getServeNum())); // 保留2位小数
            resultList.add(userView);
        }

        queryResult.setResultList(resultList);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }

    /**
     * 组织年度收支折线数据
     * @param user
     * @return
     */
    @Override
    public CompanyRecentView companyPaymentDiagram(TUser user) {
        Long userId = user.getId();

        // 当月
        long currentTimeMillis = System.currentTimeMillis();
        String timeStamp2Date = DateUtil.timeStamp2Date(currentTimeMillis);
        String[] split = timeStamp2Date.split("-");
        int length = Integer.valueOf(split[1]);
        int year = Integer.valueOf(split[0]);

        // 收入结果
        int[] recentIn = new int[length];
        int[] recentOut = new int[length];

        // 支出结果
        // 当年收支总数据
        List<TUserTimeRecord> userTimeRecords = getUserTimeRecords(userId, year);
        for (TUserTimeRecord userTimeRecord : userTimeRecords) {
            String month = DateUtil.timeStamp2Month(userTimeRecord.getCreateTime());
            int index = Integer.valueOf(month) - 1;
            Long inUserId = userTimeRecord.getUserId();
            Long outUserId = userTimeRecord.getFromUserId();
            if (userId.equals(inUserId)) { // 收入数据
                recentIn[index] += userTimeRecord.getTime();
            } else if (userId.equals(outUserId)) { // 支出数据
                recentOut[index] += userTimeRecord.getTime();
            }
        }

        CompanyRecentView companyRecentView = new CompanyRecentView();
        companyRecentView.setInRecent(recentIn);
        companyRecentView.setOutRecent(recentOut);

        return companyRecentView;
    }

    /**
     * 获取当年收支
     * @param userId
     * @param year
     * @return
     */
    private List<TUserTimeRecord> getUserTimeRecords(Long userId, Integer year) {
        Map<String, Object> yearBetween = DateUtil.y2BetweenStamp(year);
        return userTimeRecordDao.selectByUserIdOrFromUserIdAndTypeBetween(userId,PaymentEnum.PAYMENT_TYPE_ACEPT_SERV.getCode(),(long) yearBetween.get("betLeft"), (long) yearBetween.get("betRight"));
    }

    /**
     * 成员移动
     * @param nowUser
     * @param groupId
     * @param userIds
     */
    public void userMove(TUser nowUser, Long groupId, List<Long> userIds) {
        Long nowTime = System.currentTimeMillis();
        Long companyId = getOwnCompanyId(nowUser.getId());
        List<TUserCompany> userCompanies = userCompanyDao.selectUserConpanysByUserIdsAndCompayId(userIds , companyId);
        if (userCompanies == null || userCompanies.size() <= 0) {
            throw new MessageException("选择用户非组内成员，请后退重试");
        }
        List<Long> userCompanyIdList = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            for (int j = 0; j < userCompanies.size(); j++) {
                if (userIds.get(i) == userCompanies.get(j).getUserId().longValue()) {
                    userCompanies.get(j).setGroupId(groupId);
                    userCompanies.get(j).setUpdateTime(nowTime);
                    userCompanies.get(j).setUpdateUser(nowUser.getId());
                    userCompanies.get(j).setUpdateUserName(nowUser.getName());
                    userCompanyIdList.add(userCompanies.get(j).getId());
                }
            }
        }
        if (userCompanies.size() > 0) {
            userCompanyDao.updateUserCompanyByLIst(userCompanies , userCompanyIdList);
        }
    }

    /**
     * 成员修改
     * @param nowUser
     * @param userName
     * @param userId
     * @param sex
     * @param groupId
     */
    public void userModify(TUser nowUser, String userName, Long userId, Integer sex, Long groupId) {
        long nowTime = System.currentTimeMillis();
        Long companyId = getOwnCompanyId(nowUser.getId());
        TUserCompany userCompany = null;
        userCompany = userCompanyDao.selectUserConpanysByUserIdAndCompayId(userId , companyId);
        if (userCompany == null ) {
            throw new MessageException("选择用户非组内成员，请后退重试");
        }
        //修改组内姓名
        if (userName != null){
            userCompany.setTeamName(userName);
        }
        //修改分组ID
        if (groupId != null){
            userCompany.setGroupId(groupId);
        }
        //修改性别
        if (sex != null){
            TUser user = new TUser();
            user.setId(userId);
            user.setSex(sex);
            userDao.updateByPrimaryKey(user);
        }
        if (userName != null || groupId != null){
            userCompany.setUpdateTime(nowTime);
            userCompany.setUpdateUser(nowUser.getId());
            userCompany.setUpdateUserName(nowUser.getName());
            userCompanyDao.updateByPrimaryKey(userCompany);
        }


    }

}
