package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

/**
 * 
 * 功能描述:获取IP地址
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:08:37
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class RequestUtil {
    
	/**
	 * 真实IP地址
	 */
	public static String getRealRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();

		return ip;
	}
	
	
	/** 
     * 判断是否为Ajax请求 
     * 
     * @param request HttpServletRequest 
     * @return 是true, 否false 
     */  
    public static boolean isAjaxRequest(HttpServletRequest request) {
    	String header = request.getHeader("x-requested-with");
        return (header==null?false:true);  
 
    }
    
    
    /**
     * 是否跨越请求
     * @param request
     * @return
     */
    public static boolean isCorsRequest(HttpServletRequest request) {   	
		return (request.getHeader(HttpHeaders.ORIGIN) != null);
	}
    
    
    /**
     * 读取request的字符串
     * @param request
     * @return
     */
	public static String read(HttpServletRequest request) {
		String inputLine = null;
		// 接收到的数据
		StringBuffer recieveData = new StringBuffer();
		BufferedReader in = null;
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(request.getInputStream(), "UTF-8");
			in = new BufferedReader(reader);
			reader.close();
			while ((inputLine = in.readLine()) != null) {
				recieveData.append(inputLine);
			}

		} catch (IOException e) {

		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
			}
		}

		return recieveData.toString();
	}

}
