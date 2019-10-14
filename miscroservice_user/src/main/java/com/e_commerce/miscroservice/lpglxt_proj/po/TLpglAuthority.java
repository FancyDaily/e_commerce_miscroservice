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
@Table(commit = "楼盘管理权限表")
@Data
@Builder
@NoArgsConstructor
public class TLpglAuthority extends BaseEntity{


	@Column(commit = "权限名称")
	private String authorityName;

	@Column(commit = "父级Id")
	private Integer parentId;

	@Column(commit = "权限地址")
	private String url;

	@Column(commit = "权限类型id",length = 11)
	private Integer typeId;






}
