package com.e_commerce.miscroservice.sdx_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 书袋熊publish简单vo
 * @Author: FangyiXu
 * @Date: 2019-10-25 14:22
 */
@Data
@Builder
@NoArgsConstructor
public class SdxBasicPublishVo {
	Integer key;
	String value;
}

