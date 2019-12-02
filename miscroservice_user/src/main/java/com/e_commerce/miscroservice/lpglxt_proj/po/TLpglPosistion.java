package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

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

	@Transient
	private List<TLpglRole> roles;

	@Column(commit = "职位名称")
	private String posisitionName;

	@Column(commit = "优惠额度")
	private Double discountCredit;

	@Column(commit = "级别", length = 11)
	private Integer level;

}
