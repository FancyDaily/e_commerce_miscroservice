package com.e_commerce.miscroservice.csq_proj.testAuto.po;

import com.e_commerce.miscroservice.csq_proj.testAuto.vo.Test;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.AutoGenerateCode;
import com.e_commerce.miscroservice.csq_proj.po.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试自动生成工具
 * @Author: FangyiXu
 * @Date: 2019-07-15 16:54
 */
@Data
@Builder
@NoArgsConstructor
@Table
public class TestPo extends BaseEntity {

	@Column(commit = "姓名")
	private String name;

	@Column(commit = "年龄")
	private Integer age;

	public static void main(String[] args) {
		AutoGenerateCode.generate(TestPo.class);
	}

    public Test  copyTest() {
        return null;
     }

}
