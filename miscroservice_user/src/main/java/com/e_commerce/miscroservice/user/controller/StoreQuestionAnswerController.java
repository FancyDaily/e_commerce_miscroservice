package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.service.store.StoreQuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * QA,FAQ
 * @author hyf
 * @version V1.0
 * @date 2018/11/19 11:57
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/qa/store")
public class StoreQuestionAnswerController
{

    @Autowired
    private StoreQuestionAnswerService storeQuestionAnswerService;

    /**
     * 热门问题
     * @return
     */
    @RequestMapping("/hot/question")
    public Response hotQuestion(){
        return storeQuestionAnswerService.hotQuestion();
    }
    /**
     * 问题类别列表
     * @return
     */
    @RequestMapping("/type/list")
    public Response typeList(){
        return storeQuestionAnswerService.typeList();
    }

    /**
     * 问题类别
     * @param type 问题类别
     * @param pageNumber 页码
     * @return
     */
    @RequestMapping("/type/question")
    public Response typeQuestion(Integer type,Integer pageNumber){
        return storeQuestionAnswerService.typeQuestion(type,pageNumber);
    }


    /**
     * 问题详情
     * @param id
     * @return
     */
    @RequestMapping({"/question/detail","/question/detail/auth"})
    public Response questionDetail(Long id){
        Integer userId = IdUtil.getId();
        return storeQuestionAnswerService.questionDetail(id,userId);
    }

    /**
     * 问题 是否有用
     * @param id
     * @param useful 0 有用 1 无用
     * @return
     */
    @RequestMapping("/question/useful")
    public Response questionUseful(Long id,Integer useful){
        return storeQuestionAnswerService.questionUseful(id,useful);
    }
    /**
     * 问题 查询
     * @param question 查询的问题
     * @param pageNumber 页码
     * @return
     */
    @RequestMapping("/question/search")
    public Response questionSearch(String question,Integer pageNumber){
        return storeQuestionAnswerService.questionSearch(question,pageNumber);
    }
}
