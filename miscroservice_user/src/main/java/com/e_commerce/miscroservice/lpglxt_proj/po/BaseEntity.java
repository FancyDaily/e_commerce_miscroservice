package com.e_commerce.miscroservice.lpglxt_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class BaseEntity extends com.e_commerce.miscroservice.commons.entity.application.BaseEntity {

	@Column(commit = "有效性", defaultVal = "1")
	protected String isValid;
}
