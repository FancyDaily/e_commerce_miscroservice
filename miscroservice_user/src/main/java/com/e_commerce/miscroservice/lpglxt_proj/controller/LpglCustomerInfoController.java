package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCustomerInfoService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户报备
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:59
 */
@RequestMapping("lpgl/customerInfos")
@RestController
@Log
public class LpglCustomerInfoController {

	@Autowired
	private LpglHouseService lpglHouseService;

	@Autowired
	private LpglCertService lpglCertService;

	@Autowired
	private LpglCustomerInfoService lpglCustomerInfoService;

	/**
	 * 报备信息列表
	 * @param status 状态
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list")
	public Object commitCustomerInfos(Integer status, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("报备信息列表, status={}, pageNum={}, pageSize={}",status, pageNum, pageSize);
			QueryResult list = lpglCustomerInfoService.list(status, pageNum, pageSize);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "报备信息列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("报备信息列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 递交报备信息
	 * @param houseId 商品房编号
	 * @param telephone 手机号
	 * @param description 描述
	 * @return
	 */
	@RequestMapping("commit")
	public Object commitCustomerInfos(Long id, Long houseId, String telephone, String description) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("递交报备信息, id={}, houseId={}, telephone={}, description={}", id, houseId, telephone, description);
			lpglCustomerInfoService.commit(id, houseId, telephone, description);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "递交报备信息", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("递交报备信息", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 删除报备信息
	 * @param id 报备信息编号
	 * @return
	 */
	@RequestMapping("remove")
	public Object removeCustomerInfos(Long id) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("删除报备信息, id={}", id);
			lpglCustomerInfoService.remove(id);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "删除报备信息", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除报备信息", e);
			result.setSuccess(false);
		}
		return result;
	}
}
