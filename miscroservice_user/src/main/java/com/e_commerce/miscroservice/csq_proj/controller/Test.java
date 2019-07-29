package com.e_commerce.miscroservice.csq_proj.controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.enums.application.CsqSceneEnum;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSceneVo;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-25 22:02
 */
public class Test {
	public static void main(String[] args) {
		CsqSceneVo build = CsqSceneVo.builder()
			.userId(111111111l)
			.fundId(12313123132l)
			.type(CsqSceneEnum.TYPE_FUND.getCode()).build();
		Object o = JSONObject.toJSON(build);
		System.out.println(o);
	}
}
