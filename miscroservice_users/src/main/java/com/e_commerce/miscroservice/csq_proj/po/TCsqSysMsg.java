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
@Table(commit = "从善桥系统消息表")
@Data
@Builder
public class TCsqSysMsg {

	@Id
	private Long id;

	@Column(commit = "标题")
	private String title;

	@Column(commit = "内容")
	private String content;

	@Column(commit = "项目编号")
	private Long serviceId;

	@Column(commit = "类别", length = 11, isNUll = false)
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
