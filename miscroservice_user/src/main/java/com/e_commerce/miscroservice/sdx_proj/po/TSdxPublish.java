package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.csq_proj.po.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * publish表
 * @Author: FangyiXu
 * @Date: 2019-06-11 09:50
 */
@Table(commit = "书袋熊publish表")
@Data
@Builder
@NoArgsConstructor
public class TSdxPublish extends BaseEntity {

	@Column(commit = "业务类型名")
	private Integer mainKey;

	@Column(commit = "业务类型描述", defaultVal = "未知")
	private String keyDesc;

	@Column(commit = "值(json)", charset = "utf8mb4", length = 4096)
	private String value;



}
