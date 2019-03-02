package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.StoreQuestionAnswerDao;
import com.e_commerce.miscroservice.user.entity.YjjQuestionSearch;
import com.e_commerce.miscroservice.user.mapper.StoreQuestionAnswerMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/20 17:20
 * @Copyright 玖远网络
 */
@Repository
public class StoreQuestionAnswerDaoImpl implements StoreQuestionAnswerDao {
    private Log logger = Log.getInstance(StoreQuestionAnswerDaoImpl.class);
    @Resource
    private StoreQuestionAnswerMapper storeQuestionAnswerMapper;
    /**
     * 问题类型列表
     *
     * @return
     */
    @Override
    public List<YjjQuestionType> typeList() {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionType.class);
        mybatisSqlWhereBuild.eq(YjjQuestionType::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,YjjQuestionType::getSort);
        List<YjjQuestionType> typeList = MybatisOperaterUtil.getInstance().finAll(new YjjQuestionType(),mybatisSqlWhereBuild);
        return typeList;
    }
    /**
     * 热门问题
     * @return
     */
    @Override
    public List<YjjQuestionAnswer> hotQuestion() {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionAnswer.class);
        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,YjjQuestionAnswer::getSearchTime);
        mybatisSqlWhereBuild.page(1,5);
        List<YjjQuestionAnswer> list = MybatisOperaterUtil.getInstance().finAll(new YjjQuestionAnswer(),mybatisSqlWhereBuild);
        return list;
    }

    /**
     * 问题类别
     * @param type 类型
     * @param pageNum
     * @return
     */
    @Override
    public List<YjjQuestionAnswer> typeQuestion(Integer type, Integer pageNum) {
//        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionAnswer.class);
//        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getDelState, StateEnum.NORMAL);
//        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getStatus, StateEnum.NORMAL);
//        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getQuestionTypeId,type);
////        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,YjjQuestionAnswer::getSearchTime);
//        mybatisSqlWhereBuild.page(pageNum,10);
//        List<YjjQuestionAnswer> list = MybatisOperaterUtil.getInstance().finAll(new YjjQuestionAnswer(),mybatisSqlWhereBuild);
        PageHelper.startPage(pageNum, 10);
        List<YjjQuestionAnswer> list = storeQuestionAnswerMapper.typeQuestion(type);
        return list;
    }

    /**
     * 问题详情
     * @param id
     * @return
     */
    @Override
    public YjjQuestionAnswer questionDetail(Long id) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionAnswer.class);
        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(YjjQuestionAnswer::getId,id);
        YjjQuestionAnswer answer = MybatisOperaterUtil.getInstance().findOne(new YjjQuestionAnswer(),mybatisSqlWhereBuild);
        return answer;
    }
    /**
     * 更新问题查询次数
     * @param id
     */
    @Override
    public void upQuestionSearchTime(Long id) {
        logger.info("id={}问题 用户查询+1");
        storeQuestionAnswerMapper.upQuestionSearchTime(id);
    }
    /**
     * 问题 是否有用
     * @param id
     * @param useful
     * @return
     */
    @Override
    public void upQuestionUseful(Long id, Integer useful) {
       storeQuestionAnswerMapper.upQuestionUseful(id,useful);
    }

    /**
     * 问题查询
     * @param question
     * @param pageNum
     * @return
     */
    @Override
    public List<YjjQuestionAnswer> questionSearch(String question, Integer pageNum) {
        PageHelper.startPage(pageNum,20);
        List<YjjQuestionAnswer> list = storeQuestionAnswerMapper.questionSearch(question);
        return list;
    }

    /**
     * 根据questionid查询 浏览
     *
     * @param id
     * @return
     */
    @Override
    public YjjQuestionSearch findOneShopQuestionByQId(Long id,Integer userId) {
        YjjQuestionSearch yjjQuestionSearch = MybatisOperaterUtil.getInstance().findOne(
                new YjjQuestionSearch(),
                new MybatisSqlWhereBuild(YjjQuestionSearch.class)
                        .eq(YjjQuestionSearch::getQuestionId,id)
                        .eq(YjjQuestionSearch::getUserId,userId)
                        .eq(YjjQuestionSearch::getDelStatus,StateEnum.NORMAL)
        );
        return yjjQuestionSearch;
    }

    /**
     * 添加浏览
     *
     * @param id
     */
    @Override
    public void insertShopQuestionSearch(Long id,Integer userId) {
        YjjQuestionSearch yjjQuestionSearch = new YjjQuestionSearch();
        yjjQuestionSearch.setQuestionId(id);
        yjjQuestionSearch.setUserId(Long.valueOf(userId));
        MybatisOperaterUtil.getInstance().save(yjjQuestionSearch);
    }
}
