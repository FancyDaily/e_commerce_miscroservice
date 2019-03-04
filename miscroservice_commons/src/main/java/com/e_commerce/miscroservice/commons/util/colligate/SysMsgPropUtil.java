package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 功能描述:系统消息配置属性加载工具
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年9月26日 上午10:25:27
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class SysMsgPropUtil {

	public static Properties properties = null;
	static{
		if(properties==null){
			
			try {
				properties = new Properties();
				properties = PropertiesLoaderUtils.loadAllProperties("config/sysMsgTemplate.properties");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static String get(String key){
		try {			
			return properties.getProperty(key,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void set(String key,String value){
		properties.setProperty(key, value);		
	}
}
