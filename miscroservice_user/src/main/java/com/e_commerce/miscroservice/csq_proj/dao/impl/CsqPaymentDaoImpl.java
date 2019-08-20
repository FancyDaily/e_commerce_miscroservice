package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.mapper.CsqPaymentMapper;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPaymentDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @ClassName CsqPaymentDaoImpl
 * @Auhor huangyangfeng
 * @Date 2019-06-17 15:17
 * @Version 1.0
 */
@Repository
public class CsqPaymentDaoImpl implements CsqPaymentDao {
	@Resource
	private CsqPaymentMapper csqPaymentMapper;

	public MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqUserPaymentRecord> findWaters(Long userId) {
		List<TCsqUserPaymentRecord> list = MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(),baseBuild()
			.eq(TCsqUserPaymentRecord::getUserId,userId)
		);
		return list;
	}

	@Override
	public TCsqUserPaymentRecord findWaterById(Long recordId) {
		TCsqUserPaymentRecord record = MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(),baseBuild()
		.eq(TCsqUserPaymentRecord::getId,recordId)
		);
		return record;
	}



	@Override
	public Double countMoney(Long userId, Integer inOut) {
		Double money = csqPaymentMapper.countMoney(userId,inOut);
		money = money == null? 0:money;
		return money;
	}

	@Override
	public int insert(TCsqUserPaymentRecord... build) {
		return MybatisPlus.getInstance().save(build);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), baseBuild()
			.eq(TCsqUserPaymentRecord::getUserId, userId)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdDesc(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), byuserIdDescBuild(userId));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOut(Long userId, Integer option) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), baseBuild()
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getInOrOut, option)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOutDesc(Long userId, Integer option) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), byUserIdAndInOrOutDescBuild(userId, option)
		);
	}

	@Override
	public int multiInsert(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		return MybatisPlus.getInstance().save(tCsqUserPaymentRecords);
	}

	@Override
	public TCsqUserPaymentRecord selectByOrderIdAndNeqId(Long orderId, Long paymentId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(),baseBuild()
			.eq(TCsqUserPaymentRecord::getOrderId, orderId)
			.neq(TCsqUserPaymentRecord::getId, paymentId)
			);
	}

	@Override
	public TCsqUserPaymentRecord selectByOrderNoAndUserIdAndInOut(Long orderId, Long userId, int toCode) {
		return MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(), baseBuild()
			.eq(TCsqUserPaymentRecord::getOrderId, orderId)
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getInOrOut, toCode)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdDescPage(Long userId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = byuserIdDescBuild(userId);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOutDescPage(Integer pageNum, Integer pageSize, Long userId, Integer option) {
		MybatisPlusBuild mybatisPlusBuild = byUserIdAndInOrOutDescBuild(userId, option);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize)
		);
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndNeqEntityTypeDescPage(Long userId, Integer pageNum, Integer pageSize, Integer... entityType) {
		MybatisPlusBuild mybatisPlusBuild = byUserIdBuild(userId);
		for(Integer eT:entityType) {
			mybatisPlusBuild = mybatisPlusBuild.neq(TCsqUserPaymentRecord::getEntityType, eT);
		}
		mybatisPlusBuild = mybatisPlusBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime));
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOutAndNeqEntityDescPage(Integer pageNum, Integer pageSize, Long userId, Integer option, Integer... entityType) {
		MybatisPlusBuild mybatisPlusBuild = byUserIdAndInOrOutBuild(userId, option);
		for(Integer et:entityType) {
			mybatisPlusBuild = mybatisPlusBuild.neq(TCsqUserPaymentRecord::getEntityType, et);
		}
		mybatisPlusBuild = mybatisPlusBuild.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime));
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), mybatisPlusBuild.page(pageNum, pageSize));
	}

	private MybatisPlusBuild byUserIdAndInOrOutDescBuild(Long userId, Integer option) {
		return byUserIdAndInOrOutBuild(userId, option)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime));
	}

	private MybatisPlusBuild byUserIdAndInOrOutBuild(Long userId, Integer option) {
		return baseBuild()
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getInOrOut, option);
	}

	private MybatisPlusBuild byuserIdDescBuild(Long userId) {
		return byUserIdBuild(userId)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime));
	}

	private MybatisPlusBuild byUserIdBuild(Long userId) {
		return baseBuild()
			.eq(TCsqUserPaymentRecord::getUserId, userId);
	}
}
