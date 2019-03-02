package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionType;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.ShopQuestionAnswerDao;
import com.e_commerce.miscroservice.user.entity.ShopQuestionSearch;
import com.e_commerce.miscroservice.user.mapper.ShopQuestionAnswerMapper;
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
public class ShopQuestionAnswerDaoImpl implements ShopQuestionAnswerDao {
    private Log logger = Log.getInstance(ShopQuestionAnswerDaoImpl.class);
    @Resource
    private ShopQuestionAnswerMapper shopQuestionAnswerMapper;
    /**
     * 问题类型列表
     *
     * @return
     */
    @Override
    public List<ShopQuestionType> typeList() {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionType.class);
        mybatisSqlWhereBuild.eq(ShopQuestionType::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopQuestionType::getSort);
        List<ShopQuestionType> typeList = MybatisOperaterUtil.getInstance().finAll(new ShopQuestionType(),mybatisSqlWhereBuild);
        return typeList;
    }
    /**
     * 热门问题
     * @return
     */
    @Override
    public List<ShopQuestionAnswer> hotQuestion() {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionAnswer.class);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopQuestionAnswer::getSearchTime);
        mybatisSqlWhereBuild.page(1,5);
        List<ShopQuestionAnswer> list = MybatisOperaterUtil.getInstance().finAll(new ShopQuestionAnswer(),mybatisSqlWhereBuild);
        return list;
    }

    /**
     * 问题类别
     * @param type 类型
     * @param pageNum
     * @return
     */
    @Override
    public List<ShopQuestionAnswer> typeQuestion(Integer type, Integer pageNum) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionAnswer.class);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getQuestionTypeId,type);
        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopQuestionAnswer::getSort);
//        mybatisSqlWhereBuild.orderBy(MybatisSqlWhereBuild.ORDER.DESC,ShopQuestionAnswer::getSearchTime);
        mybatisSqlWhereBuild.page(pageNum,10);
        List<ShopQuestionAnswer> list = MybatisOperaterUtil.getInstance().finAll(new ShopQuestionAnswer(),mybatisSqlWhereBuild);
        return list;
    }

    /**
     * 问题详情
     * @param id
     * @return
     */
    @Override
    public ShopQuestionAnswer questionDetail(Long id) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionAnswer.class);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getDelStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getStatus, StateEnum.NORMAL);
        mybatisSqlWhereBuild.eq(ShopQuestionAnswer::getId,id);
        ShopQuestionAnswer answer = MybatisOperaterUtil.getInstance().findOne(new ShopQuestionAnswer(),mybatisSqlWhereBuild);
        return answer;
    }
    /**
     * 更新问题查询次数
     * @param id
     */
    @Override
    public void upQuestionSearchTime(Long id) {
        logger.info("id={}问题 用户查询+1");
        shopQuestionAnswerMapper.upQuestionSearchTime(id);
    }
    /**
     * 问题 是否有用
     * @param id
     * @param useful
     * @return
     */
    @Override
    public void upQuestionUseful(Long id, Integer useful) {
       shopQuestionAnswerMapper.upQuestionUseful(id,useful);
    }

    /**
     * 问题查询
     * @param question
     * @param pageNum
     * @return
     */
    @Override
    public List<ShopQuestionAnswer> questionSearch(String question, Integer pageNum) {
        PageHelper.startPage(pageNum,20);
        List<ShopQuestionAnswer> list = shopQuestionAnswerMapper.questionSearch(question);
        return list;
    }

    /**
     * 插入浏览记录
     *
     * @param id
     */
    @Override
    public void insertShopQuestionSearch(Long id,Long userId) {
        ShopQuestionSearch shopQuestionSearch = new ShopQuestionSearch();
        shopQuestionSearch.setQuestionId(id);
        shopQuestionSearch.setUserId(userId);
        MybatisOperaterUtil.getInstance().save(shopQuestionSearch);
    }

    /**
     * 查找浏览记录
     *
     * @param id
     * @return
     */
    @Override
    public ShopQuestionSearch findOneShopQuestionByQId(Long id,Long userId) {
        ShopQuestionSearch shopQuestionSearch = MybatisOperaterUtil.getInstance().findOne(new ShopQuestionSearch(),
                new MybatisSqlWhereBuild(ShopQuestionSearch.class).
                        eq(ShopQuestionSearch::getQuestionId,id).
                        eq(ShopQuestionSearch::getUserId,userId).eq(ShopQuestionSearch::getDelStatus,StateEnum.NORMAL)
        );
        return shopQuestionSearch;
    }
}
