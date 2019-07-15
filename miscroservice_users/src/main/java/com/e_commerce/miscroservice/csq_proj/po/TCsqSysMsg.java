package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.vo.CsqSysMsgVo;
import lombok.Builder;
import lombok.Data;

/**
 * 系统消息
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:58
 */
@Table(commit = "从善桥系统消息表")
@Data(matchSuffix = true)
@Builder
public class TCsqSysMsg extends BaseEntity {

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

	@Column(commit = "类别", length = 11, isNUll = false, defaultVal = "1")
	private Integer type;

	@Column(commit = "已读状态", length = 11, defaultVal = "0")
	private Integer isRead;

	public TCsqSysMsg copyTCsqSysMsg() {
		return null;
	}

	public CsqSysMsgVo copyCsqSysMsgVo() {
		return null;
	}
}
