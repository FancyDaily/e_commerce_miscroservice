package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.user.entity.YjjQuestionSearch;

import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
public interface StoreQuestionAnswerDao {
    /**
     * 问题类型列表
     * @return
     */
    List<YjjQuestionType> typeList();

    /**
     * 热门问题
     * @return
     */
    List<YjjQuestionAnswer> hotQuestion();

    /**
     * 问题类别
     * @param type 类型
     * @param pageNum
     * @return
     */
    List<YjjQuestionAnswer> typeQuestion(Integer type, Integer pageNum);

    /**
     * 问题详情
     * @param id
     * @return
     */
    YjjQuestionAnswer questionDetail(Long id);

    /**
     * 更新问题查询次数
     * @param id
     */
    void upQuestionSearchTime(Long id);

    /**
     * 问题 是否有用
     * @param id
     * @param useful
     * @return
     */
    void upQuestionUseful(Long id, Integer useful);

    /**
     * 问题查询
     * @param question
     * @param pageNum
     * @return
     */
    List<YjjQuestionAnswer> questionSearch(String question, Integer pageNum);


    /**
     * 根据questionid查询 浏览
     * @param id
     * @return
     */
    YjjQuestionSearch findOneShopQuestionByQId(Long id,Integer userId);

    /**
     * 添加浏览
     * @param id
     */
    void insertShopQuestionSearch(Long id,Integer userId);
}
