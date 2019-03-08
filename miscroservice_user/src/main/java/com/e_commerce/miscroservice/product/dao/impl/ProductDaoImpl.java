package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.mapper.ProductMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
				.orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TService::getCreateTime)));
	}

	@Override
	public List<TService> selectListByIds(List<Long> productIds) {
		return MybatisOperaterUtil.getInstance().finAll(new TService(), new MybatisSqlWhereBuild(TService.class).
				in(TService::getId, productIds));
	}

	@Override
	public List<TServiceDescribe> getListProductDesc(List<Long> productIds) {
		return MybatisOperaterUtil.getInstance().finAll(new TServiceDescribe(), new MybatisSqlWhereBuild(TServiceDescribe.class)
				.in(TServiceDescribe::getServiceId, productIds));
	}

	@Override
	public List<TServiceDescribe> getProductDesc(Long serviceId) {
		return MybatisOperaterUtil.getInstance().finAll(new TServiceDescribe(), new MybatisSqlWhereBuild(TServiceDescribe.class)
				.eq(TServiceDescribe::getServiceId, serviceId));
	}

	@Override
	public Page<TService> getListProductByUserId(Long userId, Integer pageNum, Integer pageSize, Integer type) {
		Page<TService> page = PageHelper.startPage(pageNum, pageSize);
		MybatisOperaterUtil.getInstance().finAll(new TService(), new MybatisSqlWhereBuild(TService.class)
				.eq(TService::getUserId, userId).eq(TService::getType, type)
				.neq(TService::getStatus, ProductEnum.STATUS_DELETE.getValue()));
		return page;
	}
}
