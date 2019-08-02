package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-08-02 13:46
 */
@Data
@Table
@Builder
public class TcsqFormId extends BaseEntity {

	@Column(commit = "用户编号")
	private Long userId;

	@Column(commit = "微信formid")
	private Long formId;
	
	public static void main(String[] args) {
	    
	}
}
