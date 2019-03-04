package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;

import java.sql.Timestamp;

public class BaseDTO {
	private Boolean is_deleted;
	@Column(commit = "创建时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;

//	@Column(dateGeneStrategy = DbHandler.DateGeneStrategy.UPDATE)
//	private Timestamp updateTime;

}
