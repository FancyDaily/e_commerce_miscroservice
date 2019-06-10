package com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZSubjectDao;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.mapper.GZSubjectMapper;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzSubject;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyLearningSubjectVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.e_commerce.miscroservice.commons.util.colligate.DateUtil.timeStamp2Date;

@Component
public class GZSubjectDaoImpl implements GZSubjectDao {

    @Resource
    private GZSubjectMapper gzSubjectMapper;

    @Override
    public List<TGzSubject> selectByAvailableStatus(Integer availableStatus) {
        return MybatisPlus.getInstance().finAll(new TGzSubject(), new MybatisPlusBuild(TGzSubject.class)
        .eq(TGzSubject::getAvaliableStatus, availableStatus)
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public TGzSubject selectByPrimaryKey(Long subjectId) {
        MybatisPlus instance = MybatisPlus.getInstance();
        TGzSubject one = instance.findOne(new TGzSubject(), new MybatisPlusBuild(TGzSubject.class)
                .eq(TGzSubject::getId, subjectId)
                .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
        return one;
    }

    @Override
    public int updateByPrimaryKey(TGzSubject subject) {
        return MybatisPlus.getInstance().update(subject, new MybatisPlusBuild(TGzSubject.class)
        .eq(TGzSubject::getId, subject.getId())
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public List<MyLearningSubjectVO> findMyLearningSubject(Integer id, long currentTimeMillis) {
        return gzSubjectMapper.findMyLearningSubject(id,currentTimeMillis);
//        return MybatisPlus.getInstance().finAll(new MyLearningSubjectVO(),new MybatisPlusBuild(TGzUserSubject.class)
//                .eq(TGzUserSubject::getUserId,id));
    }

    @Override
    public List<MyLearningSubjectVO> findEndingSubject(Integer id) {
        return gzSubjectMapper.findEndingSubject(id,System.currentTimeMillis());

//        return MybatisPlus.getInstance().finAll(new TGzUserSubject(),new MybatisPlusBuild(TGzUserSubject.class)
//                .eq(TGzUserSubject::getUserId,id).lt(TGzUserSubject::getExpireTime,System.currentTimeMillis()));
    }

    @Override
    public TGzSubject findSubjectById(Long subjectId) {
        return MybatisPlus.getInstance().findOne(new TGzSubject(),new MybatisPlusBuild(TGzSubject.class).eq(TGzSubject::getId,subjectId));
    }

    @Override
    public List<TGzSubject> selectAll() {
		String now = timeStamp2Date(System.currentTimeMillis(), "yyyyMMdd HHmm");
		String[] nowArray = now.split(" ");
		String ymd = nowArray[0];
		String hm = nowArray[1];
		return MybatisPlus.getInstance().finAll(new TGzSubject(), new MybatisPlusBuild(TGzSubject.class)
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES)
			.gte(TGzSubject::getAvailableDate, ymd)
			.gt(TGzSubject::getAvailableTime, hm)
        .orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TGzSubject::getAvailableDate)));
    }

    @Override
    public List<TGzSubject> selectInPrimaryKeys(List<Long> subjectIds) {
        return MybatisPlus.getInstance().finAll(new TGzSubject(), new MybatisPlusBuild(TGzSubject.class)
        .in(TGzSubject::getId, subjectIds)
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

	@Override
	public TGzSubject selectBySeriesIndex(Integer integer) {
		return MybatisPlus.getInstance().findOne(new TGzSubject(), new MybatisPlusBuild(TGzSubject.class)
		.eq(TGzSubject::getSeriesIndex, integer)
		.eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
    public List<TGzSubject> selectByNameAndSeriesIndex(String name, Integer seriesIndex) {
        return MybatisPlus.getInstance().finAll(new TGzSubject(), new MybatisPlusBuild(TGzSubject.class)
        .eq(TGzSubject::getName, name)
        .eq(TGzSubject::getSeriesIndex, seriesIndex)
        .eq(TGzSubject::getIsValid, AppConstant.IS_VALID_YES));
    }

    @Override
    public int insert(TGzSubject gzSubject) {
        return MybatisPlus.getInstance().save(gzSubject);
    }
}
