package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCertDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglCustomerInfoDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCertEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglCustomerInfoEnum;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglHouseEnum;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCustomerInfos;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglCertServieImpl implements LpglCertService {

	@Autowired
	private LpglCertDao lpglCertDao;

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Autowired
	private LpglCustomerInfoDao lpglCustomerInfoDao;

	@Override
	public QueryResult underCertList(Integer type, Integer status, Integer pageNum, Integer pageSize) {
		List<TLpglCert> tLpglCerts = lpglCertDao.selectByTypeAndStatusPage(type, status, pageNum, pageSize);
		long total = IdUtil.getTotal();

		return PageUtil.buildQueryResult(tLpglCerts, total);
	}

	@Override
	public void cert(Long userId, Long certId, Integer status) {
		boolean certPass = TlpglCertEnum.STATUS_PASS.getCode() == status;
		TLpglCert tLpglCert = lpglCertDao.selectByPrimaryKey(certId);
		dealWithLpglHouse(tLpglCert, certPass);

		tLpglCert.setCertUserId(userId);
		tLpglCert.setStatus(status);
		lpglCertDao.update(tLpglCert);
	}

	@Override
	public void commitCert(Long houseId, Integer type, Double disCountPrice, String description) {
		TLpglCert build = TLpglCert.builder()
			.houseId(houseId)
			.type(type)
			.discountPrice(disCountPrice)
			.description(description).build();
		lpglCertDao.insert(build);
	}

	private void dealWithLpglHouse(TLpglCert tLpglCert, boolean certPass) {
		/*boolean pass = TlpglCertEnum.TYPE_PRICEDISCOUNT.getCode() != tLpglCert.getType();
		if(pass) return;*/
		TlpglCertEnum type = TlpglCertEnum.getType(tLpglCert.getType());
		if (type == null) {
			return;
		}
		Long houseId = tLpglCert.getHouseId();
		TLpglHouse tLpglHouse = lpglHouseDao.selectByPrimaryKey(houseId);
		switch (type) {
			case TYPE_PRICEDISCOUNT:
				tLpglHouse.setDisCountStatus(!certPass ? TlpglHouseEnum.DISCOUNT_STAUTS_NO.getCode() : TlpglHouseEnum.DISCOUNT_STATUS_YES.getCode());
				if (certPass) {
					tLpglHouse.setDisCountPrice(tLpglHouse.getDisCountPrice());
				}
				//TODO 消息通知：审核结果抄送销售经理确认
				break;
			case TYPE_SOLDOUTREQUEST:
				tLpglHouse.setStatus(!certPass ? TlpglHouseEnum.STATUS_INITAIL.getCode() : TlpglHouseEnum.STATUS_SOLDOUT.getCode());
				//TODO 消息通知：审核结果抄送最高级领导[总经理,副总经理]
				break;
			case TYPE_CUSTOMER:
				Long customerInfoId = tLpglCert.getCustomerInfoId();
				TLpglCustomerInfos tLpglCustomerInfos = lpglCustomerInfoDao.selectByPrimaryKey(customerInfoId);
				tLpglCustomerInfos.setStatus(!certPass ? TlpglCustomerInfoEnum.STATUS_INVALID.getCode() : TlpglCustomerInfoEnum.STATUS_VALID.getCode());
				lpglCustomerInfoDao.update(tLpglCustomerInfos);
				return;
		}
		lpglHouseDao.update(tLpglHouse);
	}
}
