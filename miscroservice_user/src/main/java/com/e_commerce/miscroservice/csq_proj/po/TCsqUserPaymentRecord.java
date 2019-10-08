package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.vo.CsqDonateUserPaymentVo;
import com.e_commerce.miscroservice.csq_proj.vo.CsqUserPaymentRecordVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 充值捐助流水(普通账户、基金、项目)
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:55
 */
@Table(commit = "从善桥流水表", charset = "utf8mb4")
@Data
@Builder
@NoArgsConstructor
public class TCsqUserPaymentRecord extends BaseEntity {

	@Id
	private Long id;

	private Long orderId;

	private Long userId;

	private Long entityId;	//支出或收入的实体Id

	private Integer entityType;	//支出或收入的实体类型

	@Transient
	private Integer toType;	//订单对应toType

	@Transient
	private String minutesAgoStr;	//多少时间以前

	@Transient
	private Integer minutesAgo;

	@Transient
	private TCsqUser user;

	@Transient
	private String serviceName;

	@Transient
	private String date;

	@Column(commit = "描述", charset = "utf8mb4")
	private String description;

	@Column(commit = "收入0/支出1", length = 11, isNUll = false)
	private Integer inOrOut;

	@Column(commit = "金额", precision = 2, isNUll = false)
	private Double money;

	public CsqUserPaymentRecordVo copyUserPaymentRecordVo() {
		return null;
	}

	public CsqDonateUserPaymentVo copyCsqDonateUserPaymentVo() {
		return null;
	}
}
