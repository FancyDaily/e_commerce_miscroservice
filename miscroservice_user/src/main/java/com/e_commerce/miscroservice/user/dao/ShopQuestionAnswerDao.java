package com.e_commerce.miscroservice.user.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionType;
import com.e_commerce.miscroservice.user.entity.ShopQuestionSearch;

import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
public interface ShopQuestionAnswerDao {
    /**
     * 问题类型列表
     * @return
     */
    List<ShopQuestionType> typeList();

    /**
     * 热门问题
     * @return
     */
    List<ShopQuestionAnswer> hotQuestion();

    /**
     * 问题类别
     * @param type 类型
     * @param pageNum
     * @return
     */
    List<ShopQuestionAnswer> typeQuestion(Integer type, Integer pageNum);

    /**
     * 问题详情
     * @param id
     * @return
     */
    ShopQuestionAnswer questionDetail(Long id);

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
    List<ShopQuestionAnswer> questionSearch(String question, Integer pageNum);

    /**
     * 插入浏览记录
     * @param id
     */
    void insertShopQuestionSearch(Long id,Long userId);

    /**
     * 查找浏览记录
     * @param id
     * @return
     */
    ShopQuestionSearch findOneShopQuestionByQId(Long id,Long userId);
}
