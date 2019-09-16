package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-21 14:52
 */
@Data
@Builder
@NoArgsConstructor
public class CsqShareVo {

	private Long serviceId;

	private String title;

	private String name;

	private String coverPic;

	private String sharePic;

	private Double currentAmont;

	private Double expectedAmount;

	private Double donateSum;

	private Integer donateCnt;

	private List<CsqDonateRecordVo> donateRecordVos;
}
