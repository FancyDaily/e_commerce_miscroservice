package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglEstateService;
import com.e_commerce.miscroservice.lpglxt_proj.vo.LpglHouseMapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 楼盘
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:36
 */
@RequestMapping("lpgl/estate")
@RestController
@Log
public class LpglEstateController {

	@Autowired
	private LpglEstateService lpglEstateService;

	/**
	 * 楼盘列表
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list")
	public Object estateList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("楼盘列表, pageNum={}, pageSize={}", pageNum, pageSize);
			QueryResult list = lpglEstateService.list(pageNum, pageSize);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "楼盘列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("楼盘列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 售楼情况图谱数据
	 * @param estateId 楼盘编号
	 * @param buildingNum 楼号
	 * @return
	 */
	@RequestMapping("house/map")
	public Object houseMap(Long estateId, Integer buildingNum) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("售楼情况图谱数据, estateId={}, buildingNum={}", estateId, buildingNum);
			Map<String, Map<Integer, Map<Double, Map<Integer, List<TLpglHouse>>>>> res = lpglEstateService.houseMap(estateId, buildingNum);
			result.setData(res);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "售楼情况图谱数据", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("售楼情况图谱数据", e);
			result.setSuccess(false);
		}
		return result;
	}
}
