package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.csq_proj.po.TCsqFormId;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-02 14:04
 */
@Data(matchSuffix = true)
@Builder
@NoArgsConstructor
public class CsqFormIdVo {

	/**
	 * 用户编号
	 */
	private Long userId;

	/**
	 * 微信formid
	 */
	private String formId;

	TCsqFormId copyTCsqFormId() {
		return null;
	}
}
