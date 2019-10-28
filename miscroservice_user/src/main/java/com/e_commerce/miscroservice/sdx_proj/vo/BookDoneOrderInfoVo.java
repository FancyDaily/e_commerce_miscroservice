package com.e_commerce.miscroservice.sdx_proj.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-28 10:18
 */
@Data
@Builder
public class BookDoneOrderInfoVo {

	private String name;

	private String headPic;

	private String doneTimeDesc;

	private Long timeStamp;

}
