package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZEvaluateDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzEvaluate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 09:53
 */
@Component
public class GZEvaluateDaoImpl implements GZEvaluateDao {
    

    @Override
    public int insert(TGzEvaluate gzEvaluate) {
        return MybatisPlus.getInstance().save(gzEvaluate);
    }

    @Override
    public List<TGzEvaluate> selectByUserIdAndLessonId(Long userId, Long lessonId) {
        return MybatisPlus.getInstance().finAll(new TGzEvaluate(), new MybatisPlusBuild(TGzEvaluate.class)
        .eq(TGzEvaluate::getUserId, userId)
        .eq(TGzEvaluate::getLessonId, lessonId)
        .eq(TGzEvaluate::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzEvaluate> selectByLessonId(Long lessonId) {
        return MybatisPlus.getInstance().finAll(new TGzEvaluate(), new MybatisPlusBuild(TGzEvaluate.class)
        .eq(TGzEvaluate::getLessonId, lessonId)
        .eq(TGzEvaluate::getIsValid, AppConstant.IS_VALID_YES));
    }
}
