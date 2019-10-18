package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglCert;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审核
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:59
 */
@RequestMapping("lpgl/cert")
@RestController
@Log
public class LpglCertController {

	@Autowired
	private LpglHouseService lpglHouseService;

	@Autowired
	private LpglCertService lpglCertService;

	/**
	 * 审批列表(优惠价申请、更改状态为已售出申请、客户报备)
	 * @param status 状态
	 * @param type 类型1优惠价申请2售出申请3客户报备
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @param isToday 是否筛选今天
	 * @param groupId 分组编号
	 * @return
	 */
	@RequestMapping("list")
	public Object houseUnderCertList(Integer status, Integer type, Integer pageNum, Integer pageSize, boolean isToday, Long groupId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("审批商品房列表(优惠价申请、更改状态为已售出申请、客户报备), status={}, type={}, pageNum={}, pageSize={}, isToday={}, groupId={}", status, type, pageNum, pageSize, isToday, groupId);
			QueryResult result1 = lpglCertService.underCertList(type, status, pageNum, pageSize, isToday, groupId);
			result.setData(result1);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "审批商品房列表(优惠价申请、更改状态为已售出申请、客户报备)", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("审批商品房列表(优惠价申请、更改状态为已售出申请、客户报备)", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 审批详情
	 * @param certId 审批详情
	 * @return
	 */
	@RequestMapping("detail")
	public Object certDetail(Long certId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("审批详情, certId={}", certId);
			TLpglCert detail = lpglCertService.detail(certId);
			result.setData(detail);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "审批详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("审批详情", e);
			result.setSuccess(false);
		}
		return result;
	}


	/**
	 * 审批(优惠价申请、更改状态为已售出申请、客户报备申请)
	 * @param certId 审批编号
	 * @param status 状态
	 * @return
	 */
	@RequestMapping("do")
	@UrlAuth(withoutPermission = true)
	public Object houseCert(Integer status, Long certId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("审批(优惠价申请、更改状态为已售出申请、客户报备申请), userId={}, certId={}, status={}", userId, certId, status);
			lpglCertService.cert(userId, certId, status);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "审批(优惠价申请、更改状态为已售出申请、客户报备申请)", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("审批(优惠价申请、更改状态为已售出申请、客户报备申请)", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 递交审核申请
	 * @param houseId 商品房编号
	 * @param type 审核类型1优惠申请2售出请求
	 * @param disCountPrice 优惠价格
	 * @param description 描述
	 * @return
	 */
	@RequestMapping("commit")
	@UrlAuth(withoutPermission = true)
	public Object commitCert(Long houseId, Integer type, Double disCountPrice, String description) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("递交审核申请, userId={}, houseId={}, type={}, disCountPrice={}, description={}, telephone={}", houseId, type, disCountPrice, description);
			lpglCertService.commitCert(userId, houseId, type, disCountPrice, description);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "递交审核申请", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("递交审核申请", e);
			result.setSuccess(false);
		}
		return result;
	}

}
