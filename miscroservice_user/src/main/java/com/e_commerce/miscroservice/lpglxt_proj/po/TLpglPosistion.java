package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼盘管理职位表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:51
 */
@Table(value = "t_lpgl_posistion",commit = "楼盘管理职位表")
@Data
@Builder
@NoArgsConstructor
public class TLpglPosistion extends BaseEntity {

	@Column(commit = "职位名称")
	private String posisitionName;

	@Column(commit = "优惠额度")
	private Double discountCredit;

}
