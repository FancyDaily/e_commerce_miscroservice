package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼盘管理权限表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:31
 */
@Table(commit = "楼盘管理角色权限表")
@Data
@Builder
@NoArgsConstructor
public class TLpglRoleAuthority extends BaseEntity{


	@Column(commit = "角色id")
	private Integer roleId;

	@Column(commit = "权限id")
	private Integer authorityId;
}
