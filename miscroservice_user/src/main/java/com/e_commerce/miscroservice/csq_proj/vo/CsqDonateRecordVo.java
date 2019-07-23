package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-21 15:28
 */
@Data(matchSuffix = true)
@Builder
@NoArgsConstructor
public class CsqDonateRecordVo {

	private Integer minutesAgo;

	private String userHeadPortraitPath;

	private String name;

	private Double donateAmount;

	private Long createTime;

}
