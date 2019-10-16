package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼盘管理角色表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:45
 */
@Table(commit = "楼盘管理角色表")
@Data
@Builder
@NoArgsConstructor
public class TLpglRole extends BaseEntity {

	@Column(commit = "角色名称")
	private String roleName;

}
