package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZUserLessonDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserLesson;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 14:15
 */
@Component
public class GZUserLessonDaoImpl implements GZUserLessonDao {

    @Override
    public TGzUserLesson selectByUserIdAndLessonId(Long userId, Long lessonId) {
        return MybatisPlus.getInstance().findOne(new TGzUserLesson(), new MybatisPlusBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getUserId, userId)
                .eq(TGzUserLesson::getLessonId, lessonId)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TGzUserLesson... tGzUserLesson) {
        return MybatisPlus.getInstance().save(tGzUserLesson);
    }

    @Override
    public int update(TGzUserLesson tGzUserLesson) {
        return MybatisPlus.getInstance().update(tGzUserLesson, new MybatisPlusBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getId, tGzUserLesson.getId()));
    }

    @Override
    public List<TGzUserLesson> selectByUserIdAndSubjectId(Long userId, Long subjectId) {
        return MybatisPlus.getInstance().finAll(new TGzUserLesson(), new MybatisPlusBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getUserId, userId)
                .eq(TGzUserLesson::getSubjectId, subjectId)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzUserLesson> selectBySubjectIdInUserIds(Long subjectId, List<Long> userIds) {
        return MybatisPlus.getInstance().finAll(new TGzUserLesson(), new MybatisPlusBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getSubjectId, subjectId)
                .in(TGzUserLesson::getUserId, userIds)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzUserLesson> selectByLessonId(Long id) {
        return MybatisPlus.getInstance().finAll(new TGzUserLesson(), new MybatisPlusBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getLessonId, id)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int batchUpdate(List<TGzUserLesson> toUpdater, List<Long> toUpdaterIds) {
        int update = MybatisPlus.getInstance().update(toUpdater, new MybatisPlusBuild(TGzUserLesson.class)
                .in(TGzUserLesson::getId, toUpdaterIds)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
        return update;
    }

    @Override
    public int batchInsert(List<TGzUserLesson> toInserter) {
        return MybatisPlus.getInstance().save(toInserter);
    }

    @Override
    public void insertList(List<TGzUserLesson> list) {
    	if(!list.isEmpty()) {
        	MybatisPlus.getInstance().save(list.toArray());
		}
    }
    @Override
    public List<TGzUserLesson> selectBySubjectId(Long subjectId) {
        return MybatisPlus.getInstance().finAll(new TGzUserLesson(), new MybatisPlusBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getSubjectId, subjectId)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }
}
