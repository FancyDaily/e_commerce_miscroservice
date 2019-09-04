package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-27 17:16
 */
@Table(commit = "丛善桥线下数据(旧)")
@Data
@Builder
@NoArgsConstructor
public class TCsqOffLineData {

	@Id
	private Long id;

	private Long fundId;

	private String fundName;

	@Column(commit = "类型")
	private Integer type;

	@Column(commit = "捐款人")
	private String userName;

	@Column(commit = "数额", precision = 2)
	private Double money;

	@Column(commit = "日期 eg.2019-7-2")
	private String date;

	@Column(commit = "描述")
	private String description;

	@Column(defaultVal = "1")
	private String isValid;

}
