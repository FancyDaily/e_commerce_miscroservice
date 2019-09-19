package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFundDonateVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqWaitToInvoiceOrderVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 涉及应用外支付的订单表
 * @Author: FangyiXu
 * @Date: 2019-06-11 17:57
 */
@Table(commit = "应用外支付的订单表")
@Data
@Builder
@NoArgsConstructor
public class TCsqOrder extends BaseEntity {

	@Id
	private Long id;

	private Long userId;

	private Long fromId;

	private Long toId;

	@Transient
	private String date;

	@Transient
	private String serviceName;

	/*@Column(commit = "业务类型(爱心账户充值、申请基金支付、基金充值、项目捐赠 .etc)")
	private Integer type;*/

	@Column(commit = "支付来源")
	private Integer fromType;

	@Column(commit = "钱款去向")
	private Integer toType;

	@Column(commit = "订单编号")
	private String orderNo;

	@Column(commit = "订单金额", precision = 2, isNUll = false)
	private Double price;

	@Column(commit = "订单状态", length = 11, defaultVal = "1", isNUll = false)
	private Integer status;

	@Column(commit = "开票状态", length = 11, defaultVal = "0", isNUll = false)
	private Integer inVoiceStatus;

	@Column(commit = "订单创建时间戳")
	private Long orderTime;

	@Column(commit = "是否为匿名发起人")
	private Integer isAnonymous;

	@Column(commit = "是否为活动相关")
	private Integer isActivity;

	public CsqWaitToInvoiceOrderVo copyCsqOrderVo() {
		return null;
	}

	public CsqFundDonateVo copyCsqFundDonateVo() {
		CsqFundDonateVo csqFundDonateVo = new CsqFundDonateVo();
//		Long time = getOrderTime();
//		csqFundDonateVo.setYear(DateUtil.timeStamp2Date(time, "yyyy"));
//		csqFundDonateVo.setDate(DateUtil.timeStamp2Date(time, "MM/dd"));
		csqFundDonateVo.setMoney(getPrice());
		return csqFundDonateVo;
	}

	/*public static void main(String[] args) {
		AutoGenerateCode.generate();
	}*/
}
