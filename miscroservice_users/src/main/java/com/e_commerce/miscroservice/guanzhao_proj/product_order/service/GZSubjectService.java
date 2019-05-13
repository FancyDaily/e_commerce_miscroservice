package com.e_commerce.miscroservice.guanzhao_proj.product_order.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzEvaluate;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.SubjectInfosVO;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;

import java.util.List;

public interface GZSubjectService {

    /**
     * 在售商品列表
     * @return
     * @param pageNum
     * @param pageSize
     */
    QueryResult subjectList(Integer pageNum, Integer pageSize);

    /**
     * 商品详情
     * @param subjectId
     * @return
     */
    SubjectInfosVO subjectDetail(Long subjectId);

    /**
     * 章节列表
     * @param subjectId
     * @return
     */
    List<TGzLesson> lessonList(Long subjectId);

    /**
     * 评价章节
     * @param user
     * @param evaluate
     */
    void evaluateLesson(TUser user, TGzEvaluate evaluate);

    /**
     * 解锁课程(统一解锁：解锁该课程下的所有章节)
     * @param subjectId
     */
    void unlockSubject(Long subjectId);

    /**
     * 查询我的正在学习
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    QueryResult<MyLearningSubjectVO> findMyLearningSubject(Integer id, Integer pageNum, Integer pageSize);

    /**
     * 已结束的课程
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    QueryResult<MyLearningSubjectVO> findEndingSubject(Integer id, Integer pageNum, Integer pageSize);
}
