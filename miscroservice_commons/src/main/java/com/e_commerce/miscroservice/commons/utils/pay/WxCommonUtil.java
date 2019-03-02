package com.e_commerce.miscroservice.commons.utils.pay;

import com.e_commerce.miscroservice.commons.entity.proxy.PreOrderResult;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.StringUtil;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;

public class WxCommonUtil {


    private static Log logger = Log.getInstance(WxCommonUtil.class);



    /**
     * 描述 调用统一下单后，对返回参数进行封装。客户端唤起微信支付
     * @param preOrderResult  原始返回参数
     * @param payKey   秘钥
     * @author hyq
     * @date 2018/9/25 16:57
     * @return java.util.SortedMap<java.lang.Object,java.lang.Object>
     */
    public static SortedMap<Object, Object> getBrandWCPayRequest(PreOrderResult preOrderResult) {

        SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
        //公众号id
        signMap.put("appId", preOrderResult.getAppid());
        //时间戳  timeStamp	String	是	时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
        signMap.put("timeStamp", Sign.getTimeStamp());
        //随机字符串	nonceStr	String	是	随机字符串，长度为32个字符以下。
        signMap.put("nonceStr", preOrderResult.getNonce_str());
        //订单详情扩展字符串  packageValue	package	String	是	统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
        signMap.put("packageValue", "Sign=WXPay");
        signMap.put("prepayId",preOrderResult.getPrepay_id());
        signMap.put("partnerId",preOrderResult.getMch_id());
        //签名方式	signType	String	是	签名算法，暂支持 MD5
        signMap.put("signType", "MD5");
        //WX_PAY_KEY
        String paySign = Sign.createSign("utf-8",signMap,"a3798f6af930b3c1039199c338e83296");
        logger.info("生成的签名PaySIGN:"+paySign);
        //签名	paySign	String	是	签名,具体签名方案参见小程序支付接口文档;
        signMap.put("sign", paySign);
        signMap.put("errorMsg", preOrderResult.getErr_code_des());
        return signMap;
    }

    public static SortedMap<String, Object> genPayData4AppNew(String body, String out_trade_no, BigDecimal total_fee, String ip) {
        SortedMap<String, Object> result = new TreeMap<String, Object>();
        String prepayId = placeOrderApp(body,out_trade_no,total_fee,ip).getPrepay_id();
        logger.info("调用微信支付prepayId:"+prepayId);


        result.put("appId", WxPayStaticValue.APPID);
        result.put("partnerId", WxPayStaticValue.MCH_ID);
        result.put("prepayId", prepayId);
        String packageValue = "Sign=WXPay";
        result.put("packageValue", packageValue);
        String nonceStr =StringUtil.genNonceStr();
        result.put("nonceStr", nonceStr);
        long timeStamp = TimeUtils.genTimeStamp();
        result.put("timeStamp", timeStamp);

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", WxPayStaticValue.APPID));
        signParams.add(new BasicNameValuePair("noncestr", nonceStr));
        signParams.add(new BasicNameValuePair("package", packageValue));
        signParams.add(new BasicNameValuePair("partnerid", WxPayStaticValue.MCH_ID));
        signParams.add(new BasicNameValuePair("prepayid", prepayId));
        signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));

        String sign =Sign.genSign(signParams, WxPayStaticValue.KEY);
        result.put("sign", sign);

        return result;
    }
    public static PreOrderResult placeOrderApp(String body, String out_trade_no, BigDecimal total_fee, String ip) {
        String trade_type=WxPayStaticValue.TRADE_APP_TYPE;
        // 生成预付单对象
        // 生成随机字符串
        String nonce_str = StringUtil.genNonceStr();
//		String nonce_str = UUID.randomUUID().toString().trim().replaceAll("-", "");

        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
        packageParams.add(new BasicNameValuePair("appid",WxPayStaticValue.APPID));
        packageParams.add(new BasicNameValuePair("body", body));
        packageParams.add(new BasicNameValuePair("mch_id", WxPayStaticValue.MCH_ID));
        packageParams.add(new BasicNameValuePair("nonce_str",nonce_str));
        packageParams.add(new BasicNameValuePair("notify_url", WxPayStaticValue.NOTIFY_WX_RECHAEGE_URL));
        packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));
        packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
        packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(total_fee.multiply(new BigDecimal(100)).intValue())));
        packageParams.add(new BasicNameValuePair("trade_type", trade_type));
        packageParams.add(new BasicNameValuePair("sign",  Sign.genSign(packageParams,
                WxPayStaticValue.KEY)));

        // Object转换为XML
        String xml = null;
        try {
            xml =XmlUtil.toXml(packageParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 统一下单地址
        String url = WxPayStaticValue.PLACEANORDER_URL;

        // 调用微信统一下单地址
        logger.info("调用微信支付请求原始报文:"+xml);
        String returnXml = HttpUtil.post(url, xml);

        logger.info("调用微信支付返回原始报文:"+returnXml);

        // XML转换为Object
        PreOrderResult preOrderResult = null;
        try {
            preOrderResult = (PreOrderResult) XmlUtil.xml2Object(returnXml, PreOrderResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return preOrderResult;
    }
}
