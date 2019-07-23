package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捐助vo
 * @Author: FangyiXu
 * @Date: 2019-06-21 15:28
 */
@Data(matchSuffix = true)
@Builder
@NoArgsConstructor
public class CsqDonateRecordVo {

	/**
	 * 分钟前
	 */
	private Integer minutesAgo;

	/**
	 * 捐款人头像
	 */
	private String userHeadPortraitPath;

	/**
	 * 捐款人
	 */
	private String name;

	/**
	 * 捐款数额
	 */
	private Double donateAmount;

	/**
	 * 创建时间戳
	 */
	private Long createTime;

	/**
	 * 受益项目名
	 */
	private String toName;

}
