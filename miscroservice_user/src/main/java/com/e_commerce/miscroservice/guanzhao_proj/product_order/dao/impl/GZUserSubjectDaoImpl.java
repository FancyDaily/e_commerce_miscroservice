package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZUserSubjectDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserSubject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-09 15:04
 */
@Component
public class GZUserSubjectDaoImpl implements GZUserSubjectDao {
    @Override
    public TGzUserSubject selectByUserIdAndSubjectId(Long userId, Long subjectId) {
        return MybatisPlus.getInstance().findOne(new TGzUserSubject(), new MybatisPlusBuild(TGzUserSubject.class)
        .eq(TGzUserSubject::getUserId, userId)
        .eq(TGzUserSubject::getSubjectId, subjectId)
        .eq(TGzUserSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzUserSubject> selectBySubjectId(Long subjectId) {
        return MybatisPlus.getInstance().findAll(new TGzUserSubject(), new MybatisPlusBuild(TGzUserSubject.class)
        .eq(TGzUserSubject::getSubjectId, subjectId)
        .eq(TGzUserSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public void insert(TGzUserSubject tGzUserSubject) {
        MybatisPlus.getInstance().save(tGzUserSubject);
    }

    @Override
    public List<TGzUserSubject> selectByUserId(Integer id) {
        return MybatisPlus.getInstance().findAll(new TGzUserSubject(), new MybatisPlusBuild(TGzSubject.class)
        .eq(TGzUserSubject::getUserId, id)
        .eq(TGzUserSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int updateByPrimaryKey(TGzUserSubject tGzUserSubject) {
        return MybatisPlus.getInstance().update(tGzUserSubject, new MybatisPlusBuild(TGzUserSubject.class)
        .eq(TGzUserSubject::getId, tGzUserSubject.getId())
        .eq(TGzUserSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<TGzUserSubject> selectByPrimaryKey(Long id) {
        return MybatisPlus.getInstance().findAll(new TGzUserSubject(), new MybatisPlusBuild(TGzUserSubject.class)
        .eq(TGzUserSubject::getId, id)
        .eq(TGzUserSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int batchUpdate(List<TGzUserSubject> updaters, List<Long> userSubjectIds) {
        return MybatisPlus.getInstance().update(updaters, new MybatisPlusBuild(TGzUserSubject.class)
        .in(TGzUserSubject::getId, userSubjectIds)
        .eq(TGzUserSubject::getIsValid, AppConstant.IS_VALID_YES));
    }
}
