package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZLessonDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-08 20:58
 */
@Component
public class GZLessonDaoImpl implements GZLessonDao {

    @Override
    public List<TGzLesson> selectBySubjectId(Long subjectId) {
        return MybatisPlus.getInstance().finAll(new TGzLesson(), new MybatisPlusBuild(TGzLesson.class)
                .eq(TGzLesson::getSubjectId, subjectId)
                .eq(TGzLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzLesson selectByPrimaryKey(Long lessonId) {
        return MybatisPlus.getInstance().findOne(new TGzLesson(), new MybatisPlusBuild(TGzLesson.class)
        .eq(TGzLesson::getId, lessonId)
        .eq(TGzLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzLesson selectBySubjectIdAndName(Long subjectId, String fileName) {
        return MybatisPlus.getInstance().findOne(new TGzLesson(), new MybatisPlusBuild(TGzLesson.class)
        .eq(TGzLesson::getSubjectId, subjectId)
        .eq(TGzLesson::getName, fileName));
    }

    @Override
    public int updateByPrimaryKey(TGzLesson tGzLesson) {
        return MybatisPlus.getInstance().update(tGzLesson, new MybatisPlusBuild(TGzLesson.class)
        .eq(TGzLesson::getId, tGzLesson.getId())
        .eq(TGzLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(List<TGzLesson> toInsert) {
        return MybatisPlus.getInstance().save(toInsert);
    }
}
