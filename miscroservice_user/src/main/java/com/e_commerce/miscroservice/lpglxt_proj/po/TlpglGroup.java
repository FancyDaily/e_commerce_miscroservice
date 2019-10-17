package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 分组表
 * @Author: FangyiXu
 * @Date: 2019-10-17 13:41
 */
@Table(commit = "分组表")
@Data
@Builder
public class TlpglGroup {

	@Column(commit = "楼盘编号")
	private Long estateId;

	@Column(commit = "组名")
	private String name;

	@Column(commit = "用户编号")
	private Long userId;
}
