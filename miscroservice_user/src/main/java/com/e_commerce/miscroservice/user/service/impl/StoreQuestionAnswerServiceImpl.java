package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.dao.StoreQuestionAnswerDao;
import com.e_commerce.miscroservice.user.entity.YjjQuestionSearch;
import com.e_commerce.miscroservice.user.service.store.StoreQuestionAnswerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
@Service
public class StoreQuestionAnswerServiceImpl implements StoreQuestionAnswerService {
    private Log logger = Log.getInstance(StoreQuestionAnswerServiceImpl.class);

    @Resource
    private StoreQuestionAnswerDao storeQuestionAnswerDao;
    /**
     * 问题类型列表
     * @return
     */
    @Override
    public Response typeList() {
        logger.info("问题类型列表");
        List<YjjQuestionType> list = storeQuestionAnswerDao.typeList();
        return Response.success(list);
    }
    /**
     * 热门问题
     * @return
     * @param
     */
    @Override
    public Response hotQuestion() {
        logger.info("热门问题");
        List<YjjQuestionAnswer> list = storeQuestionAnswerDao.hotQuestion();
        return Response.success(list);
    }
    /**
     * 问题类别
     * @param type 类型
     * @param pageNum
     * @return
     */
    @Override
    public Response typeQuestion(Integer type, Integer pageNum ) {
        logger.info("问题类别 type={}",type);
        if (type==null){
            logger.info("问题类型 参数为空type={}",type);
            return Response.errorMsg("参数为空");
        }
        List<YjjQuestionAnswer> list = storeQuestionAnswerDao.typeQuestion(type,pageNum);
        SimplePage<YjjQuestionAnswer> simplePage = new SimplePage<>(list);
        return Response.success(simplePage);
    }
    /**
     * 问题详情
     * @param id
     * @return
     */
    @Override
    public Response questionDetail(Long id,Integer userId) {
        logger.info("问题详情 id={},userId={}",id,userId);
        if (id==null){
            logger.info("问题类型 参数为空id={}",id);
            return Response.errorMsg("参数为空");
        }
        YjjQuestionAnswer answer = storeQuestionAnswerDao.questionDetail(id);

        YjjQuestionSearch yjjQuestionSearch = null;
        if (userId!=null){
            yjjQuestionSearch = storeQuestionAnswerDao.findOneShopQuestionByQId(id,userId);
        }
        if (yjjQuestionSearch==null&&userId!=null){
            storeQuestionAnswerDao.upQuestionSearchTime(id);
            storeQuestionAnswerDao.insertShopQuestionSearch(id,userId);
        }
        return Response.success(answer);
    }
    /**
     * 问题 是否有用
     * @param id
     * @param useful
     * @return
     */
    @Override
    public Response questionUseful(Long id, Integer useful) {
        logger.info("问题 是否有用 id={}，useful={}",id,useful);
        if (id==null||useful==null||useful>1||useful<0){
            logger.info("问题 是否有用 参数为空");
            return Response.errorMsg("参数为空");
        }
        storeQuestionAnswerDao.upQuestionUseful(id,useful);
        return Response.success();
    }

    /**
     * 问题 查询
     * @param question 查询的问题
     * @param pageNum
     * @return
     */
    @Override
    public Response questionSearch(String question, Integer pageNum) {
        logger.info("问题 查询 question={}",question);
        if (question==null){
            logger.info("问题 是否有用 参数为空");
            return Response.errorMsg("参数为空");
        }
        List<YjjQuestionAnswer> list  = storeQuestionAnswerDao.questionSearch(question,pageNum);
        SimplePage<YjjQuestionAnswer> simplePage = new SimplePage<>(list);
        return Response.success(simplePage);
    }

}
