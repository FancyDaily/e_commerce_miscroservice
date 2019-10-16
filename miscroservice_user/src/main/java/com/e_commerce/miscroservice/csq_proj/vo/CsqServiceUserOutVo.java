package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 用户捐款数据vo
 * @Author: FangyiXu
 * @Date: 2019-10-16 14:57
 */
@Builder
@Data
public class CsqServiceUserOutVo {

	private String headPic;

	private String name;

	private String telephone;

	private Timestamp createTime;

	private Integer donateNum;

	private Double donateAmount;

	private Timestamp lastDonatetime;

	private Double lastDonateAmount;


}
