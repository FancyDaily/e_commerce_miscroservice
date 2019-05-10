package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.MqTemplate;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.service.TimerScheduler;
import com.e_commerce.miscroservice.commons.enums.application.GZLessonEnum;
import com.e_commerce.miscroservice.commons.enums.application.GZSubjectEnum;
import com.e_commerce.miscroservice.commons.enums.application.KeyValueEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.MqChannelEnum;
import com.e_commerce.miscroservice.commons.enums.colligate.TimerSchedulerTypeEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 09:22
 */
@Service
public class GZLessonServiceImpl implements GZLessonService {

    @Autowired
    private GZLessonDao gzLessonDao;

    @Autowired
    private GZSubjectDao gzSubjectDao;

    @Autowired
    private GZUserSubjectDao gzUserSubjectDao;

    @Autowired
    private GZUserLessonDao gzUserLessonDao;

    @Autowired
    private GZKeyValueDao keyValueDao;

    @Autowired
    @Lazy
    MqTemplate mqTemplate;

    @Override
    public void updateLearningCompletion(Long lessonId) {
        TGzLesson lesson = gzLessonDao.selectByPrimaryKey(lessonId);
    }

    @Override
    public void unlockLesson(Long subjectId, String fileName) {
        long currentTimeMillis = System.currentTimeMillis();
        //校验
        TGzLesson tGzLesson = gzLessonDao.selectBySubjectIdAndName(subjectId, fileName);
        if(Objects.isNull(tGzLesson)) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该课程下没有该章节！请确认您的文件名称!");
        }

        //解锁开始立即解锁  TODO 定时解锁
        Long lessonId = tGzLesson.getId();
/*
        //TODO 定时
        TimerScheduler scheduler = new TimerScheduler();
        scheduler.setType(TimerSchedulerTypeEnum.ORDER_OVERTIME_END.toNum());
        scheduler.setName("lower_order" + UUID.randomUUID().toString());
        scheduler.setCron(DateUtil.genCron(tGzLesson.getAvailableDate() + "" + tGzLesson.getAvailableTime()));
        Map<String, String> map = new HashMap<>();
        map.put("subjectId", String.valueOf(subjectId));
        map.put("lessonId", String.valueOf(lessonId));
        scheduler.setParams(JSON.toJSONString(map));

        mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_SEND.toName(), JSONObject.toJSONString(scheduler));
*/

        Integer lessonAvailableStatus = tGzLesson.getAvaliableStatus();
        if(Objects.isNull(lessonAvailableStatus) || Objects.equals(GZLessonEnum.AVAILABLE_STATUS_NO.getCode(),lessonAvailableStatus)) {
            tGzLesson.setAvaliableStatus(GZLessonEnum.AVAILABLE_STATUS_YES.getCode());
            tGzLesson.setUpdateTime(currentTimeMillis);
            gzLessonDao.updateByPrimaryKey(tGzLesson);
        }

        //如果对应subject没有解锁，则解锁
        TGzSubject subject = gzSubjectDao.selectByPrimaryKey(subjectId);
        if(Objects.isNull(subject)) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "找不到该课程！请确认课程编号");
        }

        Integer avaliableStatus = subject.getAvaliableStatus();
        if(Objects.isNull(avaliableStatus) || Objects.equals(GZSubjectEnum.AVAILABLE_STATUS_FALSE.getCode(),avaliableStatus)) {
            subject.setAvaliableStatus(GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
            subject.setUpdateTime(currentTimeMillis);
            gzSubjectDao.updateByPrimaryKey(subject);
        }

        //找到所有购买此课程的用户
        List<TGzUserSubject> tGzUserSubjects = gzUserSubjectDao.selectBySubjectId(subjectId);
        List<TGzUserLesson> toInserter = new ArrayList<>();
        for(TGzUserSubject tGzUserSubject:tGzUserSubjects) {
            //创建user-lesson关系
            Long userId = tGzUserSubject.getUserId();
            TGzUserLesson userLesson = new TGzUserLesson();
            userLesson.setLessonId(lessonId);
            userLesson.setSubjectId(subjectId);
            userLesson.setUserId(userId);
            userLesson.setCreateTime(currentTimeMillis);
            userLesson.setCreateUser(userId);
            userLesson.setUpdateTime(currentTimeMillis);
            userLesson.setUpdateUser(userId);
            userLesson.setIsValid(AppConstant.IS_VALID_YES);
            toInserter.add(userLesson);

            //生成sign，插入key-value
            String sourceStr = userId.toString() + lessonId.toString() + subjectId.toString();
            String sign = Md5Util.md5(sourceStr);
            TGzKeyValue keyValue = new TGzKeyValue();
            keyValue.setType(KeyValueEnum.TYPE_SIGN.toCode());
            keyValue.setKey(userId.toString());
            keyValue.setValue(sign);
            keyValueDao.insert(keyValue);
        }

        try {
            gzUserLessonDao.batchInsert(toInserter);
        } catch (Exception e) {

        }

        //解锁
        /*Long lessonId = tGzLesson.getId();
        Long userId = null;
        List<TGzUserLesson> tGzUserLessonList = gzUserLessonDao.selectByLessonId(lessonId);
        List<TGzUserLesson> toUpdater = new ArrayList<>();
        List<Long> toUpdaterIds = new ArrayList<>();
        for(TGzUserLesson tGzUserLesson:tGzUserLessonList) {
            toUpdaterIds.add(tGzUserLesson.getId());
            userId = tGzUserLesson.getUserId();
            tGzUserLesson.setStatus(GZUserLessonEnum.STATUS_AVAILABLE.getCode());
            tGzUserLesson.setUpdateTime(System.currentTimeMillis());
            toUpdater.add(tGzUserLesson);
        }
        gzUserLessonDao.batchUpdate(toUpdater, toUpdaterIds);*/

    }

    @Override
    public void authCheck(String sign, Long userId, String name, Long productId, Long lessonId) {
        String sourceStr = userId + lessonId + productId + "";
        String expectedSign = Md5Util.md5(sourceStr);
        //校验sign
        if(!Objects.equals(expectedSign, sign)) {
            throw new MessageException("对不起，你没有权限观看!");
        }
        //校验user
        TGzKeyValue keyValue = keyValueDao.selectByTypeAndValue(KeyValueEnum.TYPE_SIGN.toCode(), sign);
        if(!Objects.equals(userId + "", keyValue.getKey())) {
            throw new MessageException("对不起, 您没有权限观看!");
        }

    }

    @Override
    public void sendUnlockTask(Long subjectId, String fileName) {
        unlockLesson(subjectId, fileName);
    }
}
