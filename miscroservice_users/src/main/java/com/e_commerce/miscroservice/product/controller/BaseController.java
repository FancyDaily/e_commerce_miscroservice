package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.entity.application.TFormid;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.message.controller.MessageCommonController;
import com.e_commerce.miscroservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 'product 的公共Controller
 */
public class BaseController {



	@Autowired
	protected ProductService productService;
	@Autowired
	protected RedisUtil redisUtil;
	@Autowired
	protected MessageCommonController messageCommonController;

	/**
	 * 有效
	 */
	protected String IS_VALID_YES = "1";
	/**
	 * 无效
	 */
	protected String IS_VALID_NO = "0";
	/**
	 * 是封面图片
	 */
	protected String IS_COVER_YES = "1";
	/**
	 * 是组织账户
	 */
	protected Integer IS_COMPANY_ACCOUNT_YES = 1;
	/**
	 * 用户类型为公益组织
	 */
	protected String USER_TYPE_ORGANIZATION = "2";

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
