package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-21 14:52
 */
@Data
public class CsqShareVo {

	private String title;

	private String name;

	private String coverPic;

	private Double currentAmont;

	private Double expectedAmount;

	private Double donateSum;

	private Integer donateCnt;

	private List<CsqDonateRecordVo> donateRecordVos;
}
