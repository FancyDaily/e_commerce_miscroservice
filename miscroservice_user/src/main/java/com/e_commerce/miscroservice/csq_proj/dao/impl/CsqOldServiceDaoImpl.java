package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOldServiceDao;
import com.e_commerce.miscroservice.csq_proj.po.TOldService;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:26
 */
@Component
public class CsqOldServiceDaoImpl implements CsqOldServiceDao {
	@Override
	public List<TOldService> selectByNames(String... names) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.in(TOldService::getTitle, names)
			.eq(TOldService::getStatus, 1));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatus(int status, int checkStatus) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatusNeqTotalAmount(int status, int checkStatus, String totalAmount) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus)
			.neq(TOldService::getTotalamount, totalAmount));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatusAndAdderEqCHargePersonId(int status, int checkStatus) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus)
			.eq(TOldService::getAdder, TOldService::getChargepersonid));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatusAndAdderEqCHargePersonIdAndFinType(int status, int checkStatus, String type) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus)
			.eq(TOldService::getFintype, type)
			.eq(TOldService::getAdder, TOldService::getChargepersonid));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatusByDType(int status, int checkStatus, String pf) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus)
			.eq(TOldService::getDtype, pf));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatusByDTypeAndDonationAmountNeq(int status, int checkStatus, String pf, String s) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus)
			.eq(TOldService::getDtype, pf)
			.neq(TOldService::getDonationamount, s));
	}

	@Override
	public List<TOldService> selectByStatusByCheckStatusByDTypeAndDonationAmountNeqAndFinTypeNeq(int status, int checkStatus, String pf, String s, String finType) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getStatus, status)
			.eq(TOldService::getCheckstatus, checkStatus)
			.eq(TOldService::getDtype, pf)
			.neq(TOldService::getDonationamount, s)
			.neq(TOldService::getFintype, finType)
		);
	}

	@Override
	public List<TOldService> selectInNames(List<String> serviceNames) {
		return MybatisPlus.getInstance().findAll(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.in(TOldService::getTitle, serviceNames));
	}

	@Override
	public TOldService selectByPrimaryKey(String pfid) {
		return MybatisPlus.getInstance().findOne(new TOldService(), new MybatisPlusBuild(TOldService.class)
			.eq(TOldService::getId, pfid)
		);
	}

}
