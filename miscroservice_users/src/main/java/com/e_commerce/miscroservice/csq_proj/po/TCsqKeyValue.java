package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Key-Value
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:56
 */
@Data
@Table(commit = "从善桥key-value表")
@Builder
public class TCsqKeyValue extends BaseEntity {

	@Id
	private Long id;

	@Column(commit = "键值")
	private Long key;

	@Column(commit = "副键值", defaultVal = "0")
	private Long subKey;

	@Column(commit = "值")
	private String value;

	@Column(commit = "类型", length = 11, isNUll = false)
	private Integer type;

}



