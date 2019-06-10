package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 收藏
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:39
 */
@Table
@Data
@Builder
public class CsqUserCollection {

	private Long id;

	private Long serviceId;


}
