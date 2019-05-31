package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay.wechat;

import com.e_commerce.miscroservice.commons.util.colligate.MD5;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.*;


@Component
public class WeChatPay {


    private DecimalFormat decimalFormat = new DecimalFormat("0");

    public static void main(String[] args) throws Exception {
        String orderNo = "sss";
        Double payMoney = 0.01;
		String openid = "";
        Map<String, Object> appParam = new WeChatPay().createWebParam(orderNo, payMoney, openid);
        System.out.println(appParam);
    }

    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return 返回微信服务器响应的信息
     */
    private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
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

    private static Map xmlToMap(String strXML) throws Exception {
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
    public Map<String, Object> createWebParam(String orderNo, Double payCount, String openid) throws Exception {
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("appid", ConstantUtil.APP_ID);
        param.put("partnerid", ConstantUtil.APP_ID);
        param.put("prepayid", createPay(orderNo, payCount).get("prepay_id"));
        param.put("package", "Sign=WXPay");
        param.put("noncestr", System.currentTimeMillis());
        String ten_time = String.valueOf(System.currentTimeMillis());
        param.put("timestamp", ten_time.substring(0, 10));
        param.put("openid", openid);
        setSign(param);
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


    private Map createPay(String orderNo, Double payCount) throws Exception {
        String output = getOrderRequestXml(orderNo, payCount);
        String json = httpsRequest(ConstantUtil.GATEURL, "POST", output);
        Map resultMap = xmlToMap(json);
        return resultMap;
    }

    private String getRequestXml(SortedMap<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<Map.Entry<String, Object>> es = params.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private String getOrderRequestXml(String orderNo, Double payCount) {
        SortedMap<String, Object> param = new TreeMap();
        param.put("appid", ConstantUtil.APP_ID);
        param.put("body", "商店支付");
        param.put("device_info", "WEB");
        param.put("mch_id", ConstantUtil.MCH_ID);
        param.put("nonce_str", System.currentTimeMillis() + "");
        param.put("notify_url", ConstantUtil.NOTIFY_URL);
        param.put("out_trade_no", orderNo);
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("total_fee", decimalFormat.format(payCount * 100));
        param.put("trade_type", "JSAPI");
        setSign(param);
        return getRequestXml(param);
    }

    /**
     * 统一下单设置签名的方式
     */
    private void setSign(SortedMap<String, Object> param) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entries : param.entrySet()) {
            sb.append(entries.getKey());
            sb.append("=");
            sb.append(entries.getValue());
            sb.append("&");

        }
        sb.append("key=");
        sb.append(ConstantUtil.APP_KEY);


        param.put("sign", MD5.crypt(sb.toString()));
    }

    public String getOpenid(String code) throws Exception {
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
		SortedMap<String, Object> requestParam = new TreeMap<>();
		requestParam.put("grant_type", "authorization_code");
		requestParam.put("appid", ConstantUtil.APP_ID);
		requestParam.put("secret", ConstantUtil.APP_KEY);
		requestParam.put("code", code);
		String requestXml = getRequestXml(requestParam);

		String xmlStr = httpsRequest(requestUrl, "GET", requestXml);
		Map resultMap = xmlToMap(xmlStr);
		return (String) resultMap.get("openid");
	}
}
