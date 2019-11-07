package com.e_commerce.miscroservice.csq_proj.yunma_api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 正元校园支付API工具类
 * @Author: FangyiXu
 * @Date: 2019-11-01 14:27
 */
@Log
public class YunmaPayUtil {

	/**商户编码，由云马提供。每一个收款账号对应一个商户编码*/
	private static final String CP_CODE  = "yiyun";

	/**签名密钥，由云马提供，用于对请求的数据进行签名加密*/
	private static final String SIGN_KEY = "123456";

	/**应用id，由云马提供*/
	private static final String APP_ID = YunmaConstant.APP_ID;

	public static Map<String, String> preOrder(String orderNo, Double tranMoney, String prodName, String tranType, String prodDes, String notifyUrl, String clientIp, String returnUrl, String yunmaUserId) {
		log.info("预下单preOrder, orderNo={}, tranMoney={}, proName={}, tranType={}, prodDes={}, notifyUrl={}, clientIp={}, returnUrl={}, yunmaUserId={}", orderNo, tranMoney, prodName, tranType, prodDes, notifyUrl, clientIp, returnUrl, yunmaUserId);
		Map<String, String> paramMap = new HashMap<>();
		//商户编码 必须
		paramMap.put("cp_code", CP_CODE);
		//商户订单号 必须
		paramMap.put("cp_tran_no", orderNo);
		//金额,单位为分 必须
		int money = (int) (tranMoney * 100);
		paramMap.put("tran_money", String.valueOf(money));
		//商品名字 必须
		prodName = StringUtil.makeNoNull(prodName);
		paramMap.put("prod_name", prodName);
		//业务类型 必须
		paramMap.put("tran_type", tranType);
		//商品描述
		prodDes = StringUtil.makeNoNull(prodDes);
		paramMap.put("prod_des", prodDes);
		//异步通知地址 必须
		paramMap.put("notify_url", notifyUrl);
		//客户端ip
		paramMap.put("client_ip", clientIp);
		//商户订单提交时间 必须 yyyyMMddHHmmss
		paramMap.put("time", String.valueOf(System.currentTimeMillis() / 1000));
//		paramMap.put("return_url", "http://unified.lsmart.wang/back");
		paramMap.put("return_url", returnUrl);
		paramMap.put("app_id", APP_ID);//云马分配

		//userId 和 accountType & account 必填其中一种
		paramMap.put("userId", yunmaUserId);
//		paramMap.put("account", "9");
//		paramMap.put("account_type", "1");
		String sign = getSign(paramMap, SIGN_KEY);
//		System.out.println(sign);
		log.info("sign={}", sign);
		//签名 必须
		paramMap.put("sign", sign);
		String response = sendPost("https://unifiedpay.lsmart.wang/pay/unified/preOrder.shtml", paramMap);

		JSONObject jsonObject = JSON.parseObject(response);
		HashMap<String, String> map = new HashMap<>();
		if ("0".equals(jsonObject.getString("ret_code"))) {
			String tran_no = jsonObject.getString("tran_no");
			map.put("tran_no", tran_no);
			System.out.println("预下单成功，订单号：" + tran_no);
		} else {
			System.out.println("预下单失败，错误原因：" + jsonObject.getString("ret_msg"));
		}
		return map;
	}

	public static void main(String[] args) {
		preOrderDemo();
	}

	/**
	 * 预下单
	 */
	private static void preOrderDemo() {
		Map<String, String> paramMap = new HashMap<>();
		//商户编码 必须
		paramMap.put("cp_code", CP_CODE);
		//商户订单号 必须
		paramMap.put("cp_tran_no", "2"); // 商户放订单号，同一个订单号只能支付一次，测试时请注意每次修改
		//金额,单位为分 必须
		paramMap.put("tran_money", "1");
		//商品名字 必须
		paramMap.put("prod_name", "测试商品");
		//业务类型 必须
		paramMap.put("tran_type", "9999");
		//商品描述
		paramMap.put("prod_des", "测试商品描述");
		//异步通知地址 必须
		paramMap.put("notify_url", YunmaConstant.NOTIFY_URL);
		//客户端ip
		paramMap.put("client_ip", "127.0.0.1");
		//商户订单提交时间 必须 yyyyMMddHHmmss
		paramMap.put("time", String.valueOf(System.currentTimeMillis() / 1000));
		paramMap.put("return_url", "http://unified.lsmart.wang/back");
		paramMap.put("app_id", APP_ID);//云马分配

		//userId 和 accountType & account 必填其中一种
		paramMap.put("userId", "123");
//		paramMap.put("account", "9");
//		paramMap.put("account_type", "1");
		String sign = getSign(paramMap, SIGN_KEY);
		System.out.println(sign);
		//签名 必须
		paramMap.put("sign", sign);
		String response = sendPost("https://unifiedpay.lsmart.wang/pay/unified/preOrder.shtml", paramMap);

		JSONObject jsonObject = JSON.parseObject(response);
		if ("0".equals(jsonObject.getString("ret_code"))) {
			System.out.println("预下单成功，订单号：" + jsonObject.getString("tran_no"));
		} else {
			System.out.println("预下单失败，错误原因：" + jsonObject.getString("ret_msg"));
		}
	}

	/**
	 * 获取签名
	 *
	 * @param map 传递的参数，请勿加入value为null的值
	 * @param signKey 签名密钥
	 * @return
	 * @history
	 */
	public static String getSign(Map<String, String> map, String signKey) {
		// 根据KEY排序
		Map<String, String> sortedParamMap = new TreeMap<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals("sign") || entry.getKey().startsWith("t_") || entry.getValue() == null) {
				// 略过sign和以t_开头的参数
				continue;
			}
			sortedParamMap.put(entry.getKey(), urlDecoder(entry.getValue()));
		}
		// 最终结果
		List<String> allList = new LinkedList<String>();
		for (String value : sortedParamMap.values()) {
			if (StringUtils.isNotBlank(value)) {
				allList.add(value);
			}
		}
		// 添加签名key
		allList.add(signKey);
		String params = StringUtils.join(allList.iterator(), "|");
		String signOne = msgMd5(params);
		String secondParams = new StringBuilder().append(signOne).append("|").append(signKey).toString();
		String signTwo = msgMd5(secondParams);
		return signTwo;
	}

	/**
	 * url解码
	 * @param url
	 * @return
	 * @history
	 */
	public static String urlDecoder(String url) {
		try {
			return URLDecoder.decode(url, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/****
	 *
	 * sendPost:发送post请求
	 *
	 * @date 2018年1月29日 上午11:39:45
	 * @param url 地址
	 * @param map 参数
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> map) {
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		StringEntity stringEntity = new UrlEncodedFormEntity(formparams, Charsets.UTF_8);

		httppost.setEntity(stringEntity);

		CloseableHttpClient httpsClient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		try {
			long start = System.currentTimeMillis();
			response = httpsClient.execute(httppost);
			Long different = System.currentTimeMillis() - start;
			if (different > 2000) {
			}
			if (response == null) {
				return null;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			log.info("statusCode={}", statusCode);
			if (statusCode != 200) {
				throw new RuntimeException("http请求返还非200错误");
			}
			String result = null;
			try {
				result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
			} catch (ParseException | IOException e) {
				throw new RuntimeException("转字符串异常", e);
			}

			return result;
		} catch ( SocketTimeoutException | UnknownHostException e) {
			throw new RuntimeException("http请求超时", e);
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("http请求异常", e);
		} finally {
			IOUtils.closeQuietly(response);
		}
	}

	/**
	 * 通用md5
	 * @param s
	 * @return
	 */
	public static String msgMd5(String s) {
		String backString = "";
		try {
			char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			backString = new String(str); // 换后的结果转换为字符串
			return backString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> deCode(String code) {
		log.info("解析code, code={}", code);
		Map<String, String> paramMap = new HashMap<>();
		//商户编码 必须
		paramMap.put("cp_code", CP_CODE);
		paramMap.put("app_id", APP_ID);
		paramMap.put("code", code);
		String sign = getSign(paramMap, SIGN_KEY);
		log.info("sign={}", sign);
		//签名 必须
		paramMap.put("sign", sign);
		String response = sendPost("http://auth.xiaofubao.com/oauth2/access_token", paramMap);

		JSONObject jsonObject = JSON.parseObject(response);
		HashMap<String, String> map = new HashMap<>();
		if ("0".equals(jsonObject.getString("ret_code"))) {
			String accessToken = jsonObject.getString("access_token");
			String userId = jsonObject.getString("user_id");
			map.put("accessToken", accessToken);
			map.put("userId", userId);
			log.info("解析成功，accessToken={}, userId={}" + accessToken, userId);
		} else {
			log.info("解析失败，错误原因：" + jsonObject.getString("ret_msg"));
		}
		return map;
	}

	public static HashMap<String, String> infos(String accessToken, String userId) {
		log.info("解析用户信息, accessToken={}, userId={}", accessToken, userId);
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("app_id", APP_ID);
		paramMap.put("user_id", userId);
		paramMap.put("access_token", accessToken);

		String sign = getSign(paramMap, SIGN_KEY);
		log.info("sign={}", sign);
		//签名 必须
		paramMap.put("sign", sign);
		String response = sendPost("http://auth.xiaofubao.com/oauth2/userinfo", paramMap);

		JSONObject jsonObject = JSON.parseObject(response);
		HashMap<String, String> map = new HashMap<>();
		if ("0".equals(jsonObject.getString("ret_code"))) {
			userId = jsonObject.getString("user_id");
			String userName = jsonObject.getString("user_name");
			String certNo = jsonObject.getString("cert_no");
			String telephone = jsonObject.getString("mobile_no");
			map.put("userId", userId);
			map.put("userName", userName);
			map.put("certNo", certNo);
			map.put("telephone", telephone);
			log.info("解析成功，accessToken={}, userId={}" + accessToken, userId);
		} else {
			log.info("解析失败，错误原因：" + jsonObject.getString("ret_msg"));
		}

		return map;
	}
}
