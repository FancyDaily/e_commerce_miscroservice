package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-29 10:32
 */
@Table
@Data
@Builder
@NoArgsConstructor
public class TCsqPersonInChargeInfo {

	@Id
	private Long id;

	private Long fundId;

	private String fundName;

	private String occupation;

	private String personInCharge;

	private String personInChargePic;

	private String description;
}
