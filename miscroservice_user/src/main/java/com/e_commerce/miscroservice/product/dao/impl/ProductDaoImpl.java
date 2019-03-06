package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author 马晓晨
 * @date 2019/3/4
 */
@Repository
public class ProductDaoImpl implements ProductDao {
	@Autowired
	ProductMapper productMapper;


	@Override
	public int insert(TService service) {
		return MybatisOperaterUtil.getInstance().save(service);
	}

	@Override
	public TService selectByPrimaryKey(Long id) {
		return MybatisOperaterUtil.getInstance().findOne(new TService(), new MybatisSqlWhereBuild(TService.class)
				.eq(TService::getId, id));
	}

	@Override
	public int updateByPrimaryKeySelective(TService lowerFrameService) {
		return MybatisOperaterUtil.getInstance().update(lowerFrameService, new MybatisSqlWhereBuild(TService.class)
				.eq(TService::getId, lowerFrameService.getId()));
	}

	@Override
	public TService selectUserNewOneRecord(Long userId, Integer type) {
		return MybatisOperaterUtil.getInstance().findOne(new TService(), new MybatisSqlWhereBuild(TService.class)
				.eq(TService::getUserId, userId).eq(TService::getType, type).eq(TService::getIsValid, '1')
				.eq(TService::getStatus, ProductEnum.STATUS_UPPER_FRAME.getValue())
				.orderBy(MybatisSqlWhereBuild.ORDER.DESC, TService::getCreateTime));
	}
}
