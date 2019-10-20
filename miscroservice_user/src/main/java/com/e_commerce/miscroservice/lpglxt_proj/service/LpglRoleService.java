package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglAuthority;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglRole;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUserPosistion;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:39
 */
public interface LpglRoleService {

	JSONObject findRole(Long userId);

	JSONObject findRole(Long userId, Integer status);

	List<TLpglRole> findAllRole();

	List<TLpglPosistion> findAllPosistion();

	List<TLpglAuthority> findAllAuthority();

	List<TLpglRole> findAllPosistionRole(Long posistionId);

	List<TLpglRole> findAllPosistionRole(Long... posistionId);

	List<TLpglRole> findAllPosistionRole(List<Long> posistionId);

	List<TLpglAuthority> findAllRoleAuthority(Long... roleId);

	List<TLpglAuthority> findAllRoleAuthority(List<Long> roleId);

	JSONObject findAllPosistionRoleAuthority();

	TLpglPosistion findAllPosistionById(Long posistionId);

	int updatePosistionRole(Long posistionId, Long userId);

	AjaxResult findRolePage(Long userId, Long id, Integer status);

	List<TLpglAuthority> findAllFloorAuthority(Long roleId);

	List<TLpglUserPosistion> findUserPosition(Long id);
}
