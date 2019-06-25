package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * publish表
 * @Author: FangyiXu
 * @Date: 2019-06-11 09:50
 */
@Table(commit = "从善桥publish表")
@Data
@Builder
public class TCsqPublish extends BaseEntity {

	@Id
	private Long id;

	@Column(commit = "业务类型名")
	private Integer mainKey;

	@Column(commit = "业务类型描述", defaultVal = "未知")
	private String keyDesc;

	@Column(commit = "值(json)")
	private String value;



}
