package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOldUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TOldUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqDataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据导入
 * @Author: FangyiXu
 * @Date: 2019-08-09 16:16
 */
@RequestMapping("transfer")
@RestController
public class CsqDataTransferController {

	@Autowired
	private CsqDataTransferService csqDataTransferService;

	@Autowired
	private CsqOldUserDao csqOldUserDao;

	@RequestMapping("test1")
	public Object test1() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithOpenid();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test2")
	public Object test2() {
		AjaxResult result = new AjaxResult();
		try {
			List<TOldUser> tOldUsers = csqOldUserDao.selectAll();
			Object sthInCommon = csqDataTransferService.getSthInCommon(tOldUsers);
			result.setData(sthInCommon);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test3")
	public Object test3() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.transferUser();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test4")
	public Object test4() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithOldIdAtPayment();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test5")
	public Object test5() {
		AjaxResult result = new AjaxResult();
		try {
			String growthFundRecords = csqDataTransferService.getGrowthFundRecords();
			result.setData(growthFundRecords);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test6")
	public Object test6() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithFundNService();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test3_1")
	public Object test3_1() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithPayment();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


}
