package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.CsqWechatConstant;
import com.e_commerce.miscroservice.csq_proj.dao.CsqOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
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
@Log
public class CsqWeChatPay {

	@Autowired
	private CsqOrderDao csqOrderDao;

	private DecimalFormat decimalFormat = new DecimalFormat("0");
	
	private static final  int MAX_CAP=1_000;
	private static Map<String, String> openIdCache = new ConcurrentHashMap<>(MAX_CAP);

	private String getOpenId(HttpServletRequest request) {

		String openid = request.getParameter("openid");

		String code = "";

		if (openid != null) {
			return openid;
		}

		code = request.getParameter("code");
		openid = openIdCache.get(code);

		if(openid == null) {
			StringBuilder params = new StringBuilder();
			params.append("secret=");
			params.append(CsqWechatConstant.APP_SECRET);
			params.append("&appid=");
			params.append(CsqWechatConstant.APP_ID);
			params.append("&grant_type=authorization_code");
	//		params.append("&code=");
			params.append("&js_code=");
			params.append(code);

	//		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
			String url = "https://api.weixin.qq.com/sns/jscode2session";
			String result = httpsRequest(
				url, "GET", params.toString());


			JSONObject jsonObject = JSONObject.parseObject(result);
			if (!jsonObject.containsKey("openid")) {
				return "";
			}
			openid = jsonObject.get("openid").toString();

			if(openIdCache.size()>=MAX_CAP){
				openIdCache.clear();
			}
			openIdCache.put(code,openid);
		}

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

	public Map parseXmlStr(String strXML) throws Exception {
		return xmlToMap(strXML);
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
		param.put("appid", CsqWechatConstant.APP_ID);
		param.put("partnerid", CsqWechatConstant.APP_ID);
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
	public Map<String, String>   createWebParam(String orderNo, Double payCount, HttpServletRequest request) throws Exception {
		return createWebParam(orderNo, payCount, request, null, false);
	}

	/*public Map<String, String> createWebParam(String orderNo, Double payCount, HttpServletRequest request, String attach) throws Exception {
		return createWebParam(orderNo, payCount, request, attach, ApplicationEnum.GUANZHAO_APPLICATION.toCode());
	}*/

	public Map<String, String> createWebParam(String orderNo, Double payCount, HttpServletRequest request, String attach, boolean isWeb) throws Exception {
		log.info("web params= orderNo{} payCount={} ",orderNo,payCount);
		SortedMap<String, String> param = new TreeMap<>();
		param.put("appId", CsqWechatConstant.APP_ID);
		String ten_time = String.valueOf(System.currentTimeMillis());
		param.put("timeStamp", ten_time.substring(0, 10));
		param.put("package", "prepay_id=" + createPay(orderNo, payCount, request, attach, isWeb).get("prepay_id"));
		param.put("nonceStr", ten_time);
		param.put("signType", "MD5");

		setSign(param, isWeb);

		return param;
	}

	public Map<String, String> createRefundWebParam(String orderNo, Double payCount, HttpServletRequest request, String attach) throws Exception {
		log.info("web params= orderNo{} payCount={} ",orderNo,payCount);
		createRefund(orderNo, payCount, request, attach);

		//一些返还给前端的参数(如果需要
		SortedMap<String, String> param = new TreeMap<>();
		/*param.put("appId", CsqWechatConstant.APP_ID);
		String ten_time = String.valueOf(System.currentTimeMillis());
		param.put("timeStamp", ten_time.substring(0, 10));
		param.put("package", "prepay_id=" + createRefund(orderNo, payCount, request, attach).get("prepay_id"));
		param.put("nonceStr", ten_time);
		param.put("signType", "MD5");

		setSign(param, true);*/

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
		return createPay(orderNo, payCount, request, null, false);
	}

	private Map<String, String> createPay(String orderNo, Double payCount, HttpServletRequest request, String attach, boolean isWeb) throws Exception {
		String openId = null;
		if (request != null) {
			openId = getOpenId(request);
		}
		String output = getOrderRequestXml(orderNo, payCount, openId, attach, isWeb);
		String json = httpsRequest(CsqWechatConstant.GATEURL, "POST", output);
		Map resultMap = xmlToMap(json);
		log.info("sing_info, msg={}",resultMap.toString());
		return resultMap;
	}

	private Map<String, String> createRefund(String orderNo, Double payCount, HttpServletRequest request, String attach) throws Exception {
		String openId = null;
		if (request != null) {
			openId = getOpenId(request);

		}
		String output = getRefundRequestXml(orderNo, payCount, payCount, attach);
		String json = httpsRequest(CsqWechatConstant.REFUND_URL, "POST", output);
		Map resultMap = xmlToMap(json);
		log.info("sing_info",resultMap);
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


		return sb.toString();
	}

	private String getOrderRequestXml(String orderNo, Double payCount, String openId, boolean isWeb) {
		return getOrderRequestXml(orderNo, payCount, openId, null, isWeb);
	}

	private String getRefundRequestXml(String orderNo, Double payCount, Double refundFee, String reFundDesc) {
		//构建请求参数Map
		SortedMap<String, String> param = new TreeMap<>();
		param.put("appid", CsqWechatConstant.APP_ID);
		param.put("mch_id", CsqWechatConstant.MCH_ID);
		param.put("nonce_str", System.currentTimeMillis() + "");
		param.put("notify_url", CsqWechatConstant.NOTIFY_URL_REFUD);
		param.put("out_trade_no", orderNo);
		String refundOrderNo = "RF" + orderNo;
		param.put("out_refund_no", refundOrderNo);
		param.put("total_fee", decimalFormat.format(payCount * 100));
		param.put("refund_fee", decimalFormat.format(refundFee * 100));

		setSign(param, false);

		return getRequestXml(param);
	}

	private String getOrderRequestXml(String orderNo, Double payCount, String openId, String attach, boolean isWeb) {
		String appid = CsqWechatConstant.APP_ID;
		String mchid = CsqWechatConstant.MCH_ID;
		String notify_url = CsqWechatConstant.NOTIFY_URL;

		SortedMap<String, String> param = new TreeMap();
		if(attach != null) {
			param.put("attach", attach);
		}
		param.put("appid", appid);
		param.put("body", "商店支付");
		param.put("device_info", "WEB");
		param.put("mch_id", mchid);
		param.put("nonce_str", System.currentTimeMillis() + "");
		param.put("notify_url", notify_url);
		log.info(notify_url);
		param.put("out_trade_no", orderNo);
		param.put("spbill_create_ip", "127.0.0.1");

		if (openId != null) {
			param.put("openid", openId);
			param.put("trade_type", "JSAPI");

		}else{
			param.put("trade_type", "APP");

		}

		param.put("total_fee", decimalFormat.format(payCount * 100));


		setSign(param, isWeb);
		return getRequestXml(param);
	}

	private void  setSign(SortedMap<String, String> param, boolean isWeb) {
		String appKey = CsqWechatConstant.APP_KEY;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entries : param.entrySet()) {
			sb.append(entries.getKey());
			sb.append("=");
			sb.append(entries.getValue());
			sb.append("&");
		}
		if (isWeb) {
			sb.append("mainKey=");
		} else {
			sb.append("key=");
		}
		sb.append(appKey);

		if (isWeb) {
			param.put("paySign", md5(sb.toString()));
		} else {
			param.put("sign", md5(sb.toString()));
		}

	}


	private String md5(String sourceStr) {

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
		System.out.println(new CsqWeChatPay().createAppParam(System.currentTimeMillis()+"",12D));
		System.out.println(new CsqWeChatPay().md5("appId=wxb8edf6df645eb4e5&nonceStr=1559657319465&package=prepay_id=wx04220839908978f523d2205e1705732200&signType=MD5&timeStamp=1559657319&mainKey=5uBcQ1wcsu8U46xEwgYxv68aRxqsRsLM"));
		;

	}
}
