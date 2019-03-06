package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;

/**
 *
 */
public interface ProductService {

	/**
	 * 功能描述:提交求助
	 * 作者:马晓晨
	 * 创建时间:2018/10/30 下午12:06
	 * @param token
	 * @param
	 * @return
	 */
	void submitSeekHelp(TUser user, ServiceParamView param, String token);
	/**
	 *
	 * 功能描述:提交服务
	 * 作者:马晓晨
	 * 创建时间:2018年10月31日 下午2:58:14
	 * @param user
	 * @param param
	 * @param token
	 */
	void submitService(TUser user, ServiceParamView param, String token);

}
