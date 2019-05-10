package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-08 20:58
 */
public interface GZLessonDao {

    List<TGzLesson> selectBySubjectId(Long subjectId);

    TGzLesson selectByPrimaryKey(Long lessonId);

    TGzLesson selectBySubjectIdAndName(Long subjectId, String fileName);

    int updateByPrimaryKey(TGzLesson tGzLesson);
}
