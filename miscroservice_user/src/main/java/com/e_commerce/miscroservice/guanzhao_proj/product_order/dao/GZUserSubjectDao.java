package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserSubject;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 15:04
 */
public interface GZUserSubjectDao {

    int updateByPrimaryKey(TGzUserSubject tGzUserSubject);

    List<TGzUserSubject> selectByPrimaryKey(Long id);

    int batchUpdate(List<TGzUserSubject> updaters, List<Long> userSubjectIds);

    TGzUserSubject selectByUserIdAndSubjectId(Long userId, Long lessonId);

    List<TGzUserSubject> selectBySubjectId(Long subjectId);

    /**
     * 保存我的课程
     * @param tGzUserSubject
     */
    void insert(TGzUserSubject tGzUserSubject);

    List<TGzUserSubject> selectByUserId(Integer id);
}
