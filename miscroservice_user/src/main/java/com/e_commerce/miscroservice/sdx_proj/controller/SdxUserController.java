package com.e_commerce.miscroservice.sdx_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.UrlAuth;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书袋熊用户
 * @Author: FangyiXu
 * @Date: 2019-10-25 11:57
 */
@RestController
@RequestMapping("sdx/user")
@Log
public class SdxUserController {

	@Autowired
	private TSdxUserService tSdxUserService;

	/**
	 * 用户信息
	 * @param userId 要查询的用户编号(为空表示当前用户)
	 * @return
	 */
	@RequestMapping("infos")
	@UrlAuth
	public Object infos(Long userId) {
		Long ids = userId == null? IdUtil.getId() : userId;
		log.info("用户信息, userId={}. ids={}", userId, ids);
		return Response.success(tSdxUserService.infos(userId));
	}

	@RequestMapping("global/donate")
	public Object globalDonate() {
		log.info("全局播报");
		return Response.success(tSdxUserService.globalDonate());
	}



}
