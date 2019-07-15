package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.po.TCsqServiceReport;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-27 17:24
 */
@Data(matchSuffix = true)
public class CsqServiceDetailVo extends CsqServiceListVo {

	private Integer raiseStatus;	//筹备状态

	@Transient
	private List<CsqBasicUserVo> donaters;	//捐助人列表

	@Transient
	private Double sumTotalOut;

	@Transient
	private List<CsqUserPaymentRecordVo> csqUserPaymentRecordVos;

	@Transient
	private List<CsqServiceReportVo> csqServiceReportVos;

	@Column(commit = "描述图")
	private String detailPic;

	@Column(commit = "受益人/机构")
	private String beneficiary;

	@Column(commit = "银行卡号")
	private String creditCard;

	public TCsqService copyTCsqService() {
		return null;
	}
}
