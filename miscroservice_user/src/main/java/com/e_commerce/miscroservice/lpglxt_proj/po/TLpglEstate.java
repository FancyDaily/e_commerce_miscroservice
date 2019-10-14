package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼盘表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:56
 */
@Table(commit = "楼盘表")
@Data
@Builder
@NoArgsConstructor
public class TLpglEstate extends BaseEntity {

	/**
	 * 名字
	 */
	@Column(commit = "名字")
	String name;
}
