package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserFollow;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.dao.UserFollowDao;
import com.e_commerce.miscroservice.user.service.FocusService;
import com.e_commerce.miscroservice.user.vo.DesensitizedUserView;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FocusServiceImpl implements FocusService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private UserDao userDao;

    private SnowflakeIdWorker idGenerator = new SnowflakeIdWorker();

    /**
     * 关注
     *
     * @param user
     * @param userFollowId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void submit(TUser user, Long userFollowId) {
        Long id = user.getId();

        // 非空校验
        if (userFollowId == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "关注对象id不能为空");
        }

        //获取目标用户
        TUser targetUser = userDao.selectByPrimaryKey(userFollowId);
        Integer followNum = targetUser.getFollowNum();

        Map<String, Object> recordMap = userFollowDao.findRecords(id, userFollowId);
        //互相关注
        boolean isFollowTheSame = (boolean) recordMap.get("isFollowTheSame");   //操作之前的关注状态
        //我-关注对象 关注记录
        TUserFollow follow = (TUserFollow) recordMap.get("follow");
        //关注对象-我 关注记录
        TUserFollow befollowed = (TUserFollow) recordMap.get("beFollowed");

        // 是否存在记录
        if (follow != null) {  //本次业务为取消关注
            follow.setIsValid(AppConstant.IS_VALID_NO);
            //改变互关状态
            if (isFollowTheSame) { //TODO 如果我关注的对象也关注我(互关)
                befollowed.setExtend(AppConstant.IS_ATTEN_THE_SAME_NO);
                userFollowDao.update(befollowed);

                follow.setExtend(AppConstant.IS_ATTEN_THE_SAME_NO);
            }
            userFollowDao.update(follow);

            //TODO 对方的粉丝数目减1
            if(followNum>0) {
                targetUser.setFollowNum(targetUser.getFollowNum() - 1);
                userDao.updateByPrimaryKey(targetUser);
            }
            return;
        }

        //本次业务为关注
        // 插入新记录
        TUserFollow userFollow = new TUserFollow();
        userFollow.setId(idGenerator.nextId());
        userFollow.setIsValid(AppConstant.IS_VALID_YES);
        userFollow.setUserId(id);
        userFollow.setUserFollowId(userFollowId);
        // creater & updater
        userFollow.setCreateTime(System.currentTimeMillis());
        userFollow.setUpdateTime(System.currentTimeMillis());
        userFollow.setCreateUser(id);
        userFollow.setCreateUserName(user.getName());
        userFollow.setUpdateUser(id);
        userFollow.setUpdateUserName(user.getName());
        // extend
        if (!isFollowTheSame && befollowed!=null) { //TODO 如果我关注的对象也关注我(操作后)
            //将符合条件的两条记录改为互关
            befollowed.setExtend(AppConstant.IS_ATTEN_THE_SAME_YES);
            userFollowDao.update(befollowed);

            userFollow.setExtend(AppConstant.IS_ATTEN_THE_SAME_YES);
        }
        userFollowDao.insert(userFollow);

        //TODO 对方的粉丝数目加1
        targetUser.setFollowNum(targetUser.getFollowNum() + 1);
        userDao.updateByPrimaryKey(targetUser);
    }

    /**
     * 关注/粉丝列表
     *
     * @param user
     * @param lastTime
     * @param pageSize
     * @return
     */
    @Override
    public QueryResult<DesensitizedUserView> myList(TUser user, Long lastTime, Integer pageSize, boolean isFocus) {
        //主键
        Long userId = user.getId();

        // 非空
        if (lastTime == null) {
            lastTime = System.currentTimeMillis();
        }

        if (pageSize == null) {
            pageSize = 0;
        }

        // PageHelper
        Page<Object> startPage = PageHelper.startPage(0, pageSize);

        // 查询关注表
        List<TUserFollow> userFollows = new ArrayList<>();
        if (isFocus) {
            userFollows = userFollowDao.findUserIdRecords(userId, lastTime);
        } else {
            userFollows = userFollowDao.findUserFollowIdRecords(userId, lastTime);
        }

        // idlist & timeStampMap
        List<Long> idList = new ArrayList<>();
        Map<Long, Object> timeStampMap = new HashMap<>();
        Map<Long, Object> isAttenMap = new HashMap<>();
        for (TUserFollow userFollow : userFollows) {
            if (isFocus) {
                idList.add(userFollow.getUserFollowId());
                timeStampMap.put(userFollow.getUserFollowId(), userFollow.getCreateTime());    //creatTime
                isAttenMap.put(userFollow.getUserFollowId(), userFollow.getExtend());    //记录互相关注状态的map
            } else {
                idList.add(userFollow.getUserId());
                timeStampMap.put(userFollow.getUserId(), userFollow.getCreateTime());           //creatTime
                isAttenMap.put(userFollow.getUserId(), userFollow.getExtend());     //记录互相关注状态的map
            }
        }

        if (idList.size() == 0) {
            return new QueryResult<>();
        }

        // 查询用户
        List<TUser> users = userDao.queryByIds(idList);

        //String化
        List<DesensitizedUserView> userList = new ArrayList<>();
        for (TUser theUser : users) {
            DesensitizedUserView userView = BeanUtil.copy(theUser, DesensitizedUserView.class);
            userView.setIdStr(String.valueOf(userView.getId()));
            userView.setTimeStamp(String.valueOf(timeStampMap.get(userView.getId())));
            //互相关注状态
            String extendStr = (String) isAttenMap.get(userView.getId());
            userView.setIsAtten(AppConstant.IS_ATTEN_THE_SAME_YES.equals(extendStr) ? AppConstant.IS_ATTEN_YES : AppConstant.IS_ATTEN_NO);

            userList.add(userView);
        }

        //排序
        Collections.sort(userList, new Comparator<DesensitizedUserView>() {

            @Override
            public int compare(DesensitizedUserView o1, DesensitizedUserView o2) {
                return o2.getTimeStamp().compareTo(o1.getTimeStamp());
            }
        });

        QueryResult<DesensitizedUserView> queryResult = new QueryResult<>();
        queryResult.setResultList(userList);
        queryResult.setTotalCount(startPage.getTotal());

        return queryResult;
    }
}
