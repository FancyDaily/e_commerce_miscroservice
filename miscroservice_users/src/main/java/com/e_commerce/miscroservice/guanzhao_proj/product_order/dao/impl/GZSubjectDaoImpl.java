package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZSubjectDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.mapper.GZSubjectMapper;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class GZSubjectDaoImpl implements GZSubjectDao {

    @Resource
    private GZSubjectMapper gzSubjectMapper;

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

    @Override
    public List<MyLearningSubjectVO> findMyLearningSubject(Integer id) {
        return gzSubjectMapper.findMyLearningSubject(id);
//        return MybatisOperaterUtil.getInstance().finAll(new MyLearningSubjectVO(),new MybatisSqlWhereBuild(TGzUserSubject.class)
//                .eq(TGzUserSubject::getUserId,id));
    }

    @Override
    public List<MyLearningSubjectVO> findEndingSubject(Integer id) {
        return gzSubjectMapper.findEndingSubject(id,System.currentTimeMillis());

//        return MybatisOperaterUtil.getInstance().finAll(new TGzUserSubject(),new MybatisSqlWhereBuild(TGzUserSubject.class)
//                .eq(TGzUserSubject::getUserId,id).lt(TGzUserSubject::getExpireTime,System.currentTimeMillis()));
    }
}
