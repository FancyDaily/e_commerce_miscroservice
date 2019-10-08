package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.JavaDocRead;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-02 17:19
 */
@Data
@Builder
@NoArgsConstructor
public class CsqServiceMsgParamVo {

	/**
	 * 筹款目标达成通知展示内容
	 */
	CsqServiceListVo csqServiceListVo;

	/**
	 * 组织审核通知展示内容
	 */
	CsqUserAuthVo csqUserAuthVo;

	/**
	 * 发票开具通知展示内容
	 */
	CsqUserInvoiceVo csqUserInvoiceVo;

	/**
	 * 打款申请成功(即项目或基金支出)通知展示内容
	 */
	CsqMoneyApplyRecordVo csqMoneyApplyRecordVo;

}
