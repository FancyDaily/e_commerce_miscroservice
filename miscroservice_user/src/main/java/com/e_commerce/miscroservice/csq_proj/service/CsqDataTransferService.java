package com.e_commerce.miscroservice.csq_proj.service;

import com.e_commerce.miscroservice.csq_proj.po.TOldUser;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:18
 */
public interface CsqDataTransferService {
	void dealWithOpenid();

	Object getSthInCommon(List<TOldUser> tOldUsers);

	void transferUser();

	void dealWithPayment();

	void dealWithOldIdAtPayment();

	String getGrowthFundRecords();

	void dealWithFundNService();

	void dealWithFundNeqGuanMing();

	void dealWithPic();

	void dealWithServicePic();
}
