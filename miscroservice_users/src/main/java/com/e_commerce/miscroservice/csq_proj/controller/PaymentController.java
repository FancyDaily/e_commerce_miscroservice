package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import com.e_commerce.miscroservice.csq_proj.service.CsqPaymentService;
import com.e_commerce.miscroservice.csq_proj.service.CsqUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 流水
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:37
 */
@RestController
@Log
public class PaymentController {




	@Autowired
	private CsqPaymentService csqPaymentService;

	@Autowired
	private CsqUserService csqUserService;

	/**
	 * 查询流水
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("find/waters")
	public Object findWarters(Integer pageNum,Integer pageSize){
		AjaxResult result = new AjaxResult();

		Long userId = Long.valueOf(IdUtil.getId());
		log.info("流水查询num={},size={},userId={}",pageNum,pageSize,userId);
		try {
			QueryResult<TCsqUserPaymentRecord > records = csqPaymentService.findWaters(pageNum, pageSize, userId);
			Map<String,Object> map  = new HashMap<>();
			TCsqUser tCsqUser = csqUserService.findCsqUserById(userId);
			map.put("list",records);
			map.put("payNum",tCsqUser.getPayNum());
			map.put("surplusAmount",tCsqUser.getSurplusAmount());
			result.setData(map);
			result.setSuccess(true);
		}catch (MessageException e){
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}catch (Exception e){

		}
		return result;
	}
}
