package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglAuthority;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglPosistion;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglRole;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:39
 */
public interface LpglRoleService {

	JSONObject findRole(Long userId);

	List<TLpglRole> findAllRole();

	List<TLpglPosistion> findAllPosistion();

	List<TLpglAuthority> findAllAuthority();

	List<TLpglRole> findAllPosistionRole(Long posistionId);

	List<TLpglAuthority> findAllRoleAuthority(Long roleId);

	JSONObject findAllPosistionRoleAuthority();

	TLpglPosistion findAllPosistionById(Long posistionId);

	int updatePosistionRole(Long posistionId, Long userId);
}
