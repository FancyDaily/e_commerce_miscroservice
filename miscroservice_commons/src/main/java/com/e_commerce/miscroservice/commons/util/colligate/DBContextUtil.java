package com.e_commerce.miscroservice.commons.util.colligate;
/**
 * 功能描述:动态切换数据库
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Sep 25, 2017 11:41:07 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class DBContextUtil {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
	  
    public static void setDbType(String type) {  
        contextHolder.set(type);  
    }  
  
    public static String getDbType() {  
        return contextHolder.get();  
    }  
  
    public static void clearDbType() {  
        contextHolder.remove();  
    }  
}
