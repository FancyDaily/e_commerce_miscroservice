package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserInvoice;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 15:25
 */
@Data(matchSuffix = true)
public class CsqInvoiceRecord {

	private String dateString;

	private Double myAmount;

	private String orderNo;

	@Column(commit = "项目名称")
	private String name;

	private Long itemId;	//项目编号

	private Integer itemType;	//项目类别

	private Integer isOut;	//是否已经邮寄

	public TCsqUserInvoice copyTCsqUserInvoice() {
		return null;
	}

}
