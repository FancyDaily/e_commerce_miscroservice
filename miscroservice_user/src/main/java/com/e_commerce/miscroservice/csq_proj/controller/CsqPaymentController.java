package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 流水
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:37
 */
@RequestMapping("csq/payment")
@RestController
@Log
public class CsqPaymentController {

	@Autowired
	private CsqPaymentService csqPaymentService;



	/**
	 * 查询流水
	 *
	 * @param pageNum          页码
	 * @param pageSize         大小
	 * @param option           收入0/支出1 全部null
	 * @param isGroupingByYear 是否按年份分组
	 *                         <p>
	 *                         {
	 *                         "list": {
	 *                         "resultList": [
	 *                         {
	 *                         "id": 31,
	 *                         "orderId": 8,	//订单id
	 *                         "userId": 2000,
	 *                         "entityId": 2000,	//对象id
	 *                         "entityType": 1,	//对象类型
	 *                         "user": "",
	 *                         "serviceName": "",	//项目名称
	 *                         "date": "",	//日期
	 *                         "description": "",	//描述
	 *                         "inOrOut": 1,	//收支标记
	 *                         "money": 333	//金额
	 *                         }
	 *                         ],
	 *                         "totalCount": 3
	 *                         },
	 *                         "outMoney": "",	//支出
	 *                         "inMoney": 1905	//收入
	 *                         }
	 * @return
	 */
	@RequestMapping("find/waters")
	@UrlAuth
	public AjaxResult findWarters(Integer pageNum, Integer pageSize, @RequestParam(required = false) Integer option, boolean isGroupingByYear) {
		AjaxResult result = new AjaxResult();

		Long userId = IdUtil.getId();
		log.info("流水查询num={},size={},userId={}", pageNum, pageSize, userId);
		try {
			Object records = csqPaymentService.findWaters(pageNum, pageSize, userId, option, isGroupingByYear);
			Double inMoney = csqPaymentService.countMoney(userId, 0);
			Double outMoney = csqPaymentService.countMoney(userId, 1);
			Map<String, Object> map = new HashMap<>();
			map.put("list", records);
			map.put("inMoney", inMoney);
			map.put("outMoney", outMoney);
			result.setData(map);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询流水", e);
		}
		return result;
	}

	/**
	 * 查询我的证书
	 *
	 * @param recordId 记录编号
	 *                 <p>
	 *                 {
	 *                 "date":"2019-06-26",	//日期
	 *                 "code":"https://timebank-test-img.oss-cn-hangzhou.aliyuncs.com/person/QR31.jpg",	//code
	 *                 "money":333, //金额
	 *                 "name":"007",	//名
	 *                 "time":"2019-06-26",	//日期
	 *                 "serviceName":"",	//项目名
	 *                 "countMoney":""	//做你关机
	 *                 }
	 * @return
	 */
	@RequestMapping("/my/certificate")
	@UrlAuth
	public AjaxResult findMyCertificate(Long recordId) {
		AjaxResult result = new AjaxResult();

		Long userId = IdUtil.getId();
		log.info("查询我的证书 userId={}", userId);

		try {
			Map<String, Object> map = csqPaymentService.findMyCertificate(recordId, userId);
			result.setData(map);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询流水", e);
		}
		return result;
	}
}
