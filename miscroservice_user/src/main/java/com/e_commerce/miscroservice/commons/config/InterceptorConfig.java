package com.e_commerce.miscroservice.commons.config;

import com.e_commerce.miscroservice.commons.filter.InitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年10月24日 下午2:23:49
 */
//@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private InitInterceptor initInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(initInterceptor);
	}
}
