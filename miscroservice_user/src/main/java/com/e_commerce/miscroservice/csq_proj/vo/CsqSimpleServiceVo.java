package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import lombok.Builder;
import lombok.Data;

/**
 * 简单项目vo
 * @Author: FangyiXu
 * @Date: 2019-07-23 16:59
 */
@Data
@Builder
public class CsqSimpleServiceVo {

	/**
	 * 项目id
	 */
	private Long id;

	/**
	 * 项目类型
	 */
	private Integer type;

	public TCsqService copyTCsqService() {
		return null;
	}
}
