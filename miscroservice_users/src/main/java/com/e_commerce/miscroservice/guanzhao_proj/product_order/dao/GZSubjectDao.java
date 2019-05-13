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
     * @return
     */
    List<MyLearningSubjectVO> findMyLearningSubject(Integer id);

    List<TGzSubject> selectByNameAndSeriesIndex(String name, Integer seriesIndex);

    int insert(TGzSubject gzSubject);
}
