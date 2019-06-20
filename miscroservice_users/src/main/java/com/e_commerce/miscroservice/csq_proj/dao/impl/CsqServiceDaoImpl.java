package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO
 * @ClassName CsqServiceDaoImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-16 12:18
 * @Version 1.0
 */
@Repository
public class CsqServiceDaoImpl implements CsqServiceDao {

	public MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES);
	}

	public MybatisPlusBuild IdWhereBuild(Long id) {
		return baseWhereBuild().eq(TCsqService::getId, id);
	}

	@Override
	public TCsqService findOne(Long serviceId) {

		return MybatisPlus.getInstance().findOne(new TCsqService(),new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getId,serviceId).eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
		);
	}

	@Override
	public List<TCsqService> findAll(List<Long> serviceIdList) {
		List<TCsqService> list = MybatisPlus.getInstance().finAll(new TCsqService(),new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getId,serviceIdList)
		);
		return list;
	}

	@Override
	public List<TCsqService> selectInIds(List<Long> csqServiceIds) {
		return MybatisPlus.getInstance().finAll(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getId, csqServiceIds)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int insert(TCsqService... service) {
		return MybatisPlus.getInstance().save(service);
	}

	@Override
	public List<TCsqService> selectAll() {
		return MybatisPlus.getInstance().finAll(new TCsqService(), baseWhereBuild()
			.eq(TCsqService::getFundStatus, CsqFundEnum.STATUS_PUBLIC.getVal())	//已公开的基金对应的项目
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),	//按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),	//按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount)	//按还需筹多少金额正序
			));
	}

	@Override
	public List<TCsqService> selectMine(Long userId) {
		return MybatisPlus.getInstance().finAll(new TCsqService(), baseWhereBuild()
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),	//按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),	//按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount)	//按还需筹多少金额正序
			));
	}

	@Override
	public TCsqService selectByPrimaryKey(Long serviceId) {
		return MybatisPlus.getInstance().findOne(new TCsqService(), baseWhereBuild()
			.eq(TCsqService::getId, serviceId));
	}

	@Override
	public int update(TCsqService csqService) {
		return MybatisPlus.getInstance().update(csqService, IdWhereBuild(csqService.getId()));
	}

}
