package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-05 10:34
 */
@Data(matchSuffix = true)
public class CsqServiceReportVo {
	@Id
	private Long id;

	private Long serviceId;

	@Column(commit = "标题")
	private String title;

	@Column(commit = "描述")
	private String description;

	@Column(commit = "图片")
	private String pic;

	public TCsqServiceReport copyTCsqServiceReport() {
		return null;
	}
}
