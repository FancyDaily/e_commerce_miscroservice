package com.e_commerce.miscroservice.lpglxt_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-11 15:31
 */
@Data
@Builder
@NoArgsConstructor
public class LpglGroupHouseMapVo {

	/**
	 * 单元
	 */
	private Integer group;

	/**
	 * 楼层房号列表
	 */
	List<LpglAreaHouseMapVo> lpglAreaHouseMapVos;
}
