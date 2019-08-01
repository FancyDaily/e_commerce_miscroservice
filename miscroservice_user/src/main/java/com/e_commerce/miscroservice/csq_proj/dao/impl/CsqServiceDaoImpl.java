package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.alipay.api.domain.AlipayFundTransDishonorQueryModel;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqServiceEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @ClassName CsqServiceDaoImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-16 12:18
 * @Version 1.0
 */
@Repository
public class CsqServiceDaoImpl implements CsqServiceDao {

	public MybatisPlusBuild IdWhereBuild(Long id) {
		return baseBuild().eq(TCsqService::getId, id);
	}

	@Override
	public TCsqService findOne(Long serviceId) {

		return MybatisPlus.getInstance().findOne(new TCsqService(),new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getId,serviceId).eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
		);
	}

	@Override
	public List<TCsqService> findAll(List<Long> serviceIdList) {
		List<TCsqService> list = MybatisPlus.getInstance().findAll(new TCsqService(),new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getId,serviceIdList)
		);
		return list;
	}

	@Override
	public List<TCsqService> selectInIds(List<Long> csqServiceIds) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getId, csqServiceIds)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public int insert(TCsqService... service) {
		return MybatisPlus.getInstance().save(service);
	}

	@Override
	public List<TCsqService> selectAll() {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild()
			.eq(TCsqService::getStatus, CsqServiceEnum.STATUS_INITIAL.getCode())
			.and()
			.groupBefore()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_SERIVE.getCode())
			.groupAfter()
			.or()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_FUND.getCode())	//若为基金唯一对应项目
			.eq(TCsqService::getFundStatus, CsqFundEnum.STATUS_PUBLIC.getVal())	//已公开的基金对应的项目
			.groupAfter()
			.groupAfter()
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),	//按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),	//按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount)	//按还需筹多少金额正序
			));
	}

	@Override
	public List<TCsqService> selectMine(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getUserId, userId)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),	//按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),	//按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount)	//按还需筹多少金额正序
			));
	}

	@Override
	public TCsqService selectByPrimaryKey(Long serviceId) {
		return MybatisPlus.getInstance().findOne(new TCsqService(), baseBuild()
			.eq(TCsqService::getId, serviceId));
	}

	@Override
	public int update(TCsqService csqService) {
		return MybatisPlus.getInstance().update(csqService, IdWhereBuild(csqService.getId()));
	}

	@Override
	public TCsqService selectByFundId(Long fundId) {
		return MybatisPlus.getInstance().findOne(new TCsqService(), baseBuild()
			.eq(TCsqService::getFundId, fundId));
	}

	@Override
	public int updateByFundId(TCsqService build) {
		return MybatisPlus.getInstance().update(build, baseBuild()
			.eq(TCsqService::getFundId, build.getFundId()));
	}

	@Override
	public List<TCsqService> selectByNameAndUserId(String name, Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild()
			.eq(TCsqService::getUserId, userId)
			.eq(TCsqService::getName, name)
		);
	}

	@Override
	public List<TCsqService> selectLikeByPubKeys(String a) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild()
			.and()
			.groupBefore()
			.like(TCsqService::getTypePubKeys, "%," + a + "%")
			.or()
			.like(TCsqService::getTypePubKeys, "%" + a + ",%")
			.groupAfter());
	}

	@Override
	public List<TCsqService> selectLikeByPubKeysAndUserIdNeq(String a, Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild()
			.neq(TCsqService::getUserId, userId)
			.and()
			.groupBefore()
			.like(TCsqService::getTypePubKeys, "%," + a + "%")
			.or()
			.like(TCsqService::getTypePubKeys, "%" + a + ",%")
			.groupAfter());
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqService> selectInIdsOrInFundIds(List<Long> serviceIds, List<Long> fundIds) {
		MybatisPlusBuild mybatisPlusBuild = new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.and()
			.groupBefore();
		boolean isServiceListEmpty = serviceIds.isEmpty();
		boolean isFundListEmpty = fundIds.isEmpty();

		if(isServiceListEmpty && isFundListEmpty) {
			return new ArrayList<>();
		}

		if(!isServiceListEmpty) {
			mybatisPlusBuild = mybatisPlusBuild
				.in(TCsqService::getId, serviceIds);
		}

		if(!isServiceListEmpty && !isFundListEmpty) {
			mybatisPlusBuild = mybatisPlusBuild.or();
		}

		if(!isFundListEmpty) {
			mybatisPlusBuild = mybatisPlusBuild
				.in(TCsqService::getFundId, fundIds);
		}
		mybatisPlusBuild = mybatisPlusBuild.groupAfter();

		return MybatisPlus.getInstance().findAll(new TCsqService(), mybatisPlusBuild);
	}

}
