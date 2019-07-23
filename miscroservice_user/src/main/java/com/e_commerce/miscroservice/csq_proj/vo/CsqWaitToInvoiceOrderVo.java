package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqOrder;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-05 16:27
 */
@Data
public class CsqWaitToInvoiceOrderVo {

	@Id
	private Long id;

//	private Long userId;
//
//	private Long fromId;

	private Long toId;

	@Transient
	private String date;

	@Transient
	private String serviceName;

//	@Column(commit = "支付来源")
//	private Integer fromType;

	@Column(commit = "钱款去向")
	private Integer toType;

	@Column(commit = "订单编号")
	private String orderNo;

	@Column(commit = "订单金额", precision = 2, isNUll = false)
	private Double price;

//	@Column(commit = "订单状态", length = 11, defaultVal = "1", isNUll = false)
//	private Integer status;

	@Column(commit = "开票状态", length = 11, defaultVal = "0", isNUll = false)
	private Integer inVoiceStatus;

	@Column(commit = "订单创建时间戳")
	private Long orderTime;

	public TCsqOrder copyTCsqOrder() {
		return null;
	}
}
