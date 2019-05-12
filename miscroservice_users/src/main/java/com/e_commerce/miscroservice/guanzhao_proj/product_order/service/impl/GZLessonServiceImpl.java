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
    private GZEvaluateDao gzEvaluateDao;

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
        for (TGzUserSubject tGzUserSubject : tGzUserSubjects) {
            //创建user-lesson关系
            Long userId = tGzUserSubject.getUserId();
            TGzUserLesson userLesson = new TGzUserLesson();
            userLesson.setLessonId(lessonId);
            userLesson.setSubjectId(subjectId);
            userLesson.setUserId(userId);
            userLesson.setCreateUser(userId);
            userLesson.setUpdateUser(userId);
            userLesson.setIsValid(AppConstant.IS_VALID_YES);
            toInserter.add(userLesson);

            //生成sign，插入key-gzvalue
            String sourceStr = userId.toString() + lessonId.toString() + subjectId.toString();
            String sign = Md5Util.md5(sourceStr);
            TGzKeyValue keyValue = new TGzKeyValue();
            keyValue.setType(KeyValueEnum.TYPE_SIGN.toCode());
            keyValue.setGzkey(userId.toString());
            keyValue.setGzvalue(sign);
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
    public void authCheck(String sign, Long userId, String name, Long subjectId, Long lessonId) {
        String sourceStr = userId + "" + lessonId + "" + subjectId;
        String expectedSign = Md5Util.md5(sourceStr);
        //校验sign
        if (!Objects.equals(expectedSign, sign)) {
            throw new MessageException("对不起，你没有权限观看!");
        }
        //校验user
        TGzKeyValue keyValue = keyValueDao.selectByTypeAndValue(KeyValueEnum.TYPE_SIGN.toCode(), sign);
        if (!Objects.equals(userId + "", keyValue.getGzkey())) {
            throw new MessageException("对不起, 您没有权限观看!");
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
        if (Objects.isNull(tGzUserSubject) || Objects.equals(tGzUserSubject.getStatus(), GZUserSubjectEnum.STATUS_EXPIRED)) {    //TODO 失效是否可以看
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
            if(existObject!=null) {
                TGzUserLesson userLesson = (TGzUserLesson) existObject;
                videoCompletion = userLesson.getVideoCompletion();
                lessonCompletionStatus = userLesson.getLessonCompletionStatus();
                status = GZUserLessonEnum.STATUS_UNAVAILABLE.getCode();
            }
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
    public void updateVideoCompletion(Long userId, Long lessonId, Integer completion) {
        //校验
        completion = completion > 100? 100:completion;
        //更新
        TGzUserLesson tGzUserLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
        if(tGzUserLesson==null) {
            return;
        }
        Integer comletionStatus = tGzUserLesson.getLessonCompletionStatus();
        int expectedCompletionStatus = completion > 90 ? GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode() : GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_NO.getCode();
        comletionStatus = Objects.equals(comletionStatus,GZUserLessonEnum.VEDIO_COMPLETION_STATUS_DONE_YES.getCode())? comletionStatus:expectedCompletionStatus;
        tGzUserLesson.setVideoCompletion(completion);
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
