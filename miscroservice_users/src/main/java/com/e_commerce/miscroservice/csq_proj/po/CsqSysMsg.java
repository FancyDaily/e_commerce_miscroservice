package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 系统消息
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:58
 */
@Table
@Data
@Builder
public class CsqSysMsg  extends BaseEntity{

	@Id
	private Long id;

	@Column(commit = "标题")
	private String title;

	@Column(commit = "内容")
	private String content;

	@Column(commit = "项目编号")
	private Long serviceId;

	@Column(commit = "类别")
	private Integer type;


}
