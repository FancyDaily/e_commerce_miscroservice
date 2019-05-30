package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzEvaluate;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 09:22
 */
public interface GZLessonService {
    void updateLearningCompletion(Long lessonId);

    void unlockMyLesson(Long userId, Long subjectId);

    void unlockLesson(Long subjectId, Long lessonId, String fileName);

    void authCheck(String sign, Long id, Long productId, Long fileName);

    void sendUnlockTask(Long subjectId, Long lessonId, String fileName);

    QueryResult mySubjectLessonList(Long userId, Long subjectId, Integer pageNum, Integer pageSize);

    void updateVideoCompletion(Long userId, Long lessonId, Long videoId, Integer currentSeconds, Integer totalSeconds);

    QueryResult<TGzEvaluate> lessonEvaluateList(Long subjectId, Long lessonId, Integer pageNum, Integer pageSize);
}
