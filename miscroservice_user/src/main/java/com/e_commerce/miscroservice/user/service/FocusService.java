package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.user.vo.DesensitizedUserView;

public interface FocusService {

    /**
     * 关注
     * @param user
     * @param userFollowId
     */
    void submit(TUser user, Long userFollowId);

    QueryResult<DesensitizedUserView> myList(TUser user, Long lastTime, Integer pageSize, boolean isFocus);
    /*
    *//**
     * 关注列表
     * @param user
     * @param lastTime
     * @param pageSize
     * @return
     *//*
    QueryResult<DesensitizedUserView> focusList(TUser user, Long lastTime, Integer pageSize);

    *//**
     * 粉丝列表
     * @param user
     * @param lastTime
     * @param pageSize
     * @return
     *//*
    QueryResult<DesensitizedUserView> fanList(TUser user, Long lastTime, Integer pageSize);
    */

}
