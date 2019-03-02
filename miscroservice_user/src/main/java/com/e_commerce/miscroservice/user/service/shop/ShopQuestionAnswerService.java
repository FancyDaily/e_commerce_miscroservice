package com.e_commerce.miscroservice.user.service.shop;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
public interface ShopQuestionAnswerService {
    /**
     * 问题类型列表
     * @return
     */
    Response typeList();

    /**
     * 热门问题
     * @return
     */
    Response hotQuestion();

    /**
     * 问题类别
     * @param type 类型
     * @param pageNum
     * @return
     */
    Response typeQuestion(Integer type, Integer pageNum);

    /**
     * 问题详情
     * @param id
     * @return
     */
    Response questionDetail(Long id,Long userId);
    /**
     * 问题 有用
     * @param id
     * @param useful
     * @return
     */
    Response questionUseful(Long id, Integer useful);


    /**
     * 问题 查询
     * @param question 查询的问题
     * @param pageNum
     * @return
     */
    Response questionSearch(String question, Integer pageNum);
}
