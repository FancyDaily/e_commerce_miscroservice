package com.e_commerce.miscroservice.xiaoshi_proj.product.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.product.dao.ProductDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 马晓晨
 * @date 2019/3/4
 */
@Repository
public class ProductDaoImpl implements ProductDao {

	@Override
	public int insert(TService service) {
		return MybatisPlus.getInstance().save(service);
	}

	@Override
	public TService selectByPrimaryKey(Long id) {
		return MybatisPlus.getInstance().findOne(new TService(), new MybatisPlusBuild(TService.class)
				.eq(TService::getId, id));
	}

	@Override
	public int updateByPrimaryKeySelective(TService lowerFrameService) {
		return MybatisPlus.getInstance().update(lowerFrameService, new MybatisPlusBuild(TService.class)
				.eq(TService::getId, lowerFrameService.getId()));
	}

	@Override
	public TService selectUserNewOneRecord(Long userId, Integer type) {
		return MybatisPlus.getInstance().findOne(new TService(), new MybatisPlusBuild(TService.class)
				.eq(TService::getUserId, userId).eq(TService::getType, type).eq(TService::getIsValid, '1')
				.eq(TService::getStatus, ProductEnum.STATUS_UPPER_FRAME.getValue())
				.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TService::getCreateTime)));
	}

	@Override
	public List<TService> selectListByIds(List<Long> productIds) {
		return MybatisPlus.getInstance().finAll(new TService(), new MybatisPlusBuild(TService.class).
				in(TService::getId, productIds));
	}

	@Override
	public List<TServiceDescribe> getListProductDesc(List<Long> productIds) {
		return MybatisPlus.getInstance().finAll(new TServiceDescribe(), new MybatisPlusBuild(TServiceDescribe.class)
				.in(TServiceDescribe::getServiceId, productIds));
	}

	@Override
	public List<TServiceDescribe> getProductDesc(Long serviceId) {
		return MybatisPlus.getInstance().finAll(new TServiceDescribe(), new MybatisPlusBuild(TServiceDescribe.class)
				.eq(TServiceDescribe::getServiceId, serviceId));
	}

	@Override
	public List<TService> getListProductByUserId(Long userId, Integer type, String keyName) {
		MybatisPlusBuild sqlWhere = new MybatisPlusBuild(TService.class);
		if (StringUtil.isNotEmpty(keyName)) {
			sqlWhere.like(TService::getServiceName,"%" + keyName + "%");
		}
		List<TService> listServie = MybatisPlus.getInstance().finAll(new TService(), sqlWhere
				.eq(TService::getUserId, userId).eq(TService::getType, type)
				.neq(TService::getStatus, ProductEnum.STATUS_DELETE.getValue()).orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TService::getCreateTime)));
		return listServie;
	}

    @Override
    public List<TService> selectByCompanyAccountInStatusBetween(Long userId, Integer[] companyPublishedStatusArray, Long begin, Long end) {
        return MybatisPlus.getInstance().finAll(new TService(),new MybatisPlusBuild(TService.class)
		.eq(TService::getUserId,userId)
		.eq(TService::getSource, AppConstant.SERV_SOURCE_COMPANY)
				.in(TService::getStatus,companyPublishedStatusArray)
				.between(TService::getCreateTime,begin,end)
				.eq(TService::getIsValid,AppConstant.IS_VALID_YES));
    }

	@Override
	public List<TService> selectByCompanyAccountInStatus(Long userId, Integer[] companyPublishedStatusArray) {
		return MybatisPlus.getInstance().finAll(new TService(),new MybatisPlusBuild(TService.class)
				.eq(TService::getUserId,userId)
				.eq(TService::getSource, AppConstant.SERV_SOURCE_COMPANY)
				.in(TService::getStatus,companyPublishedStatusArray)
				.eq(TService::getIsValid,AppConstant.IS_VALID_YES));
	}


    /**
	 * 查看一个人的所有服务订单
	 * @param userId
	 * @return
	 */
	public List<TService> selectUserServ(Long userId){
		return MybatisPlus.getInstance().finAll(new TService() , new MybatisPlusBuild(TService.class)
				.eq(TService::getCreateUser , userId)
				.eq(TService::getIsValid , AppConstant.IS_VALID_YES)
				.eq(TService::getType , ProductEnum.TYPE_SERVICE.getValue()));
	}

	/**
	 * 批量更新订单
	 * @param serviceList
	 * @param serviceIdList
	 * @return
	 */
	public long updateServiceByList(List<TService> serviceList, List<Long> serviceIdList) {
		long count = MybatisPlus.getInstance().update(serviceList,
				new MybatisPlusBuild(TService.class)
						.in(TService::getId, serviceIdList));
		return count;
	}

    @Override
    public List<TService> selectByUserId(Long userId) {
        return MybatisPlus.getInstance().finAll(new TService(),new MybatisPlusBuild(TService.class)
		.eq(TService::getCreateUser,userId)
		.eq(TService::getIsValid,AppConstant.IS_VALID_YES));
    }

	@Override
	public TServiceDescribe getProductDescTop(Long serviceId) {
		return MybatisPlus.getInstance().findOne(new TServiceDescribe(), new MybatisPlusBuild(TServiceDescribe.class)
				.eq(TServiceDescribe::getServiceId, serviceId).eq(TServiceDescribe::getIsCover,"1") );
	}
}
