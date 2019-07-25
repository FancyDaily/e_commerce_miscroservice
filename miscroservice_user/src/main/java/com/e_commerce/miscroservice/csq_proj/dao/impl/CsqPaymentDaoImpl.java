package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
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
	@Override
	public List<TCsqUserPaymentRecord> findWaters(Long userId) {
		List<TCsqUserPaymentRecord> list = MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(),new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId,userId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
		);
		return list;
	}

	@Override
	public TCsqUserPaymentRecord findWaterById(Long recordId) {
		TCsqUserPaymentRecord record = MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(),new MybatisPlusBuild(TCsqUserPaymentRecord.class)
		.eq(TCsqUserPaymentRecord::getId,recordId)
		.eq(TCsqUserPaymentRecord::getIsValid,AppConstant.IS_VALID_YES)
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
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdDesc(Long userId) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime)));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOut(Long userId, Integer option) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getInOrOut, option)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES));
	}

	@Override
	public List<TCsqUserPaymentRecord> selectByUserIdAndInOrOutDesc(Long userId, Integer option) {
		return MybatisPlus.getInstance().findAll(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getUserId, userId)
			.eq(TCsqUserPaymentRecord::getInOrOut, option)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES)
			.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TCsqUserPaymentRecord::getCreateTime))
		);
	}

	@Override
	public int multiInsert(List<TCsqUserPaymentRecord> tCsqUserPaymentRecords) {
		return MybatisPlus.getInstance().save(tCsqUserPaymentRecords);
	}

	@Override
	public TCsqUserPaymentRecord selectByOrderIdAndNeqId(Long orderId, Long paymentId) {
		return MybatisPlus.getInstance().findOne(new TCsqUserPaymentRecord(), new MybatisPlusBuild(TCsqUserPaymentRecord.class)
			.eq(TCsqUserPaymentRecord::getOrderId, orderId)
			.neq(TCsqUserPaymentRecord::getId, paymentId)
			.eq(TCsqUserPaymentRecord::getIsValid, AppConstant.IS_VALID_YES));
	}
}
