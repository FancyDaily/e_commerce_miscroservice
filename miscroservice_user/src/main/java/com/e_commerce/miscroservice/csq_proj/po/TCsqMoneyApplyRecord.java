package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-09 17:29
 */
@Table(commit = "打款申请表")
@Data
@NoArgsConstructor
@Builder
public class TCsqMoneyApplyRecord {

	private Long id;

	private Long entityId;

	private Integer entityType;

	@Column(commit = "打款申请的发起人")
	private Long userId;

	@Column(commit = "申请人", charset = "utf8mb4")
	private String applyPerson;

	@Column(commit = "申请人联系方式")
	private String applyPersonContact;

	@Column(commit = "申请金额")
	private Double money;

	@Column(commit = "申请原因描述", charset = "utf8mb4")
	private String description;

	@Column(commit = "发票图片")
	private String invoicePic;

	@Column(commit = "审核状态")
	private Integer status;

}
