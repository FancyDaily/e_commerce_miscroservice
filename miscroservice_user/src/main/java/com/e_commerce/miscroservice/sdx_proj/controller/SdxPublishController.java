package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.sdx_proj.service.SdxPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * publishController
 *
 * @Author: FangyiXu
 * @Date: 2019-06-12 11:12
 */
@Log
@RestController
@RequestMapping("csq/publish")
public class SdxPublishController {

	@Autowired
	private SdxPublishService publishService;

	/**
	 * 插入一条publish记录
	 *
	 * @param mainKey       主要类型
	 * @param keys          键
	 * @param names         名
	 * @param keyDesc       键描述
	 * @param isObjectArray 是否使用对象数组存储
	 *                      {
	 *                      "success": true,
	 *                      "errorCode": "",
	 *                      "msg": "",
	 *                      "data": ""
	 *                      }
	 * @return
	 */
	@RequestMapping("upload")
	@UrlAuth(withoutPermission = true)
	public AjaxResult upload(Integer mainKey, Integer[] keys, String[] names, String keyDesc, boolean isObjectArray) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("插入一条publish记录, mainKey={}, keys={}, names={}", mainKey, keys, names);
			publishService.setPublishName(mainKey, keys, names, keyDesc, isObjectArray);
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

	/**
	 * 获取publish表的映射
	 *
	 * @param mainKey 指定main键
	 *                {
	 *                "data": [
	 *                "北京方向",
	 *                "东京方向",
	 *                "南京方向",
	 *                "普希金方向",
	 *                "长沙方向",
	 *                "皇后大道东方向"
	 *                ]
	 *                }
	 * @return
	 */
	@RequestMapping("gets")
	@UrlAuth(withoutPermission = true)
	public AjaxResult get(@RequestParam Integer mainKey) {
		AjaxResult result = new AjaxResult();
		try {
			log.info("获取publish表的映射, mainKey={}", mainKey);
			Object asList = publishService.getAsList(mainKey);
			result.setData(asList);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "获取publish表的映射", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取publish表的映射", e);
			result.setSuccess(false);
		}
		return result;
	}

}
