package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-27 17:21
 */
@Data
public class CsqSysMsgVo {

	@Id
	private Long id;

	private Long userId;

	@Transient
	private String dateString;

	@Transient
	private TCsqService csqService;

	@Column(commit = "标题")
	private String title;

	@Column(commit = "内容(可能为项目编号)")
	private String content;

	@Column(commit = "项目编号")
	private Long serviceId;

	@Column(commit = "类别", length = 11, isNUll = false)
	private Integer type;

	@Column(commit = "已读状态", length = 11, defaultVal = "0")
	private Integer isRead;

	public TCsqSysMsg copyTCsqSysMsg() {
		return null;
	}
}
