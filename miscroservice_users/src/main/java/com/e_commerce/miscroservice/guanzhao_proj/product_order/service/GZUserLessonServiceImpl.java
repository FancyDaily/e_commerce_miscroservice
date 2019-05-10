package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.GZUserLessonEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZUserLessonDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZUserSubjectDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserLesson;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 14:17
 */
@Service
public class GZUserLessonServiceImpl implements GZUserLessonService {

    @Autowired
    private GZUserLessonDao gzUserLessonDao;

    @Autowired
    private GZUserSubjectDao gzUserSubjectDao;

    /**
     * 完成章节学习
     *
     * @param userId
     * @param lessonId
     */
    @Override
    public void lessonComplete(Long userId, Long lessonId, Long subjectId) {
        long currentTimeMillis = System.currentTimeMillis();
        TGzUserLesson tGzUserLesson = gzUserLessonDao.selectByUserIdAndLessonId(userId, lessonId);
        if (Objects.isNull(tGzUserLesson)) {
            throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该章节不存在!");
        }
        //判断是否已经完成学习
        Integer lessonCompletionStatus = tGzUserLesson.getLessonCompletionStatus();
        if (!Objects.equals(GZUserLessonEnum.LESSON_COMPLETION_STATUS_YES.getCode(), lessonCompletionStatus)) {
            return;
        }

        tGzUserLesson.setLessonCompletionStatus(GZUserLessonEnum.LESSON_COMPLETION_STATUS_YES.getCode());
        //更新章节学习进度
        tGzUserLesson.setUpdateTime(currentTimeMillis);
        tGzUserLesson.setUpdateUser(userId);
        gzUserLessonDao.update(tGzUserLesson);

        //统计课程学习进度
        List<TGzUserLesson> gzUserLessonList = gzUserLessonDao.selectByUserIdAndSubjectId(userId, subjectId);
        if(gzUserLessonList.isEmpty()) {
            return;
        }

        int totalCnt = 0;
        int completionCnt = 0;
        for (TGzUserLesson gzUserLesson : gzUserLessonList) {
            totalCnt++;
            completionCnt += Objects.equals(gzUserLesson.getLessonCompletionStatus(), GZUserLessonEnum.LESSON_COMPLETION_STATUS_YES.getCode()) ? 1 : 0;
        }
        double result = (Float.valueOf(completionCnt) / Float.valueOf(totalCnt)) * 100;
        String temp = String.valueOf(result).substring(0,2);
        Integer completion = Integer.valueOf(temp.endsWith(".")? temp.substring(0,temp.length()-1): temp);

        //更新课程学习进度
        TGzUserSubject tGzUserSubject = gzUserSubjectDao.selectByUserIdAndSubjectId(userId, lessonId);
        tGzUserSubject.setCompletion(completion);
        tGzUserSubject.setUpdateTime(currentTimeMillis);
        tGzUserSubject.setUpdateUser(userId);
        gzUserSubjectDao.updateByPrimaryKey(tGzUserSubject);
    }
}
