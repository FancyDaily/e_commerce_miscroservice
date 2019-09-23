package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqFundEnum;
import com.e_commerce.miscroservice.commons.enums.application.CsqServiceEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.dao.CsqServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		return MybatisPlus.getInstance().findAll(new TCsqService(), inIdsBuild(csqServiceIds));
	}

	private MybatisPlusBuild inIdsBuild(List<Long> csqServiceIds) {
		return new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getId, csqServiceIds)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public int insert(TCsqService... service) {
		return MybatisPlus.getInstance().save(service);
	}

	@Override
	public List<TCsqService> selectAll() {
		return MybatisPlus.getInstance().findAll(new TCsqService(), allBuild());
	}

	@Override
	public List<TCsqService> selectMine(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), minePageBuild(userId));
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

	@Override
	public MybatisPlusBuild getBaseBuild() {
		return baseBuild();
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

	@Override
	public List<TCsqService> selectInFundIds(List<Long> fundIds) {
		MybatisPlusBuild build = baseBuild()
			.in(TCsqService::getFundId, fundIds);
		/*IdUtil.setTotal(build);
		build.page(pageNum, pageSize);*/
		return MybatisPlus.getInstance().findAll(new TCsqService(), build);
	}

	@Override
	public List<TCsqService> selectInIdsPage(List<Long> csqServiceIds, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = inIdsBuild(csqServiceIds);
		IdUtil.setTotal(mybatisPlusBuild);
		return MybatisPlus.getInstance().findAll(new TCsqService(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqService> selectMinePage(Integer pageNum, Integer pageSize, Long userId) {
		MybatisPlusBuild mybatisPlusBuild = minePageBuild(userId);
		IdUtil.setTotal(minePageBuild(userId));

		return MybatisPlus.getInstance().findAll(new TCsqService(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqService> selectAllPage(Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = allBuild();
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqService(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqService> selectInIdsOrInFundIdsPage(Integer pageNum, Integer pageSize, List<Long> serviceIds, List<Long> fundIds) {
		boolean isServiceListEmpty = serviceIds.isEmpty();
		boolean isFundListEmpty = fundIds.isEmpty();

		if(isServiceListEmpty && isFundListEmpty) {
			return new ArrayList<>();
		}
		MybatisPlusBuild mybatisPlusBuild = inIdsOrInFundIdsBuild(serviceIds, fundIds, isServiceListEmpty, isFundListEmpty);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqService(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqService> selectLikeByPubKeysAndUserIdNeqAndFundStatus(String a, Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild()
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.neq(TCsqService::getUserId, userId)
			.and()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_SERIVE.getCode())
			.or()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_FUND.getCode())
			.eq(TCsqService::getFundStatus, CsqFundEnum.STATUS_PUBLIC.getVal())
			.groupAfter()
			.groupAfter()
			.and()
			.groupBefore()
			.like(TCsqService::getTypePubKeys, "%," + a + "%")
			.or()
			.like(TCsqService::getTypePubKeys, "%" + a + ",%")
			.or()
			.eq(TCsqService::getTypePubKeys, a)
			.groupAfter());
	}

	@Override
	public int multiUpdate(List<TCsqService> oldServices) {
		oldServices = oldServices.stream()
			.filter(a -> a != null).collect(Collectors.toList());
		List<Long> updaterIds = oldServices.stream()
			.map(TCsqService::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(oldServices, new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getId, updaterIds));
	}

	@Override
	public List<TCsqService> selectInExtends(List<Long> collect) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.in(TCsqService::getExtend, collect));
	}

	@Override
	public List<TCsqService> selectWithBuild(MybatisPlusBuild baseBuild) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild);
	}

	@Override
	public List<TCsqService> selectWithBuildPage(MybatisPlusBuild baseBuild, Integer pageNum, Integer pageSize) {
		IdUtil.setTotal(baseBuild);

		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqService> selectByType(int code) {
		return MybatisPlus.getInstance().findAll(new TCsqService(), baseBuild()
			.eq(TCsqService::getType, code)
		);
	}

	@Override
	public TCsqService selectByName(String name) {
		return MybatisPlus.getInstance().findOne(new TCsqService(), new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getName, name));
	}

	@Override
	public List<TCsqService> selectByName(String name, Boolean isFuzzySearch) {
		MybatisPlusBuild build = baseBuild();
		build = isFuzzySearch?
			build.eq(TCsqService::getName, name):
			build.like(TCsqService::getName, "%" + name + "%");

		return MybatisPlus.getInstance().findAll(new TCsqService(), build);
	}

	@Override
	public List<TCsqService> selectByNamePage(String searchParam, Boolean isFuzzySearch, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild build = baseBuild();
		build = isFuzzySearch?
			build.eq(TCsqService::getName, searchParam):
			build.like(TCsqService::getName, "%" + searchParam + "%");
		IdUtil.setTotal(build);
		return MybatisPlus.getInstance().findAll(new TCsqService(), build.page(pageNum, pageSize));
	}

	private MybatisPlusBuild inIdsOrInFundIdsBuild(List<Long> serviceIds, List<Long> fundIds, boolean isServiceListEmpty, boolean isFundListEmpty) {
		MybatisPlusBuild mybatisPlusBuild = new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.and()
			.groupBefore();

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
		return mybatisPlusBuild;
	}

	private MybatisPlusBuild allBuild() {
		return baseBuild()
			.eq(TCsqService::getIsShown, CsqServiceEnum.IS_SHOWN_YES.getCode())    //判断是否可展示
			.eq(TCsqService::getStatus, CsqServiceEnum.STATUS_INITIAL.getCode())
			.and()
			.groupBefore()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_SERIVE.getCode())
			.groupAfter()
			.or()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_FUND.getCode())    //若为基金唯一对应项目
			.eq(TCsqService::getFundStatus, CsqFundEnum.STATUS_PUBLIC.getVal())    //已公开的基金对应的项目
			.groupAfter()
			.groupAfter()
			/*.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),    //按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),    //按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount),    //按还需筹多少金额正序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getSumTotalIn)    //按收入倒序*/
			.orderBy(
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getPriority),	//优先级从高到底
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getSumTotalIn),	//累计收入倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getUpdateTime)	//更新时间倒序
			);
	}

	public static void main(String[] args) {
		String build = new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqService::getIsShown, CsqServiceEnum.IS_SHOWN_YES.getCode())    //判断是否可展示
			.eq(TCsqService::getStatus, CsqServiceEnum.STATUS_INITIAL.getCode())

			.and()
			.groupBefore()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_SERIVE.getCode())
			.groupAfter()
			.or()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_FUND.getCode())    //若为基金唯一对应项目
			.eq(TCsqService::getFundStatus, CsqFundEnum.STATUS_PUBLIC.getVal())    //已公开的基金对应的项目
			.groupAfter()
			.groupAfter()
			.orderBy(
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getPriority),	//优先级从高到底
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getSumTotalIn),	//累计收入倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getUpdateTime)	//更新时间倒序
			).build();
		System.out.println(build);
	}

	/*public static void main(String[] args) {
		String build = new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.eq(TCsqService::getIsShown, CsqServiceEnum.IS_SHOWN_YES.getCode())    //判断是否可展示
			.eq(TCsqService::getStatus, CsqServiceEnum.STATUS_INITIAL.getCode())
			.and()
			.groupBefore()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_SERIVE.getCode())
			.groupAfter()
			.or()
			.groupBefore()
			.eq(TCsqService::getType, CsqServiceEnum.TYPE_FUND.getCode())    //若为基金唯一对应项目
			.eq(TCsqService::getFundStatus, CsqFundEnum.STATUS_PUBLIC.getVal())    //已公开的基金对应的项目
			.groupAfter()
			.groupAfter()
			*//*.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),    //按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),    //按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount),    //按还需筹多少金额正序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getSumTotalIn)    //按收入倒序*//*
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getUpdateTime),
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getSumTotalIn)
			).build();
		System.out.println(build);
	}*/

	private MybatisPlusBuild minePageBuild(Long userId) {
		return new MybatisPlusBuild(TCsqService.class)
			.eq(TCsqService::getUserId, userId)
			.eq(TCsqService::getIsValid, AppConstant.IS_VALID_YES)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getCreateTime),    //按发布时间倒序
				MybatisPlusBuild.OrderBuild.buildDesc(TCsqService::getType),    //按类型倒序(把项目排在上边
				MybatisPlusBuild.OrderBuild.buildAsc(TCsqService::getExpectedRemainAmount)    //按还需筹多少金额正序
			);
	}

}
