package com.e_commerce.miscroservice.lpglxt_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼层-房号映射
 * @Author: FangyiXu
 * @Date: 2019-07-11 15:31
 */
@Data
@Builder
@NoArgsConstructor
public class LpglFloorHouseMapVo {

	/**
	 * 楼层
	 */
	Integer floorNum;

	/**
	 * 房号
	 */
	Integer houseNum;
}
