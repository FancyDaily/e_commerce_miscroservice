package com.e_commerce.miscroservice.sdx_proj.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 购书订单编号
 * @Author: FangyiXu
 * @Date: 2019-10-24 17:12
 */
@Data
@Builder
public class SdxPurchaseOrderDateGroupVo {

	/**
	 * 日期
	 */
	private String monthDay;

	/**
	 * 带年份的日期
	 */
	private String wholeDate;

	/**
	 * 订单列表
	 */
	List<SdxPurchaseOrderVo> sdxPurchaseOrderVos;

}
