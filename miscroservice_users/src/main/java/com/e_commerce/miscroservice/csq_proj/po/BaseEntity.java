package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;

import java.sql.Timestamp;

public class BaseEntity {

	@Column(commit = "更新时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
	private Timestamp updateTime;

	@Column(commit = "创建时间戳", dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;
	@Column(commit = "创建者编号", isNUll = false)
	private Long createUser;


	@Column(commit = "更新者编号", isNUll = false)
	private Long updateUser;
	@Column(commit = "扩展字段")
	private String extend;



	@Column(commit = "有效性", defaultVal = "1")
	private String isValid;
}
