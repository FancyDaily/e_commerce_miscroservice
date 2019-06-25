/*
package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.xiaoshitimebank.app.constant.AppConstant;

*/
/**
 * 
 * 功能描述:配置属性加载工具
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:06:47
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 *//*

public class PropUtil {

	public static Properties properties = null;
	static{
		if(properties==null){
			
			try {
				properties = new Properties();
//				properties = PropertiesLoaderUtils.loadAllProperties("config/app.properties");
				String path = ClassLoader.getSystemResource(AppConstant.APPLICATION_PATH).toURI().getPath();
				properties = PropertiesLoaderUtils.loadAllProperties(path);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	public static String get(String mainKey){
		try {			
			return properties.getProperty(mainKey,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void set(String mainKey,String theValue){
		properties.setProperty(mainKey, theValue);
	}
}
*/
