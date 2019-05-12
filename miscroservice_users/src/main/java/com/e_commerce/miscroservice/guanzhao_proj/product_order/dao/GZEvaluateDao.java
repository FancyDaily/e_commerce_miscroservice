package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzEvaluate;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 09:53
 */
public interface GZEvaluateDao {

    int insert(TGzEvaluate gzEvaluate);

    List<TGzEvaluate> selectByUserIdAndLessonId(Long userId, Long lessonId);

    List<TGzEvaluate> selectByLessonId(Long lessonId);
}
