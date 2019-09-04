package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-02 10:50
 */
@Table
@Data
public class TCsqTransferData {

	@Id
	private Long id;

	private String date;

	private String billNumber;

	private String donaterName;

	@Column(precision = 2)
	private Double money;

	private String description;

	private Integer inOrOut;

	private String fundName;

	private Long fundId;

	@Column(defaultVal = "1")
	private String isValid;

	public TCsqOffLineData copyTCsqOffLineData() {
		TCsqOffLineData build = TCsqOffLineData.builder()
			.fundName(fundName)
			.fundId(fundId)
			.description(description)
			.date(date)
			.money(money)
			.type(inOrOut)
			.userName(donaterName)
			.isValid(isValid).build();
		return build;
	}

}
