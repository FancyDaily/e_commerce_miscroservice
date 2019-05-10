package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserLesson;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 14:15
 */
public interface GZUserLessonDao {

    TGzUserLesson selectByUserIdAndLessonId(Long userId, Long lessonId);

    int insert(TGzUserLesson... tGzUserLesson);

    int update(TGzUserLesson tGzUserLesson);

    List<TGzUserLesson> selectByUserIdAndSubjectId(Long userId, Long subjectId);

    List<TGzUserLesson> selectBySubjectIdInUserIds(Long subjectId, List<Long> userIds);

    List<TGzUserLesson> selectByLessonId(Long id);

    int batchUpdate(List<TGzUserLesson> toUpdater, List<Long> toUpdaterIds);

    int batchInsert(List<TGzUserLesson> toInserter);
}
