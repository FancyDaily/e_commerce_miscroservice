package com.e_commerce.miscroservice.user.wechat.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 20, 2018 9:49:49 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class PayCommonUtil {
	/** 
	 * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 
	 * @return boolean 
	 */  
	@SuppressWarnings("rawtypes")
	public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {  
		StringBuffer sb = new StringBuffer();  
		Set es = packageParams.entrySet();  
		Iterator it = es.iterator();  
		while(it.hasNext()) {  
			Map.Entry entry = (Map.Entry)it.next();  
			String k = (String)entry.getKey();  
			String v = (String)entry.getValue();  
			if(!"sign".equals(k) && null != v && !"".equals(v)) {  
				sb.append(k + "=" + v + "&");  
			}  
		}  
 
		sb.append("mainKey=" + API_KEY);
 
		//算出摘要  
		String mysign = MD5.MD5Encode(sb.toString(), characterEncoding).toLowerCase();  
		String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();  
 
		//System.out.println(tenpaySign + "    " + mysign);  
		return tenpaySign.equals(mysign);  
	}  
 
	/** 
	 * @author 
	 * @Description：sign签名 
	 * @param characterEncoding 
	 *            编码格式 
	 * @param parameters 
	 *            请求参数 
	 * @return 
	 */  
	@SuppressWarnings("rawtypes")
	public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {  
		StringBuffer sb = new StringBuffer();  
		Set es = packageParams.entrySet();  
		Iterator it = es.iterator();  
		while (it.hasNext()) {  
			Map.Entry entry = (Map.Entry) it.next();  
			String k = entry.getKey().toString();  
			String v = entry.getValue().toString();  
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"mainKey".equals(k)) {
				sb.append(k + "=" + v + "&");  
			}  
		}  
		sb.append("mainKey=" + API_KEY);
		String sign = MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
		return sign;  
	}  
	public static String createLinkString(Map<String, String> params) {     
		List<String> keys = new ArrayList<String>(params.keySet());     
		Collections.sort(keys);     
		String prestr = "";     
		for (int i = 0; i < keys.size(); i++) {     
			String key = keys.get(i);     
			String value = params.get(key);     
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符     
				prestr = prestr + key + "=" + value;     
			} else {     
				prestr = prestr + key + "=" + value + "&";     
			}     
		}     
		return prestr;     
	}     
	/** 
	 * @author 
	 * @Description：将请求参数转换为xml格式的string 
	 * @param parameters 
	 *            请求参数 
	 * @return 
	 */  
	@SuppressWarnings("rawtypes")
	public static String getRequestXml(SortedMap<Object, Object> parameters) {  
		StringBuffer sb = new StringBuffer();  
		sb.append("<xml>");  
		Set es = parameters.entrySet();  
		Iterator it = es.iterator();  
		while (it.hasNext()) {  
			Map.Entry entry = (Map.Entry) it.next();  
			String k = entry.getKey().toString();  
			String v = entry.getValue().toString();   
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {  
				sb.append("<" + k + ">"  + v + "</" + k + ">");  
			} else {  
				sb.append("<" + k + ">" + v + "</" + k + ">");  
			}  
		}  
		sb.append("</xml>");  
		return sb.toString();  
	}  
 
	/** 
	 * 取出一个指定长度大小的随机正整数. 
	 *  
	 * @param length 
	 *            int 设定所取出随机数的长度。length小于11 
	 * @return int 返回生成的随机数。 
	 */  
	public static int buildRandom(int length) {  
		int num = 1;  
		double random = Math.random();  
		if (random < 0.1) {  
			random = random + 0.1;  
		}  
		for (int i = 0; i < length; i++) {  
			num = num * 10;  
		}  
		return (int) ((random * num));  
	}  
 
	/** 
	 * 获取当前时间 yyyyMMddHHmmss 
	 *  
	 * @return String 
	 */  
	public static String getCurrTime() {  
		Date now = new Date();  
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
		String s = outFormat.format(now);  
		return s;  
	}
 
	public static boolean verify(String text, String sign, String key, String input_charset) {
		text = text + key;     
		String mysign =MD5.MD5Encode(text, input_charset).toUpperCase();  
		System.out.println(mysign);	System.out.println(mysign);	System.out.println(mysign);	System.out.println(mysign);
		if (mysign.equals(sign)) {     
			return true;     
		} else {     
			return false;     
		}     
	}  

}
