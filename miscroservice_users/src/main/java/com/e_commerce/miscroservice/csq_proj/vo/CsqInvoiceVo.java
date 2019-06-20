package com.e_commerce.miscroservice.csq_proj.vo;

import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-18 15:25
 */
@Data
public class CsqInvoiceVo extends TCsqService {

	private String dateString;

	private Double myAmount;

	private String orderNo;

}
