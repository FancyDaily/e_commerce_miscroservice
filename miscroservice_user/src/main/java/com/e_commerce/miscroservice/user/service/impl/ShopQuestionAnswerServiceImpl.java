package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionType;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.dao.ShopQuestionAnswerDao;
import com.e_commerce.miscroservice.user.entity.ShopQuestionSearch;
import com.e_commerce.miscroservice.user.service.shop.ShopQuestionAnswerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
@Service
public class ShopQuestionAnswerServiceImpl implements ShopQuestionAnswerService {
    private Log logger = Log.getInstance(ShopQuestionAnswerServiceImpl.class);

    @Resource
    private ShopQuestionAnswerDao shopQuestionAnswerDao;
    /**
     * 问题类型列表
     * @return
     */
    @Override
    public Response typeList() {
        logger.info("问题类型列表");
        List<ShopQuestionType> list = shopQuestionAnswerDao.typeList();
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
        List<ShopQuestionAnswer> list = shopQuestionAnswerDao.hotQuestion();
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
        List<ShopQuestionAnswer> list = shopQuestionAnswerDao.typeQuestion(type,pageNum);
        return Response.success(list);
    }
    /**
     * 问题详情
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response questionDetail(Long id,Long userId) {
        logger.info("问题详情 id={}",id);
        if (id==null){
            logger.info("问题类型 参数为空id={}",id);
            return Response.errorMsg("参数为空");
        }
        ShopQuestionAnswer answer = shopQuestionAnswerDao.questionDetail(id);
        ShopQuestionSearch shopQuestionSearch = null;
        if (userId!=null){
            shopQuestionSearch = shopQuestionAnswerDao.findOneShopQuestionByQId(id,userId);
        }
        if (shopQuestionSearch==null&&userId!=null){
            shopQuestionAnswerDao.upQuestionSearchTime(id);
            shopQuestionAnswerDao.insertShopQuestionSearch(id,userId);
        }

        return Response.success(answer);
    }
    /**
     * 问题 是否有用
     * @param id
     * @param useful
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response questionUseful(Long id, Integer useful) {
        logger.info("问题 是否有用 id={}，useful={}",id,useful);
        if (id==null||useful==null||useful>1||useful<0){
            logger.info("问题 是否有用 参数为空");
            return Response.errorMsg("参数为空");
        }
        shopQuestionAnswerDao.upQuestionUseful(id,useful);
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
        List<ShopQuestionAnswer> list  = shopQuestionAnswerDao.questionSearch(question,pageNum);
        return Response.success(list);
    }

}
