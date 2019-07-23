package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.csq_proj.vo.CsqServiceReportVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目汇报
 * @Author: FangyiXu
 * @Date: 2019-06-10 10:02
 */
@Table(commit = "从善桥项目汇报表")
@Data
@Builder
@NoArgsConstructor
public class TCsqServiceReport extends BaseEntity {

	@Id
	private Long id;

	private Long serviceId;

	@Column(commit = "标题")
	private String title;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "图片")
	private String pic;

	public CsqServiceReportVo copyCsqServiceReportVo() {
		return null;
	}

}
