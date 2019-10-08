package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-17 13:49
 */
@Data
@Builder
@NoArgsConstructor
public class CsqLineDiagramData {

	Long timeStamp;

	String date;

	Double amount;

	String label;	//横坐标

	Double value;	//纵坐标

}
