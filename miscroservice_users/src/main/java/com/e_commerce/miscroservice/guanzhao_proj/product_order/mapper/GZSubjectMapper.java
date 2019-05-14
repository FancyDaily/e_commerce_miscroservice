package com.e_commerce.miscroservice.guanzhao_proj.product_order.mapper;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GZSubjectMapper {
    /**
     * 我的正在学习
     * @param id
     * @param currentTimeMillis
     * @return
     */
    List<MyLearningSubjectVO> findMyLearningSubject(@Param("id") Integer id, @Param("currentTimeMillis") long currentTimeMillis);

    /**
     * 我的结束课程
     * @param id
     * @param currentTimeMillis
     * @return
     */
    List<MyLearningSubjectVO> findEndingSubject(@Param("id") Integer id, @Param("currentTimeMillis") long currentTimeMillis);
}
