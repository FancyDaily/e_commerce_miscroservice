package com.e_commerce.miscroservice.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 功能描述:Controller的公共方法
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月18日 下午3:50:03
 */
@Controller
public class BaseController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * 功能描述:输出错误消息
	 * 作者:马晓晨
	 * 创建时间:2018年12月9日 下午2:26:43
	 * @param e
	 * @return
	 */
	public String errInfo(Exception e) {  
	    StringWriter sw = null;  
	    PrintWriter pw = null;  
	    try {  
	        sw = new StringWriter();  
	        pw = new PrintWriter(sw);  
	        // 将出错的栈信息输出到printWriter中  
	        e.printStackTrace(pw);  
	        pw.flush();  
	        sw.flush();  
	    } finally {  
	        if (sw != null) {  
	            try {  
	                sw.close();  
	            } catch (IOException e1) {  
	                e1.printStackTrace();  
	            }
			}
	        if (pw != null) {  
	            pw.close();  
	        }  
	    }  
	    return sw.toString();  
	}  
	

}
