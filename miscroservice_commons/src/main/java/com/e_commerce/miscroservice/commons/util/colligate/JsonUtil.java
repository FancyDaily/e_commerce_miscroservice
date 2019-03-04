/*
package com.e_commerce.miscroservice.commons.util.colligate;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

*/
/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Nov 23, 2017 10:28:49 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 *//*

public final class JsonUtil {

	*/
/**
	 * 返回Json字符串
	 * @param obj
	 * @param excludeNull
	 * @return
	 *//*

	public static String toJson(Object obj,boolean excludeNull){
		JsonSerializer serializer = new JsonSerializer();
		return serializer.deep(true).excludeNulls(excludeNull).serialize(obj);
	}
	
	*/
/**
	 * 返回Json字符串,包含NULL值
	 * @param obj
	 * @return
	 *//*

	public static String toJson(Object obj){
		return toJson(obj, false);
	}
	
	*/
/**
	 * 返回转换后的对象
	 * @param jsonStr
	 * @param clazz
	 * @return
	 *//*

	public static <T> T parseFromJson(String jsonStr,Class<T> clazz){
		return  new JsonParser()
         .parse(jsonStr, clazz);
	}
	
	
}
*/
