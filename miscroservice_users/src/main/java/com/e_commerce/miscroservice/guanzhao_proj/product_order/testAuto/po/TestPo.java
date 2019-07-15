package com.e_commerce.miscroservice.guanzhao_proj.product_order.testAuto.po;

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
public class TestPo extends BaseEntity {

	private String name;

	private Integer age;

	public static void main(String[] args) {
		AutoGenerateCode.generate(TestPo.class);
	}

}
