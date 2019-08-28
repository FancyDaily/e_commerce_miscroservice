package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-15 14:49
 */
@Data
@Builder
@NoArgsConstructor
public class CsqFundDonateVo {

	private String year;

	private String date;

	private Double money;

	private String name;

	private String wholeDecription;
}
