package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Transient;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUserPaymentRecord;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-27 17:25
 */
@Data(matchSuffix = true)
public class CsqDonateUserPaymentVo extends CsqBasicUserVo {
	@Id
	private Long id;

	private Long orderId;

	private Long userId;

	private String nickName;

	private Long entityId;	//支出或收入的实体Id

	private String serviceName;

	private Integer entityType;	//支出或收入的实体类型

	@Transient
	private String date;

	@Column(commit = "金额", precision = 2, isNUll = false)
	private Double money;

	public TCsqUserPaymentRecord copyTCsqUserPaymentRecord() {
		return null;
	}

}
