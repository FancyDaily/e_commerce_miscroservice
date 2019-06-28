package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Key-Value
 * @Author: FangyiXu
 * @Date: 2019-06-10 09:56
 */
@Data
@Table(commit = "从善桥key-value表")
@Builder
public class TCsqKeyValue extends BaseEntity {

	/*public static void main(String[] args) {
	    TCsqKeyValue tcsqk = new TCsqKeyValue();
		tcsqk.setId(111L);
		tcsqk.setTheValue("12");

		List<TCsqKeyValue> all = tcsqk.findAll(tcsqk.eq(TCsqKeyValue::getType, tcsqk.getType()));
		tcsqk.save();
		tcsqk.findOne(tcsqk.eq(TCsqKeyValue::getType, tcsqk.getCountColumn()));

	}*/

	@Id
	private Long id;

	@Column(commit = "键值")
	private Long mainKey;

	@Column(commit = "副键值", defaultVal = "0")
	private Long subKey;

	@Column(commit = "值")
	private String theValue;

	@Column(commit = "类型", length = 11, isNUll = false)
	private Integer type;

}



