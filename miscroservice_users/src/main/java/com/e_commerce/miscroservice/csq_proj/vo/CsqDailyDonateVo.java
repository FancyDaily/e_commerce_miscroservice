package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 18:08
 */
@Data
public class CsqDailyDonateVo extends TCsqService {

	private Integer dayCnt;

	private Double dailyIncome;

	private Integer donateCnt;
}
