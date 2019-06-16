package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqCollectionDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserCollection;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO
 * @ClassName CsqCollectionDaoImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-16 12:51
 * @Version 1.0
 */
@Repository
public class CsqCollectionDaoImpl implements CsqCollectionDao {
	@Override
	public TCsqUserCollection findOne(Long serviceId, Long userId) {
		TCsqUserCollection tCsqUserCollection = MybatisPlus.getInstance().findOne(new TCsqUserCollection(),new MybatisPlusBuild(TCsqUserCollection.class)
			.eq(TCsqUserCollection::getServiceId,serviceId)
			.eq(TCsqUserCollection::getUserId,userId)
		);
		return tCsqUserCollection;
	}

	@Override
	public Integer insert(TCsqUserCollection in) {
		Integer i  = MybatisPlus.getInstance().save(in);
		return i;
	}

	@Override
	public Integer update(TCsqUserCollection in) {
		Integer i  = MybatisPlus.getInstance().update(in,new MybatisPlusBuild(TCsqUserCollection.class)
		.eq(TCsqUserCollection::getId,in.getId())
		);

		return i;
	}

	@Override
	public List<TCsqUserCollection> findAll(Long userId) {
		List<TCsqUserCollection> list = MybatisPlus.getInstance().finAll(new TCsqUserCollection(),new MybatisPlusBuild(TCsqUserCollection.class)
			.eq(TCsqUserCollection::getUserId,userId)
			.eq(TCsqUserCollection::getIsValid, AppConstant.IS_VALID_YES)
		);
		return list;
	}


}
