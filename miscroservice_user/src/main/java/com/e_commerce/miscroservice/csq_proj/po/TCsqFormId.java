package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.csq_proj.vo.CsqFormIdVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-02 13:46
 */
@Data
@Table(commit = "微信formid")
@Builder
@NoArgsConstructor
public class TCsqFormId extends BaseEntity {

	@Column(commit = "用户编号")
	private Long userId;

	@Column(commit = "微信formid")
	private String formId;

	CsqFormIdVo copyCsqFormIdVo() {
		return null;
	}
	
}
