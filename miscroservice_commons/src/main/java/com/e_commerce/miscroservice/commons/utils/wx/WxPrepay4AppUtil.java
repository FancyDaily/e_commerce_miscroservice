//package com.e_commerce.miscroservice.commons.utils.wx;
//
//import lombok.Data;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.xml.sax.InputSource;
//
//import java.io.StringReader;
//import java.util.*;
//
///**
// * app后台预支付接口
// *
// * @author Charlie
// * @version V1.0
// * @date 2018/9/12 19:55
// * @Copyright 玖远网络
// */
//public class WxPrepay4AppUtil{
//    private static final Logger LOGGER = LoggerFactory.getLogger (WxPrepay4AppUtil.class);
//
//
//    private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//
//    private static final String EQ = "=";
//    private static final String AND = "&";
//    private static final String PACKAGE = "Sign=WXPay";
//
//    /**
//     * resultMap = {
//     * nonce_str=cjxIgofi1sQBS5F7
//     * , appid=wx6ad169bccc57554a
//     * , sign=494E961E21C5F907C707FE3E44B51B52
//     * , trade_type=APP
//     * , return_msg=OK
//     * , result_code=SUCCESS
//     * , mch_id=1403320302
//     * , return_code=SUCCESS
//     * , prepay_id=wx15135505302236ad960b45620593988085
//     * }
//     */
//    @Data
//    public static class PayResultHelp{
//        private String msg;
//        private String code;
//        private String prepayId;
//        private boolean success;
//        private Map<String, Object> resultDetail;
//
//        private PayResultHelp(String msg, String code, String prepayId) {
//            this.msg = msg;
//            this.code = code;
//            this.prepayId = prepayId;
//            success = "SUCCESS".equals (code);
//        }
//
//        public static PayResultHelp instance(Map<String, String> resultMap) {
//            return new PayResultHelp (resultMap.get ("return_msg"), resultMap.get ("result_code"), resultMap.get ("prepay_id"));
//        }
//    }
//
//
//    /**
//     * 获取支付相关信息 <p>注意字符集的问题,要跟服务器适配!!!!!!!!!<p/>
//     *
//     * @param appId      appId
//     * @param body       body
//     * @param mchId      mchId
//     * @param tradeType  tradeType
//     * @param notifyUrl  notifyUrl
//     * @param outTradeNo outTradeNo
//     * @param ip         ip
//     * @param fee        fee
//     * @param apiKey     apiKey
//     * @return com.jiuyuan.service.WxPrepay4AppUtil.PayResultHelp
//     * @author Charlie
//     * @date 2018/9/15 14:42
//     */
//    public static PayResultHelp prepay4App(String appId, String body, String mchId, String tradeType, String notifyUrl, String outTradeNo, String ip, int fee, String apiKey) throws Exception {
//        //获取prepayId
//        String resultXml = doPrepay (appId, body, mchId, tradeType, notifyUrl, outTradeNo, ip, fee, apiKey);
//        //解析xml
//        Map<String, String> resultMap = doXMLParse (resultXml);
//        //是否成功
//        PayResultHelp resultHelp = PayResultHelp.instance (resultMap);
//        return signAgain (appId, mchId, apiKey, resultHelp);
//    }
//
//    /**
//     * 封装返回前端需要的参数
//     *
//     * @param appId      appId
//     * @param mchId      mchId
//     * @param apiKey     apiKey
//     * @param resultHelp resultHelp
//     * @return java.util.SortedMap<java.lang.String                                                               ,                                                               java.lang.Object>
//     * @author Charlie
//     * @date 2018/9/15 14:36
//     */
//    private static PayResultHelp signAgain(String appId, String mchId, String apiKey, PayResultHelp resultHelp) {
//        SortedMap<String, Object> params = new TreeMap<> ();
//        if (! resultHelp.isSuccess ()) {
//            LOGGER.info ("APP微信支付接口---获取prepayid失败");
//            return resultHelp;
//        }
//        params.put ("appid", appId);
//        params.put ("noncestr", nonceStr ());
//        params.put ("package", PACKAGE);
//        params.put ("partnerid", mchId);
//        params.put ("prepayid", resultHelp.getPrepayId ());
//        params.put ("timestamp", timestamp ());
//        params.put ("sign", genSign (params, apiKey));
//        resultHelp.setResultDetail (params);
//        return resultHelp;
//    }
//
//    private static String timestamp() {
//        return String.valueOf (System.currentTimeMillis () / 1000);
//    }
//
//    /**
//     * 预支付信息
//     * <p>
//     * 注意服务器字符集!!!这里没有对url做字符集的编码
//     * </p>
//     *
//     * @param appId      appId
//     * @param body       body
//     * @param mchId      mchId
//     * @param tradeType  tradeType
//     * @param notifyUrl  notifyUrl
//     * @param outTradeNo outTradeNo
//     * @param ip         ip
//     * @param fee        fee
//     * @param apiKey     apiKey
//     * @return 成功
//     * <xml>
//     * <return_code>
//     * <![CDATA[SUCCESS]]>
//     * </return_code>
//     * <return_msg>
//     * <![CDATA[OK]]>
//     * </return_msg>
//     * <appid>
//     * <![CDATA[wx6ad169bccc57554a]]>
//     * </appid><mch_id>
//     * <![CDATA[1403320302]]>
//     * </mch_id><nonce_str>
//     * <![CDATA[jyxvs0i1PrqeH6Nm]]>
//     * </nonce_str><sign>
//     * <![CDATA[021AB11DC22F00E955CE23EAD94FFF0D]]>
//     * </sign><result_code>
//     * <![CDATA[SUCCESS]]>
//     * </result_code><prepay_id>
//     * <![CDATA[wx15131550963088ad960b45620659438414]]>
//     * </prepay_id>
//     * <trade_type>
//     * <![CDATA[APP]]>
//     * </trade_type>
//     * </xml>
//     * <p>
//     * 失败:
//     * <xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[商户号mch_id或sub_mch_id不存在]]></return_msg></xml>
//     * @author Charlie
//     * @date 2018/9/15 13:16
//     */
//    private static String doPrepay(String appId, String body, String mchId, String tradeType, String notifyUrl, String outTradeNo, String ip, int fee, String apiKey) {
//        SortedMap<String, Object> params = new TreeMap<> ();
//        params.put ("appid", appId);
//        params.put ("body", body);
//        params.put ("mch_id", mchId);
//        params.put ("nonce_str", nonceStr ());
//        params.put ("notify_url", notifyUrl);
//        params.put ("out_trade_no", outTradeNo);
//        params.put ("spbill_create_ip", ip);
//        params.put ("total_fee", String.valueOf (fee));
//        params.put ("trade_type", tradeType);
//        params.put ("sign", genSign (params, apiKey));
//        String xmlParams = WxSupportUtil.toXml (params);
//        LOGGER.info ("APP微信支付接口---发送请求 url[{}].params[{}]", UNIFIEDORDER_URL, xmlParams);
//        String result = HttpClientUtils.post (UNIFIEDORDER_URL, xmlParams);
//        LOGGER.info ("APP微信支付接口---result[{}].", result);
//        return result;
//    }
//
//
//
//    /**
//     * 生成签名
//     */
//    private static String genSign(SortedMap<String, Object> source, String apiKey) {
//        StringBuilder sb = new StringBuilder ();
//        for (Map.Entry<String, Object> entry : source.entrySet ()) {
//            sb.append (entry.getKey ())
//                    .append (EQ)
//                    .append (String.valueOf (entry.getValue ()))
//                    .append (AND);
//        }
//        sb.append ("key=");
//        sb.append (apiKey);
//        return WxSupportUtil.MD5Encode (sb.toString (), "UTF-8").toUpperCase ();
//    }
//
//
//    private static String nonceStr() {
//        Random random = new Random ();
//        return DigestUtils.md5Hex (String.valueOf (random.nextInt (10000)));
//    }
//
//    /**
//     * 支付界面body
//     *
//     * @return java.lang.String
//     * @author Charlie
//     * @date 2018/9/15 13:35
//     */
//    private static String body() {
//        return "俞姐姐门店宝支付订单";
//    }
//
//
//    private static Map<String, String> doXMLParse(String xml) throws Exception {
//        //  Map retMap = new HashMap();
//        SortedMap<String, String> retMap = new TreeMap<String, String>();
//        try {
//            StringReader read = new StringReader(xml);
//            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
//            InputSource source = new InputSource(read);
//            // 创建一个新的SAXBuilder
//            SAXBuilder sb = new SAXBuilder();
//            // 通过输入源构造一个Document
//            Document doc = sb.build(source);
//            Element root = doc.getRootElement();// 指向根节点
//            List<Element> es = root.getChildren();
//            if (es != null && es.size() != 0) {
//                for (Element element : es) {
//                    retMap.put(element.getName(), element.getValue());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return retMap;
//    }
//
//
//
//    public static void main(String[] args) throws Exception {
//        String appId = "wx6ad169bccc57554a";
//        String body = body ();
//        String mchId = "1403320302";
//        String tradeType = "APP";
//        String notifyUrl = "http://storetest.yujiejie.com/pay/notify_url/weixin.do";
//        String outTradeNo = "SSS23214121";
//        String ip = "192.168.10.105";
//        int fee = 1;
//        String apiKey = "ebdd1da629156627139d0b5be22bee67";
//        PayResultHelp result = prepay4App (appId, body, mchId, tradeType, notifyUrl, outTradeNo, ip, fee, apiKey);
//        System.out.println ("result = " + result);
//    }
//}
