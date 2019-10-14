package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 楼盘管理权限表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:31
 */
@Table(commit = "楼盘管理权限表")
@Data
@Builder
public class TLpglAuthority extends BaseEntity{

	@Column(commit = "描述")
	private String description;

}
