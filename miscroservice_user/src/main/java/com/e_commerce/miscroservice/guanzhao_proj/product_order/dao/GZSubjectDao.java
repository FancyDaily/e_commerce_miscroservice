package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;

import java.util.List;

public interface GZSubjectDao {

    List<TGzSubject> selectByAvailableStatus(Integer availableStatus);

    TGzSubject selectByPrimaryKey(Long subjectId);

    int updateByPrimaryKey(TGzSubject subject);

    /**
     * 查看我的正在学习列表
     * @param id
     * @param currentTimeMillis
     * @return
     */
    List<MyLearningSubjectVO> findMyLearningSubject(Long id, long currentTimeMillis);

    List<TGzSubject> selectByNameAndSeriesIndex(String name, Integer seriesIndex);

    int insert(TGzSubject gzSubject);

    /**
     * 查看我已结束的课程
     * @param id
     * @return
     */
    List<MyLearningSubjectVO> findEndingSubject(Long id);

    /**
     * 查询课程
     * @param subjectId
     * @return
     */
    TGzSubject findSubjectById(Long subjectId);

    /**
     * 查找所有正在售卖的课程
     * @return
     */
    List<TGzSubject> selectAll();

    List<TGzSubject> selectInPrimaryKeys(List<Long> subjectIds);

	TGzSubject selectBySeriesIndex(Integer integer);
}
