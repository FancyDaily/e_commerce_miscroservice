package com.e_commerce.miscroservice.user.service.apiImpl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.HttpResult;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.HttpAPIService;
import com.e_commerce.miscroservice.commons.util.colligate.MD5;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.util.colligate.TextFormater;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat.WeChatPay;
import com.e_commerce.miscroservice.user.service.api.APIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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
	
	private static final String smsUrl = "http://hy.mix2.zthysms.com/sendSms.do";
	//private static final String temp = "是您本次的登录验证码，请勿泄露给他人，十分钟之内有效。工作愉快！【晓时时间银行】";
	
	private static final String userName = "xiaoshihy";
	private static final String passwd = "tGlO3q";
	
	@Value("${smsTemplate}")
	private String smsTemplate;
	
	public String execute(Map<String, Object> params) {
		return execute(params, ApplicationEnum.XIAOSHI_APPLICATION);
	}

	public String execute(Map<String, Object> params, ApplicationEnum applicationEnum) {
		String tkey = TextFormater.format(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
		String password = MD5.crypt(MD5.crypt(passwd) + tkey );
		params.put("username",userName);
		params.put("password", password);
		params.put("tkey",tkey);
		String template = StringUtil.decodeStr2Unicode(smsTemplate);
		if(!Objects.equals(applicationEnum.toCode(), ApplicationEnum.XIAOSHI_APPLICATION.toCode())) {
			template = template.replace(ApplicationEnum.XIAOSHI_APPLICATION.getDesc(), applicationEnum.getDesc());
		}
		params.put("content", String.format(template,params.get(AppConstant.VALID_CODE)));
		return sendSms(params);

	}


	private  static  String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {

			TrustManager[] tm = {new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			}};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (Exception ce) {
		}
		return null;
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
			System.out.println(result.getBody());
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
