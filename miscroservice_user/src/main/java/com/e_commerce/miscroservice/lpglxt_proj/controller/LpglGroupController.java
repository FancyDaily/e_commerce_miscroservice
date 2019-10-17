package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分组
 * @Author: FangyiXu
 * @Date: 2019-10-17 17:13
 */
@RequestMapping("group")
@RestController
@Log
public class LpglGroupController {

	@Autowired
	private LpglGroupService lpglGroupService;

	/**
	 * 分组列表
	 * @param estateId 楼盘编号
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @return
	 */
	@RequestMapping("list")
	public Object groupList(Long estateId, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("分组列表, estateId={}, pageNum={}, pageSize={}", estateId, pageNum, pageSize);
			QueryResult result1 = lpglGroupService.list(estateId, pageNum, pageSize);
			result.setData(result1);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "分组列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("分组列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 添加分组
	 * @param estateId 楼盘编号
	 * @param name 名字
	 * @return
	 */
	@RequestMapping("add")
	public Object addGroup(Long estateId, String name) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("添加分组, estateId={}, name={}", estateId, name);
			lpglGroupService.add(estateId, name);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "添加分组", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("添加分组", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 修改分组
	 * @param id 楼盘编号
	 * @param name 名字
	 * @return
	 */
	@RequestMapping("modify")
	public Object modifyGroup(Long id, String name) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("修改分组, estateId={}, name={}", id, name);
			lpglGroupService.modify(id, name);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "修改分组", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改分组", e);
			result.setSuccess(false);
		}
		return result;
	}
}
