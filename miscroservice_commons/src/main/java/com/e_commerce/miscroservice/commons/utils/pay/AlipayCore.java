package com.e_commerce.miscroservice.commons.utils.pay;

import com.e_commerce.miscroservice.commons.entity.AliPayCallBackEntity;
import org.apache.http.NameValuePair;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayCore {
    
    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(AliPayCallBackEntity sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null ) {
            return result;
        }

        Field[] a = sArray.getClass().getDeclaredFields();
        for (int i=0;i<a.length;i++){
            Field field= a[i];
            field.setAccessible(true);
            try {
                Object object =field.get(sArray);
                String key = a[i].getName();
                if (object == null || object.equals("") || key.equalsIgnoreCase("sign")
                        || key.equalsIgnoreCase("sign_type")) {
                    continue;
                }
                result.put(key, object.toString());

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//
//        for (String key : sArray.keySet()) {
//            String value = sArray.get(key);
//            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
//                || key.equalsIgnoreCase("sign_type")) {
//                continue;
//            }
//            result.put(key, value);
//        }

        return result;
    }

    public static void main(String[] args) {
        AliPayCallBackEntity sArray = new AliPayCallBackEntity();
        sArray.setBody("adfadfa");
        Field[] a = sArray.getClass().getDeclaredFields();
        for (int i=0;i<a.length;i++){
            Field field= a[i];
            field.setAccessible(true);
            try {
                Object aa =field.get(sArray);
                if (aa!=null){
                    System.out.println(a[i].getName());
                    System.out.println(aa);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

//        for (int i=0;i<b.length;i++){
//            System.out.println(b[i].getName());
//        }
    }
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(List<NameValuePair> params) {
    	
        
    	
    	String prestr = "";
    	
    	for (int i = 0; i < params.size(); i++) {
    		String key = params.get(i).getName();
    		String value = params.get(i).getValue();
    		
    		if (i == params.size() - 1) {//拼接时，不包括最后一个&字符
    			prestr = prestr + key + "=" + value;
    		} else {
    			prestr = prestr + key + "=" + value + "&";
    		}
    	}
    	
    	return prestr;
    }
    
    

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 
     * 生成文件摘要
     * @param strFilePath 文件路径
     * @param file_digest_type 摘要算法
     * @return 文件摘要结果
     */
//    public static String getAbstract(String strFilePath, String file_digest_type) throws IOException {
//        PartSource file = new FilePartSource(new File(strFilePath));
//    	if(file_digest_type.equals("MD5")){
//    		return DigestUtils.md5Hex(file.createInputStream());
//    	}
//    	else if(file_digest_type.equals("SHA")) {
//    		return DigestUtils.sha256Hex(file.createInputStream());
//    	}
//    	else {
//    		return "";
//    	}
//    }

    /**
     * 把除了非公共请求参数的请求参数以bizcontent的格式放在bizcontent里面
     */
	public static String genBizContent(List<NameValuePair> packageParams) {
        StringBuilder sb = new StringBuilder("{");

        for (int i = 0; i < packageParams.size(); i++) {
        	sb.append("\"");
            sb.append(packageParams.get(i).getName());
            sb.append("\":\"");
            sb.append(packageParams.get(i).getValue());
            sb.append("\"");
            if(i<packageParams.size()-1){
            	sb.append(",");
            }
        }
        sb.append("}");
		return sb.toString();
	}
    /**
     * 生成签名
     * @param packageParams
     * @return
     */
//	public static String genSign(List<NameValuePair> packageParams, String app_id) {
//		StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < packageParams.size(); i++) {
//            sb.append(packageParams.get(i).getName());
//            sb.append('=');
//            sb.append(packageParams.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("app_id=");
//        sb.append(app_id);
//
//        String packageSign = RSA.sign(sb.toString(), AlipayConfig.rsa_private_key, "UTF-8");
//        //仅需对sign,做URL编码
//        String sign = EncodeUtil.encodeURL(packageSign, "UTF-8");
//		return sign;
//	}
    

}
