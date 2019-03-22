package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.ProductDao;
import com.e_commerce.miscroservice.product.mapper.ProductMapper;
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
	public List<TService> getListProductByUserId(Long userId, Integer type) {
		List<TService> listServie = MybatisOperaterUtil.getInstance().finAll(new TService(), new MybatisSqlWhereBuild(TService.class)
				.eq(TService::getUserId, userId).eq(TService::getType, type)
				.neq(TService::getStatus, ProductEnum.STATUS_DELETE.getValue()).orderBy(MybatisSqlWhereBuild.OrderBuild.buildDesc(TService::getCreateTime)));
		return listServie;
	}

    @Override
    public List<TService> selectByCompanyAccountInStatusBetween(Long userId, Integer[] companyPublishedStatusArray, Long begin, Long end) {
        return MybatisOperaterUtil.getInstance().finAll(new TService(),new MybatisSqlWhereBuild(TService.class)
		.eq(TService::getUserId,userId)
		.eq(TService::getSource, AppConstant.SERV_SOURCE_COMPANY)
				.in(TService::getStatus,companyPublishedStatusArray)
				.between(TService::getCreateTime,begin,end)
				.eq(TService::getIsValid,AppConstant.IS_VALID_YES));
    }

	@Override
	public List<TService> selectByCompanyAccountInStatus(Long userId, Integer[] companyPublishedStatusArray) {
		return MybatisOperaterUtil.getInstance().finAll(new TService(),new MybatisSqlWhereBuild(TService.class)
				.eq(TService::getUserId,userId)
				.eq(TService::getSource, AppConstant.SERV_SOURCE_COMPANY)
				.in(TService::getStatus,companyPublishedStatusArray)
				.eq(TService::getIsValid,AppConstant.IS_VALID_YES));
	}
}
