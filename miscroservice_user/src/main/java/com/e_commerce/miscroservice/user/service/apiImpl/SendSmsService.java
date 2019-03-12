package com.e_commerce.miscroservice.user.service.apiImpl;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.HttpResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.HttpAPIService;
import com.e_commerce.miscroservice.commons.util.colligate.MD5;
import com.e_commerce.miscroservice.commons.util.colligate.TextFormater;
import com.e_commerce.miscroservice.user.service.api.APIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 6, 2018 2:31:32 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
@Service("sendSmsService")
public class SendSmsService implements APIService {
	
	@Autowired
	private HttpAPIService httpService;

	private static Logger LOG = LoggerFactory.getLogger(SendSmsService.class);
	
	private static final String smsUrl = "http://api.zthysms.com/sendSms.do";
	//private static final String temp = "是您本次的登录验证码，请勿泄露给他人，十分钟之内有效。工作愉快！【晓时时间银行】";
	
	private static final String userName = "xiaoshihy";
	private static final String passwd = "tGlO3q";
	
	@Value("${smsTemplate}")
	private String smsTemplate;
	
	public String execute(Map<String, Object> params) {
		String tkey = TextFormater.format(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
		String password = MD5.crypt(MD5.crypt(passwd) + tkey );
		params.put("username",userName);
		params.put("password", password);
		params.put("tkey",tkey);
		String template=smsTemplate;
		params.put("content", String.format(template,params.get(AppConstant.VALID_CODE)));
		return sendSms(params);
		
	}
	public String sendServMsg(Map<String, Object> params) {
		String tkey = TextFormater.format(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
		String password = MD5.crypt(MD5.crypt(passwd) + tkey );
		params.put("username",userName);
		params.put("password", password);
		params.put("tkey",tkey);
		return sendSms(params);
		
	}
	private String sendSms(Map<String, Object> params){
		String res;
		try {
			HttpResult result = httpService.doPost(smsUrl, params);
			res = new String(result.getBody().getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new MessageException("字符串转换异常");
		} catch (Exception e) {
			throw new MessageException("sendSms -> http请求错误");
		}
		String str[] = res.split(",");
		if(str.length>0 && "1".equals(str[0])){
			LOG.info("手机号码为" + String.valueOf(params.get("mobile")) + "的手机短信发送成功");
			return "true";
		}
		else if (str.length>0 && "-1".equals(str[0])){
			LOG.info("手机号码为"+ String.valueOf(params.get("mobile")) + "的短信发送失败,消息编号为"+str[1]);
			return "false";
		}
		else if(str.length>0 && "13".equals(str[0])){
			LOG.info("手机号码为" + String.valueOf(params.get("mobile")) + "的手机错误");			
			
			return "false";
		}
		else{
			LOG.info(res);
			return "false";
		}
	}
	
	
}
