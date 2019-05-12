package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZSubjectDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GZSubjectDaoImpl implements GZSubjectDao {

    @Override
    public List<TGzSubject> selectByAvailableStatus(Integer availableStatus) {
        return MybatisOperaterUtil.getInstance().finAll(new TGzSubject(), new MybatisSqlWhereBuild(TGzSubject.class)
        .eq(TGzSubject::getAvaliableStatus, availableStatus)
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzSubject selectByPrimaryKey(Long subjectId) {
        MybatisOperaterUtil instance = MybatisOperaterUtil.getInstance();
        TGzSubject one = instance.findOne(new TGzSubject(), new MybatisSqlWhereBuild(TGzSubject.class)
                .eq(TGzSubject::getId, subjectId)
                .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
        return one;
    }

    @Override
    public int updateByPrimaryKey(TGzSubject subject) {
        return MybatisOperaterUtil.getInstance().update(subject, new MybatisSqlWhereBuild(TGzSubject.class)
        .eq(TGzSubject::getId, subject.getId())
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
    }
}
