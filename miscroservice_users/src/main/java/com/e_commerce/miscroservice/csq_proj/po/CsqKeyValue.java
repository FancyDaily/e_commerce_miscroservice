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
@Table
@Builder
public class CsqKeyValue {

	@Id
	private Long id;

	@Column(commit = "键值")
	private Long key;

	@Column(commit = "副键值", defaultVal = "0")
	private Long subKey;

	@Column(commit = "值")
	private String value;

	@Column(commit = "类型")
	private Integer type;

	@Column(commit = "扩展字段")
	private String extend;

	@Column(commit = "创建者编号", isNUll = false)
	private Long createUser;

	@Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;

	@Column(commit = "更新者编号", isNUll = false)
	private Long updateUser;

	@Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
	private Timestamp updateTime;

	@Column(commit = "有效性", defaultVal = "1")
	private String isValid;
}
