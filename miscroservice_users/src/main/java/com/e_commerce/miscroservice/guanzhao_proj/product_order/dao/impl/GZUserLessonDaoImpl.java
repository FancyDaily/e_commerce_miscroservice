package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZUserLessonDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzLesson;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserLesson;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 14:15
 */
@Component
public class GZUserLessonDaoImpl implements GZUserLessonDao {

    @Override
    public TGzUserLesson selectByUserIdAndLessonId(Long userId, Long lessonId) {
        return MybatisOperaterUtil.getInstance().findOne(new TGzUserLesson(), new MybatisSqlWhereBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getUserId, userId)
                .eq(TGzUserLesson::getLessonId, lessonId)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TGzUserLesson... tGzUserLesson) {
        return MybatisOperaterUtil.getInstance().save(tGzUserLesson);
    }

    @Override
    public int update(TGzUserLesson tGzUserLesson) {
        return MybatisOperaterUtil.getInstance().update(tGzUserLesson, new MybatisSqlWhereBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getId, tGzUserLesson.getId()));
    }

    @Override
    public List<TGzUserLesson> selectByUserIdAndSubjectId(Long userId, Long subjectId) {
        return MybatisOperaterUtil.getInstance().finAll(new TGzUserLesson(), new MybatisSqlWhereBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getUserId, userId)
                .eq(TGzUserLesson::getSubjectId, subjectId)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzUserLesson> selectBySubjectIdInUserIds(Long subjectId, List<Long> userIds) {
        return MybatisOperaterUtil.getInstance().finAll(new TGzUserLesson(), new MybatisSqlWhereBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getSubjectId, subjectId)
                .in(TGzUserLesson::getUserId, userIds)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzUserLesson> selectByLessonId(Long id) {
        return MybatisOperaterUtil.getInstance().finAll(new TGzUserLesson(), new MybatisSqlWhereBuild(TGzUserLesson.class)
                .eq(TGzUserLesson::getLessonId, id)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
    }

    public static void main(String[] args) {
        List<TGzUserLesson> toUpdater = new ArrayList<>();
        List<Long> toUpdaterIds = new ArrayList<>();
        toUpdater.add(TGzUserLesson.builder().id(555l).userId(42l).subjectId(1l).lessonId(3l).sign("DD1016D687D5960A8F279198A94D0CC5").status(1).videoCompletion(0).videoCompletionStatus(0)
        .createUser(42l).build());
        toUpdaterIds.add(555l);
//        batchUpdate(toUpdater,toUpdaterIds);
    }

    @Override
    public int batchUpdate(List<TGzUserLesson> toUpdater, List<Long> toUpdaterIds) {
        int update = MybatisOperaterUtil.getInstance().update(toUpdater, new MybatisSqlWhereBuild(TGzUserLesson.class)
                .in(TGzUserLesson::getId, 555)
                .eq(TGzUserLesson::getIsValid, AppConstant.IS_VALID_YES));
        return update;
    }

    @Override
    public int batchInsert(List<TGzUserLesson> toInserter) {
        return MybatisOperaterUtil.getInstance().save(toInserter);
    }

    @Override
    public void insertList(List<TGzLesson> list) {
        MybatisOperaterUtil.getInstance().save(list.toArray());
    }
}
