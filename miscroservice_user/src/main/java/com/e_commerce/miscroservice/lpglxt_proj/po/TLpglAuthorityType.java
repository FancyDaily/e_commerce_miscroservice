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
public class TLpglAuthorityType extends BaseEntity{


	@Column(commit = "权限类型名称")
	private String typeName;
	
	@Column(commit = "操作类型：1 增 2 删 3 改 4 查",length = 4)
	private Integer operationType;

	@Column(commit = "权限类型:1 控制页面访问，2 控制方法访问 3 控制文件访问",length = 4)
	private Integer type;





}
