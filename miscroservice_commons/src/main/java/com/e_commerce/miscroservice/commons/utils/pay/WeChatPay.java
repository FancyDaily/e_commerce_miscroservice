package com.e_commerce.miscroservice.commons.utils.pay;

import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
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




    private static DecimalFormat decimalFormat = new DecimalFormat("0");

    public static void main(String[] args) throws Exception {
//        String orderNo = "1111111";
//        Double payMoney = 2.00;
//        Map pay = new WeChatPay().createAppParam(orderNo, payMoney);
//        System.out.println(pay);


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
    public static Map<String, Object> createAppParam(String remarks, String orderNo, Double payCount,String ip) throws Exception {
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("appId", WxPayStaticValue.APPID);
        param.put("partnerId", WxPayStaticValue.MCH_ID);
        param.put("prepayId", createPay(orderNo, payCount,remarks,ip).get("prepay_id"));
        param.put("packageValue", "Sign=WXPay");
        param.put("nonceStr", System.currentTimeMillis());
        String ten_time = String.valueOf(System.currentTimeMillis());
        param.put("timeStamp", ten_time.substring(0, 10));
        setSign(param, "sign");
        return param;


    }


    /**
     * 获取请求参数
     *
     * @return
     * @throws Exception
     */
//    public Map<String, Object> createWebParam(String orderNo, Double payCount) throws Exception {
//        SortedMap<String, Object> param = new TreeMap<>();
//        param.put("appId",APPID);
//        param.put("signType", "MD5");
//        param.put("package", "prepay_id=" + createPay(orderNo, payCount).get("prepay_id"));
//        param.put("nonceStr", System.currentTimeMillis());
//        String ten_time = String.valueOf(System.currentTimeMillis());
//        param.put("timeStamp", ten_time.substring(0, 10));
//        setSign(param, "paySign");
//        return param;
//
//
//    }


    public static Map doParseRquest(HttpServletRequest request) throws Exception {
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


    private static Map createPay(String orderNo, Double payCount,String remarks,String ip) throws Exception {
        String output = getOrderRequestXml(orderNo, payCount,remarks,ip);
        String json = httpsRequest(WxPayStaticValue.PLACEANORDER_URL, "POST", output);
        Map resultMap = xmlToMap(json);
        return resultMap;
    }

    private static String getRequestXml(SortedMap<String, Object> params) {
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

    private static String getOrderRequestXml(String orderNo, Double payCount, String remarks, String ip) {
        SortedMap<String, Object> param = new TreeMap();
        param.put("appid", WxPayStaticValue.APPID);
        param.put("body", remarks);
//        param.put("device_info", "WEB");
        param.put("mch_id", WxPayStaticValue.MCH_ID);
        param.put("nonce_str", System.currentTimeMillis() + "");
        param.put("notify_url", WxPayStaticValue.NOTIFY_WX_RECHAEGE_URL);
        param.put("out_trade_no", orderNo);
        param.put("spbill_create_ip", ip);
        param.put("total_fee", decimalFormat.format(payCount * 100));
//        param.put("openid", "oJ82J5XP-R3vAXvHWmn93XkOLxVw");
//        param.put("trade_type", "JSAPI");
        param.put("trade_type", "APP");
        setSign(param, "sign");
        return getRequestXml(param);
    }

    /**
     * 统一下单设置签名的方式
     */
    private static void setSign(SortedMap<String, Object> param, String sign) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entries : param.entrySet()) {
            sb.append(entries.getKey());
            sb.append("=");
            sb.append(entries.getValue());
            sb.append("&");

        }
        sb.append("key=");
        sb.append(WxPayStaticValue.KEY);


        param.put(sign, Md5Util.md5(sb.toString()));
    }


}
