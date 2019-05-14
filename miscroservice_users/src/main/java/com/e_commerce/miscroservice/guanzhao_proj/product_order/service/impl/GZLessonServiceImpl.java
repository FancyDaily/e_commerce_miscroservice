package com.e_commerce.miscroservice.guanzhao_proj.product_order.service.impl;

import com.e_commerce.miscroservice.commons.config.colligate.MqTemplate;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.*;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.*;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLessonVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private GZEvaluateDao gzEvaluateDao;

    @Autowired
    @Lazy
    MqTemplate mqTemplate;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void updateLearningCompletion(Long lessonId) {
        TGzLesson lesson = gzLessonDao.selectByPrimaryKey(lessonId);
    }

    @Override
    public void unlockLesson(Long subjectId, String fileName) {
        long currentTimeMillis = System.currentTimeMillis();
        //校验
        if(fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        TGzLesson tGzLesson = gzLessonDao.selectBySubjectIdAndName(subjectId, fileName);
        if (Objects.isNull(tGzLesson)) {
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
        if (Objects.isNull(lessonAvailableStatus) || Objects.equals(GZLessonEnum.AVAILABLE_STATUS_NO.getCode(), lessonAvailableStatus)) {
            tGzLesson.setAvaliableStatus(GZLessonEnum.AVAILABLE_STATUS_YES.getCode());
            gzLessonDao.updateByPrimaryKey(tGzLesson);
        }

        //如果对应subject没有解锁，则解锁
        TGzSubject subject = gzSubjectDao.selectByPrimaryKey(subjectId);
        if (Objects.isNull(subject)) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "找不到该课程！请确认课程编号");
        }

        Integer avaliableStatus = subject.getAvaliableStatus();
        if (Objects.isNull(avaliableStatus) || Objects.equals(GZSubjectEnum.AVAILABLE_STATUS_FALSE.getCode(), avaliableStatus)) {
            subject.setAvaliableStatus(GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
            gzSubjectDao.updateByPrimaryKey(subject);
        }

        //找到所有购买此课程的用户
        List<TGzUserSubject> tGzUserSubjects = gzUserSubjectDao.selectBySubjectId(subjectId);
        List<TGzUserLesson> toInserter = new ArrayList<>();
        List<TGzUserLesson> toUpdater = new ArrayList<>();
        List<Long> toUpdaterIds = new ArrayList<>();
        for (TGzUserSubject tGzUserSubject : tGzUserSubjects) {
            Long userId = tGzUserSubject.getUserId();
            //生成sign，插入key-gzvalue
            String sourceStr = userId.toString() + lessonId.toString() + subjectId.toString();
            String sign = Md5Util.md5(sourceStr);
            TGzKeyValue keyValue = new TGzKeyValue();
            keyValue.setType(KeyValueEnum.TYPE_SIGN.toCode());
            keyValue.setGzkey(userId.toString());
            keyValue.setGzvalue(sign);
            keyValueDao.insert(keyValue);

            //创建user-lesson关系
            TGzUserLesson userLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
            if(userLesson==null) {
                userLesson = new TGzUserLesson();
                userLesson.setLessonId(lessonId);
                userLesson.setSubjectId(subjectId);
                userLesson.setUserId(userId);
                userLesson.setCreateUser(userId);
                userLesson.setUpdateUser(userId);
                userLesson.setIsValid(AppConstant.IS_VALID_YES);
                userLesson.setVideoCompletion(0);
                userLesson.setVideoCompletionStatus(GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_NO.getCode());
                userLesson.setStatus(GZUserLessonEnum.STATUS_AVAILABLE.getCode());
                userLesson.setSign(sign);
                toInserter.add(userLesson);
                continue;
            }

            userLesson.setStatus(GZUserLessonEnum.STATUS_AVAILABLE.getCode());
            userLesson.setSign(sign);
            toUpdaterIds.add(userLesson.getId());
            toUpdater.add(userLesson);
        }

        try {
            if(!toInserter.isEmpty()) {
             gzUserLessonDao.batchInsert(toInserter);
            }
            if(!toUpdater.isEmpty()) {
                gzUserLessonDao.batchUpdate(toUpdater, toUpdaterIds);
            }
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
    public void authCheck(String sign, Long userId, String name, Long subjectId, Long lessonId) {
        String sourceStr = userId + "" + lessonId + "" + subjectId;
        String expectedSign = Md5Util.md5(sourceStr);
        MessageException messageException = new MessageException("对不起，你没有权限观看!");
        //校验sign
        if (!Objects.equals(expectedSign, sign)) {
            throw messageException;
        }
        //校验user
        TGzKeyValue keyValue = keyValueDao.selectByTypeAndValue(KeyValueEnum.TYPE_SIGN.toCode(), sign);
        if (!Objects.equals(userId + "", keyValue.getGzkey())) {
            throw messageException;
        }
        //校验课程有效权限
        TGzUserSubject userSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
        if(Objects.isNull(userSubject) || System.currentTimeMillis() > userSubject.getExpireTime()) {
            throw messageException;
        }
        //章节开放
        TGzLesson tGzLesson = gzLessonDao.selectByPrimaryKey(lessonId);
        messageException = new MessageException("未到开放时间！");
        boolean flag = false;
        if(Objects.isNull(tGzLesson)) {
            throw messageException;
        }
        String availableDate = tGzLesson.getAvailableDate();
        String availableTime = tGzLesson.getAvailableTime();
        String wholeDateTime = availableDate + availableTime;
        Long availableStamp = Long.valueOf(DateUtil.dateTimeToStamp(wholeDateTime));
        flag = System.currentTimeMillis() < availableStamp; //未到章节开放时间

        if(flag) {
            throw messageException;
        }

    }

    @Override
    public void sendUnlockTask(Long subjectId, String fileName) {
        unlockLesson(subjectId, fileName);
    }

    @Override
    public QueryResult mySubjectLessonList(Long userId, Long subjectId, Integer pageNum, Integer pageSize) {
        //校验开课
        TGzSubject gzSubject = gzSubjectDao.selectByPrimaryKey(subjectId);

        Integer avaliableStatus = gzSubject.getAvaliableStatus();
        String dateTime = gzSubject.getAvailableDate() + gzSubject.getAvailableTime();
        Long expectedStamp = Long.valueOf(DateUtil.dateTimeToStamp(dateTime));
        long currentTimeMillis = System.currentTimeMillis();
        boolean statusAvailableFlag = !Objects.equals(avaliableStatus, GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
        boolean timeAvailableFlag = currentTimeMillis > expectedStamp;
        if (timeAvailableFlag && !statusAvailableFlag) {    //如果到时间、状态错误，修正（仅修正课程状态）
            gzSubject.setAvaliableStatus(GZSubjectEnum.AVAILABLE_STATUS_TRUE.getCode());
            gzSubjectDao.updateByPrimaryKey(gzSubject);
        }
        //我的课程记录
        TGzUserSubject tGzUserSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
        Long expireTime = tGzUserSubject.getExpireTime();   //过期时间
        boolean expired = System.currentTimeMillis() > expireTime; //我的学习权限是否过期
        if (Objects.isNull(tGzUserSubject) || expired) {    //TODO 失效是否可以看
            throw new MessageException("对不起, 您没有权限查看该课程");
        }

        //匹配查找我的章节状态
        List<MyLessonVO> myLessonVOS = new ArrayList<>();
        Page<Object> startPage = PageHelper.startPage(pageNum==null?1:pageNum, pageSize==null?0:pageSize);
        List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);

        List<TGzUserLesson> gzUserLessonList = gzUserLessonDao.selectByUserIdAndSubjectId(userId, subjectId);
        Map<Long, Object> lessonIdAvailableStatusMap = new HashMap<>();
        for (TGzUserLesson gzUserLesson : gzUserLessonList) {
            Integer status = gzUserLesson.getStatus();
            if (Objects.equals(status, GZUserLessonEnum.STATUS_AVAILABLE.getCode())) {
                lessonIdAvailableStatusMap.put(gzUserLesson.getLessonId(), gzUserLesson);
            }
        }

        for (TGzLesson tGzLesson : tGzLessons) {
            MyLessonVO myLessonVO = tGzLesson.copyMyLessonVO();
            Object existObject = lessonIdAvailableStatusMap.get(tGzLesson.getId());
            Integer status = GZUserLessonEnum.STATUS_AVAILABLE.getCode();
            Integer videoCompletion = 0;
            Integer lessonCompletionStatus = GZUserLessonEnum.LESSON_COMPLETION_STATUS_NO.getCode();
            String sign = null;
            if(existObject!=null) {
                TGzUserLesson userLesson = (TGzUserLesson) existObject;
                videoCompletion = userLesson.getVideoCompletion();
                lessonCompletionStatus = userLesson.getLessonCompletionStatus();
                status = GZUserLessonEnum.STATUS_UNAVAILABLE.getCode();
                sign = userLesson.getSign();
            }
            myLessonVO.setSign(sign);
            myLessonVO.setLessonIndex(tGzLesson.getLessonIndex());
            myLessonVO.setAvaliableStatus(tGzLesson.getAvaliableStatus());
            myLessonVO.setAvailableDate(tGzLesson.getAvailableDate());
            myLessonVO.setAvailableTime(tGzLesson.getAvailableTime());
            myLessonVO.setVideoCompletion(videoCompletion);
            myLessonVO.setLessonCompletionStatus(lessonCompletionStatus);
            myLessonVO.setLessonId(tGzLesson.getId());
            myLessonVO.setStatus(status);
            myLessonVO.setUserId(userId);
            myLessonVOS.add(myLessonVO);
        }

        QueryResult result = new QueryResult();
        result.setTotalCount(startPage.getTotal());
        result.setResultList(myLessonVOS);

        return result;
    }

    @Override
    public void updateVideoCompletion(Long userId, Long lessonId, Integer currentSeconds, Integer totalSeconds) {
        currentSeconds = currentSeconds==null? 0: currentSeconds;
        totalSeconds = totalSeconds==null? 0:totalSeconds;
        double currentDouble = currentSeconds;
        double totalDouble = totalSeconds;
        Integer completion = (int)(currentDouble / totalDouble * 100);

        String key = userId.toString() + lessonId;
        //校验该进度是否已经更新
        Integer attribute = (Integer) request.getSession().getAttribute(key);
        if(attribute!=null && !attribute.equals(completion)) {
            return;
        }

        request.getSession().setAttribute(key, completion);

        //校验
        currentSeconds = currentSeconds > 100? 100: currentSeconds;
        //更新
        TGzUserLesson tGzUserLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
        if(tGzUserLesson==null) {
            return;
        }
        Integer comletionStatus = tGzUserLesson.getLessonCompletionStatus();
        int expectedCompletionStatus = currentSeconds > 90 ? GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode() : GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_NO.getCode();
        comletionStatus = Objects.equals(comletionStatus,GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode())? comletionStatus:expectedCompletionStatus;
        tGzUserLesson.setVideoCompletion(currentSeconds);
        tGzUserLesson.setVideoCompletionStatus(comletionStatus);
        tGzUserLesson.setLessonCompletionStatus(comletionStatus);
        gzUserLessonDao.update(tGzUserLesson);
        //统计章节进度，更新课程学习进度
        Long subjectId = tGzUserLesson.getSubjectId();
        Integer completeCnt = 0;
        Integer totalCnt = 0;
        List<TGzLesson> tGzLessons = gzLessonDao.selectBySubjectId(subjectId);
        totalCnt = tGzLessons.size();
        List<TGzUserLesson> gzUserLessonList = gzUserLessonDao.selectByUserIdAndSubjectId(userId, subjectId);
        for(TGzUserLesson gzUserLesson:gzUserLessonList) {
            if(Objects.equals(gzUserLesson.getLessonCompletionStatus(), GZUserLessonEnum.LESSON_COMPLETION_STATUS_DONE_YES.getCode())) {
                completeCnt++;
            }
        }
        Integer subjectCompletion = (totalCnt==0? 0:completeCnt/totalCnt) * 100;

        TGzUserSubject tGzUserSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, subjectId);
        tGzUserSubject.setCompletion(subjectCompletion);
        gzUserSubjectDao.updateByPrimaryKey(tGzUserSubject);
    }

    @Override
    public QueryResult<TGzEvaluate> lessonEvaluateList(Long subjectId, Long lessonId, Integer pageNum, Integer pageSize) {
        pageNum = pageNum==null?1:pageNum;
        pageSize = pageSize==null?0:pageSize;

        Page<Object> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TGzEvaluate> gzEvaluates = gzEvaluateDao.selectByLessonId(lessonId);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotalCount(startPage.getTotal());
        queryResult.setResultList(gzEvaluates);
        return queryResult;
    }

}
