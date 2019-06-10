package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class WeChatPay {


	private DecimalFormat decimalFormat = new DecimalFormat("0");

	private static final  int MAX_CAP=1_000;
	private static Map<String, String> openIdCache = new ConcurrentHashMap<>(MAX_CAP);

	private String getOpenId(HttpServletRequest request) {

		String code = request.getParameter("code");

		String openid = openIdCache.get(code);

		if (openid != null) {
			return openid;
		}
		StringBuilder params = new StringBuilder();
		params.append("secret=");
		params.append(ConstantUtil.APP_SECRET);
		params.append("&appid=");
		params.append(ConstantUtil.APP_ID);
		params.append("&grant_type=authorization_code");
		params.append("&code=");
		params.append(code);

		String result = httpsRequest(
			"https://api.weixin.qq.com/sns/oauth2/access_token", "GET", params.toString());


		JSONObject jsonObject = JSONObject.parseObject(result);
		if (!jsonObject.containsKey("openid")) {
			return "";
		}
		openid = jsonObject.get("openid").toString();

		if(openIdCache.size()>=MAX_CAP){
			openIdCache.clear();
		}
		openIdCache.put(code,openid);

		return openid;

	}


	private String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
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

	private Map xmlToMap(String strXML) throws Exception {
		try {
			Map data = new HashMap();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(), element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
			}
			return data;
		} catch (Exception ex) {
			throw ex;
		}
	}


	/**
	 * 获取请求参数
	 *
	 * @param orderNo  订单号
	 * @param payCount 支付金额
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createAppParam(String orderNo, Double payCount) throws Exception {
		SortedMap<String, String> param = new TreeMap<>();
		param.put("appid", ConstantUtil.APP_ID);
		param.put("partnerid", ConstantUtil.APP_ID);
		param.put("prepayid", createPay(orderNo, payCount, null).get("prepay_id"));
		param.put("package", "Sign=WXPay");
		String ten_time = String.valueOf(System.currentTimeMillis());
		param.put("noncestr", ten_time);
		param.put("timestamp", ten_time.substring(0, 10));
		setSign(param, false);
		return param;


	}

	/**
	 * 获取请求参数
	 *
	 * @param orderNo  订单号
	 * @param payCount 支付金额
	 * @param request  请求参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createWebParam(String orderNo, Double payCount, HttpServletRequest request) throws Exception {
		SortedMap<String, String> param = new TreeMap<>();
		param.put("appId", ConstantUtil.APP_ID);
		String ten_time = String.valueOf(System.currentTimeMillis());
		param.put("timeStamp", ten_time.substring(0, 10));
		param.put("package", "prepay_id=" + createPay(orderNo, payCount, request).get("prepay_id"));
		param.put("nonceStr", ten_time);
		param.put("signType", "MD5");


		setSign(param, true);

		return param;


	}


	public Map doParseRquest(HttpServletRequest request) throws Exception {
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = request.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();
		return xmlToMap(sb.toString());

	}


	private Map<String, String> createPay(String orderNo, Double payCount, HttpServletRequest request) throws Exception {
		String openId = null;
		if (request != null) {
			openId = getOpenId(request);

		}
		String output = getOrderRequestXml(orderNo, payCount, openId);
		String json = httpsRequest(ConstantUtil.GATEURL, "POST", output);
		Map resultMap = xmlToMap(json);
		System.out.println(resultMap);
		return resultMap;
	}


	private String getRequestXml(SortedMap<String, String> params) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<Map.Entry<String, String>> es = params.entrySet();
		Iterator<Map.Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");

		System.out.println(sb.toString());
		return sb.toString();
	}

	private String getOrderRequestXml(String orderNo, Double payCount, String openId) {
		SortedMap<String, String> param = new TreeMap();
		param.put("appid", ConstantUtil.APP_ID);
		param.put("body", "商店支付");
		param.put("device_info", "WEB");
		param.put("mch_id", ConstantUtil.MCH_ID);
		param.put("nonce_str", System.currentTimeMillis() + "");
		param.put("notify_url", ConstantUtil.NOTIFY_URL);
		param.put("out_trade_no", orderNo);
		param.put("spbill_create_ip", "127.0.0.1");

		if (openId != null) {
			param.put("openid", openId);
			param.put("trade_type", "JSAPI");

		}else{
			param.put("trade_type", "APP");

		}

		param.put("total_fee", decimalFormat.format(payCount * 100));


		setSign(param, false);


		return getRequestXml(param);
	}


	private void setSign(SortedMap<String, String> param, boolean isWeb) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entries : param.entrySet()) {
			sb.append(entries.getKey());
			sb.append("=");
			sb.append(entries.getValue());
			sb.append("&");

		}
		sb.append("key=");
		sb.append(ConstantUtil.APP_KEY);


		if (isWeb) {
			param.put("paySign", md5(sb.toString()));
		} else {
			param.put("sign", md5(sb.toString()));
		}

	}


	private String md5(String sourceStr) {
		System.out.println(sourceStr);
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {

					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return result.toUpperCase();
	}


	public static void main(String[] args) throws Exception {
		System.out.println(new WeChatPay().createAppParam(System.currentTimeMillis()+"",12D));
		System.out.println(new WeChatPay().md5("appId=wxb8edf6df645eb4e5&nonceStr=1559657319465&package=prepay_id=wx04220839908978f523d2205e1705732200&signType=MD5&timeStamp=1559657319&key=5uBcQ1wcsu8U46xEwgYxv68aRxqsRsLM"));
		;

	}
}
