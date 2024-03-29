package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-25 21:07
 */
@Data
@Builder
public class CsqSceneVo {

	private Long userId;

	private Long fundId;

	private Long serviceId;

	private String usertel;

	private Integer type;	//类型 1基金 2项目 3openid匹配业务 etc.

	private Integer activityMark;	//活动标识
}
