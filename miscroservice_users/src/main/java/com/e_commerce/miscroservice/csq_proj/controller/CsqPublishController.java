package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * publishController
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:12
 */
@Log
@RestController
@RequestMapping("csq/publish")
public class CsqPublishController {

	@Autowired
	private CsqPublishService publishService;

	/**
	 * 插入一条publish记录
	 * @param mainKey
	 * @param keys
	 * @param names
	 * @return
	 */
	@RequestMapping("upload")
	public Object upload(Integer mainKey, Integer[] keys, String[] names) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("插入一条publish记录, mainKey={}, mainKey={}, names={}", mainKey, keys, names);
			publishService.setPublishName(mainKey, keys, names);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "插入一条publish记录", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("插入一条publish记录", e);
			result.setSuccess(false);
		}
		return result;
	}

}
