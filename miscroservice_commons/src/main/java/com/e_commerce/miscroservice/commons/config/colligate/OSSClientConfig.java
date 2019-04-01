package com.e_commerce.miscroservice.commons.config.colligate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;
import org.springframework.context.annotation.PropertySource;

/**
 * 功能描述:ossClient配置
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月17日 下午9:13:47
 */
@Configuration
public class OSSClientConfig {
	
	@Value("${endpoint}")
	private String endpoint;
	@Value("${accessKeyId}")
	private String accessKeyId;
	@Value("${accessKeySecret}")
	private String accessKeySecret;
	
	@Bean
	public OSSClient ossClient() {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		return ossClient;
	}
}
