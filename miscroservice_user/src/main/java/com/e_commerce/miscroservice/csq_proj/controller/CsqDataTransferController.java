package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOldUserDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.po.TOldUser;
import com.e_commerce.miscroservice.csq_proj.service.CsqDataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@RequestMapping("test7")
	public Object test7() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithPic();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test8")
	public Object test8() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithServicePic();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test6_4")
	public Object test6_4() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.currentDealWithServicePayment();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test6_2")
	public Object test6_2() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithServicePayment();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test6_3")
	public Object test6_3() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.fixUpServiceRelated();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("test6_1")
	public Object test6_1() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithFundNeqGuanMing();
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

	@RequestMapping("off_line_test_1")
	public Object off_line_1() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.offLineDeal();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("person_in_charge_infos_test")
	public Object dealWithPersonInCahrgeInfos(String folderPath) {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithPersonInChargeInfos(folderPath);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("person_in_charge_infos_test1")
	public Object findAllPersonInChargeInfos(String json) {
		AjaxResult result = new AjaxResult();
		try {
//			List<TCsqPersonInChargeInfo> allPersonInChargeInfos = csqDataTransferService.findAllPersonInChargeInfos();
//			result.setData(allPersonInChargeInfos);
			csqDataTransferService.insert(json);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("findOutTheIdOfFund")
	public Object findOutTheIdOfFund() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.findOutTheIdOfFund();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("findOutTheIdOfFund_2")
	public Object findOutTheIdOfFund_2() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.findOutTheIdOfFund(false);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("transferData/deal")
	public Object dealWithTransferData() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.dealWithTransferData();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@RequestMapping("deleteCraft_1")
	public Object deleteCraft_1() {
		AjaxResult result = new AjaxResult();
		try {
			List<TCsqUserPaymentRecord> records = csqDataTransferService.findRecords();
			Map<String, Object> map = new HashMap<>();
			map.put("records", records);
			map.put("total", records.size());
			result.setData(records);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("deleteCraft_2")
	public Object deleteCraft_2() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.findAndDeleteRecords();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("deleteCraft_3")
	public Object deleteCraft_3(String fundIds) {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.synchronizeService(fundIds);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("transfer/growthFund_1")
	public Object transeferGrowthFundRecord() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.transeferGrowthFundRecord();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("transfer/growthFund_2")
	public Object transeferGrowthFundRecordAfter() {
		AjaxResult result = new AjaxResult();
		try {
			csqDataTransferService.transeferGrowthFundRecordAfter();
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 导出捐款数据(参数写死)
	 * @return
	 */
	@RequestMapping("dataOut_1")
	public Object dataOut_1(Long toId, Integer toType) {
		AjaxResult result = new AjaxResult();
		try {
			String path = csqDataTransferService.dataOut_1(toId, toType);
			result.setData(path);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
