package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 15:59
 */
public interface LpglUserService {
	TLpglUser findOne(Long id);

	AjaxResult login(String username, String password, HttpServletRequest request, HttpServletResponse response, String openid);

	AjaxResult register(String username, String password, Long posistionId, HttpServletResponse response, HttpServletRequest request);
}
