package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCustomerInfoDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCustomerInfos;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglCustomerInfoDaoImpl implements LpglCustomerInfoDao {

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglCustomerInfos.class)
			.eq(TLpglCert::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public TLpglCustomerInfos selectByPrimaryKey(Long customerInfoId) {
		return MybatisPlus.getInstance().findOne(new TLpglCustomerInfos(), baseBuild()
			.eq(TLpglCustomerInfos::getId, customerInfoId)
		);
	}

	@Override
	public int update(TLpglCustomerInfos tLpglCustomerInfos) {
		return MybatisPlus.getInstance().update(tLpglCustomerInfos, baseBuild()
			.eq(TLpglCustomerInfos::getId, tLpglCustomerInfos.getId())
		);
	}

	@Override
	public List<TLpglCustomerInfos> selectByStatusPage(Integer status, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild eq = baseBuild()
			.eq(TLpglCustomerInfos::getStatus, status);
		IdUtil.setTotal(eq);
		return MybatisPlus.getInstance().findAll(new TLpglCustomerInfos(), eq.page(pageNum, pageSize));
	}

	@Override
	public int insert(TLpglCustomerInfos... build) {
		return insert(Arrays.asList(build));
	}

	@Override
	public int insert(List<TLpglCustomerInfos> list) {
		return MybatisPlus.getInstance().save(list);
	}

	@Override
	public List<TLpglCustomerInfos> selectByStatusAndGteUpdateTimePage(Integer status, Long timeStamp, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild lte = baseBuild()
			.eq(TLpglCustomerInfos::getStatus, status)
			.gte(TLpglCustomerInfos::getUpdateTime, new Timestamp(timeStamp).toString());
		IdUtil.setTotal(lte);
		return MybatisPlus.getInstance().findAll(new TLpglCustomerInfos(), lte.page(pageNum, pageSize));
	}

	@Override
	public int updateByStatusAndLtUpdateTimePage(Integer status, Long timeStamp) {
		MybatisPlusBuild gt = baseBuild()
			.eq(TLpglCustomerInfos::getStatus, status)
			.lt(TLpglCustomerInfos::getUpdateTime, new Timestamp(timeStamp).toString());
		return MybatisPlus.getInstance().update(new TLpglCustomerInfos(), gt);
	}

	@Override
	public int remove(Long id) {
		TLpglCustomerInfos tLpglCustomerInfos = new TLpglCustomerInfos();
		tLpglCustomerInfos.setIsValid(AppConstant.IS_VALID_NO);
		tLpglCustomerInfos.setId(id);
		return MybatisPlus.getInstance().update(tLpglCustomerInfos, baseBuild()
			.eq(TLpglCustomerInfos::getId, tLpglCustomerInfos.getId())
		);
	}

}
