package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 楼盘管理角色表
 * @Author: FangyiXu
 * @Date: 2019-10-14 14:45
 */
@Table(commit = "楼盘管理用户职位关联表")
@Data
@Builder
@NoArgsConstructor
public class TLpglUserPosistion extends BaseEntity {

	@Transient
	private String name;

	@Column(commit = "用户id",length = 11)
	private Long userId;

	@Column(commit = "职位id",length = 11)
	private Long posistionId;
}
