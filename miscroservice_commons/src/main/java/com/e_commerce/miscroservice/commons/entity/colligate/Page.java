package com.e_commerce.miscroservice.commons.entity.colligate;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-09 17:13
 */
@Data
@Builder
public class Page {

	private Integer pageNum;
	private Integer pageSize;
}
