package com.e_commerce.miscroservice.commons.util.colligate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 功能描述:发送HTTP请求到服务器
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Sep 6, 2017 2:44:56 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class RestClientUtil {

	private static class Singleton {  
        /** 
         * 单例对象实例 
         */  
	  
        static RestTemplate INSTANCE = null;
        static{
        	//HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();        	
        	SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(1000);
            requestFactory.setReadTimeout(1000);
        	INSTANCE = new RestTemplate(requestFactory);
        }
        
    }
	
	private RestClientUtil(){

		
	}
	
	/** 
     * 单例实例 
     */  
    public static RestTemplate getInstance() {  
        return Singleton.INSTANCE;  
    } 
    
    /** 
     * post提交
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, String data) {  
        return RestClientUtil.getInstance().postForObject(url, null,  
                String.class, data);  
    }
    
    /**
     * post提交  
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, Object> params) {  
        return RestClientUtil.getInstance().postForObject(url, null,  
                String.class, params);  
    }
    
    
    /** 
     * post提交
     * @param url
     * @param data
     * @return
     */
    public static <T> T post(String url, String data,Class<T> responseType) {  
        return RestClientUtil.getInstance().postForObject(url, null,  
        		responseType, data);  
    }
    
    /**
     * post提交  
     * @param url
     * @param params
     * @return
     */
    public static <T> T post(String url, Map<String, Object> params,Class<T> responseType) {  
        return RestClientUtil.getInstance().postForObject(url, null,responseType, params);  
    }
    
    
    /**
     * post提交  
     * @param url
     * @param params
     * @return
     */
    public static <T> ResponseEntity<T>  post(String url,Class<T> responseType, Map<String, Object> params) {  
        return RestClientUtil.getInstance().postForEntity(url, null, responseType, params);
    }
    
    
    
    /** 
     * get提交
     * @param url
     * @param data
     * @return
     */
    public static String get(String url, String data) {  
        return RestClientUtil.getInstance().getForObject(url, 
                String.class, data);  
    }
    
    /**
     * get提交  
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, Map<String, Object> params) {  
        return RestClientUtil.getInstance().getForObject(url, 
                String.class, params);  
    }
    
    
    /** 
     * get提交
     * @param url
     * @param data
     * @return
     */
    public static <T> T get(String url, String data,Class<T> responseType) {  
        return RestClientUtil.getInstance().getForObject(url, null,  
        		responseType, data);  
    }
    
    /**
     * get提交  
     * @param url
     * @param params
     * @return
     */
    public static <T> T get(String url, Map<String, Object> params,Class<T> responseType) {  
        return RestClientUtil.getInstance().getForObject(url, null,  
        		responseType, params);  
    }
    
    
    /**
     * get提交  
     * @param url
     * @param params
     * @return
     */
    public static <T> ResponseEntity<T>  get(String url,Class<T> responseType, Map<String, Object> params) {  
        return RestClientUtil.getInstance().getForEntity(url,  
        		responseType, params);  
    }
    
    
    public static void main(String[] args) {  
        
    	ResponseEntity<String> str = RestClientUtil.post("https://www.baidu.com/", String.class, new HashMap<String, Object>());
    	System.out.println(str.getStatusCodeValue());
    } 
    
}
