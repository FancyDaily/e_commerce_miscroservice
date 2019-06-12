package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 项目汇报
 * @Author: FangyiXu
 * @Date: 2019-06-10 10:02
 */
@Table
@Data
@Builder
public class CsqServiceReport  extends BaseEntity{

	@Id
	private Long id;

	@Column(commit = "标题")
	private String title;

	@Column(commit = "描述")
	private String desc;

	@Column(commit = "图片")
	private String pic;


}
