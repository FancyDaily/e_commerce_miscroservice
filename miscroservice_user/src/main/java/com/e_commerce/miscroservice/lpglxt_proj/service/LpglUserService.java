package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUserPosistion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 15:59
 */
public interface LpglUserService {
	TLpglUser findOne(Long id);

	AjaxResult login(String username, String password, HttpServletRequest request, HttpServletResponse response, String openid);

	AjaxResult register(String username, String password, Long posistionId, HttpServletResponse response, HttpServletRequest request, String name);

	AjaxResult getOpenid(String code);

	AjaxResult userList(String name, String userAccount, Integer pageNum, Integer pageSize);

	AjaxResult authorities(Long id);

	List<TLpglUser> getBoss(Long id);

	void handOver(Long userId, Long certId);

	void delUser(Long userIds);

	AjaxResult delUserAuthorities(Long id, Long authId);

	List<TLpglUserPosistion> findUserPosition(Long userIds);
}
