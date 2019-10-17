package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class BaseEntity extends com.e_commerce.miscroservice.commons.entity.application.BaseEntity {

	/*@Column(commit = "扩展字段")
	protected String extend;

	@Column(commit = "创建者编号", isNUll = false)
	protected Long createUser;

	*//*@Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	protected Timestamp createTime;*//*

	@Column(commit = "更新者编号")
	protected Long updateUser;

	*//*@Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
	protected Timestamp updateTime;*/

	@Column(commit = "有效性", defaultVal = "1")
	protected String isValid;
}
