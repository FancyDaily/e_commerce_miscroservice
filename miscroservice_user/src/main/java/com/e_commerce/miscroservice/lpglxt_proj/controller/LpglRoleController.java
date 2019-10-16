package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglRoleService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 楼盘管理 权限管理Controller
 *
 * @Author:
 * @Date: 2019-06-11 15:33
 */
@RequestMapping("lpgl/role")
@RestController
@Log
public class LpglRoleController {


	@Autowired
	private LpglRoleService lpglRoleService;


	@Autowired
	private LpglUserService lpglUserService;

	@RequestMapping("findAll")
	public Object findAllRole(){
		AjaxResult ajaxResult = new AjaxResult();
		Long userId = IdUtil.getId();
		if (userId==null){
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("未登录");
			return ajaxResult;
		}
		TLpglUser tLpglUser = lpglUserService.findOne(userId);
		if (tLpglUser==null){
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("用户不存在");
			return ajaxResult;
		}
		JSONObject jsonObject = lpglRoleService.findRole(userId);
		ajaxResult.setSuccess(true);
		ajaxResult.setData(jsonObject);
		return ajaxResult;
	}

}
