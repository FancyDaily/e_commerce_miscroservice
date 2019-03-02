package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreQuestionAnswerMapper {


    /**
     * 更新问题查询次数
     * @param id
     */
    void upQuestionSearchTime(@Param("id") Long id);
    /**
     * 问题 是否有用
     * @param id
     * @param useful
     * @return
     */
    void upQuestionUseful(@Param("id") Long id, @Param("useful") Integer useful);

    /**
     * 问题查询
     * @param question
     * @return
     */
    List<YjjQuestionAnswer> questionSearch(@Param("question") String question);

    /**
     * 问题类别查询
     * @param type
     * @return
     */
    List<YjjQuestionAnswer> typeQuestion(@Param("type") Integer type);
}
