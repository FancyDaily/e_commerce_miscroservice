package com.e_commerce.miscroservice.csq_proj.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: FangyiXu
 * @Date: 2019-07-20 12:39
 */
@Data
@Builder
public class WechatPhoneAuthDto {

	/**
	 * 微信加密数据
	 */
	private String encryptedData;

	/**
	 * 微信iv
	 */
	private String iv;

	/**
	 * 微信sessionKey
	 */
	private String sessionKey;
}
