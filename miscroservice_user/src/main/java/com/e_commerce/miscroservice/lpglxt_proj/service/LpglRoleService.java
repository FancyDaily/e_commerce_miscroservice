package com.e_commerce.miscroservice.lpglxt_proj.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:39
 */
public interface LpglRoleService {

	JSONObject findRole(Long userId);
}
