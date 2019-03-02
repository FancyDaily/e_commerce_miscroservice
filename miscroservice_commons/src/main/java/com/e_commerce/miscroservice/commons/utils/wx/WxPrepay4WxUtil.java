package com.e_commerce.miscroservice.commons.utils.wx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 小程序后台微信支付接口
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/12 19:55
 * @Copyright 玖远网络
 */
public class WxPrepay4WxUtil{

    private static final String SUCCESS = "SUCCESS";
    private static final String CHARACTER = "UTF-8";

    private static final Logger logger = LoggerFactory.getLogger (WxPrepay4WxUtil.class);


    private static final String SERVER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    /**
     * 获取支付信息
     *
     * @param orderNo orderNo
     * @param body body
     * @param totalFee totalFee
     * @param openId openId
     * @param businessName businessName
     * @param appId appId
     * @param mchId mchId
     * @param payKey payKey
     * @param userIp userIp
     * @param notifyUrl notifyUrl
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/14 13:00
     */
    private static String doAcquirePayInfo(String orderNo, String body, int totalFee, String openId, String businessName, String appId, String mchId, String payKey, String userIp, String notifyUrl) {
        WxPayRequestParam data = buildWxPaySenData (orderNo, body, totalFee, openId, businessName, appId, mchId, userIp, notifyUrl, "JSAPI");
        try {
            //生成sign签名
            SortedMap<Object, Object> parameters = new TreeMap<> ();
            parameters.put ("appid", data.getAppid ());
            parameters.put ("attach", data.getAttach ());
            parameters.put ("body", data.getBody ());
            parameters.put ("mch_id", data.getMch_id ());
            parameters.put ("nonce_str", data.getNonce_str ());
            parameters.put ("notify_url", data.getNotify_url ());
            parameters.put ("out_trade_no", data.getOut_trade_no ());
            parameters.put ("total_fee", data.getTotal_fee ());
            parameters.put ("trade_type", data.getTrade_type ());
            parameters.put ("spbill_create_ip", data.getSpbill_create_ip ());
            parameters.put ("openid", data.getOpenid ());
            parameters.put ("device_info", data.getDevice_info ());
            parameters.put ("time_start", data.getTime_start ());
            String sign = createSign (payKey, parameters);
            data.setSign (sign);
            logger.info ("=== 微信支付: 开始向微信请求支付签名 parameters[{}]." ,parameters);
            XStream xs = new XStream (new DomDriver ("UTF-8", new XmlFriendlyNameCoder ("-_", "_")));
            xs.alias ("xml", WxPayRequestParam.class);
            String xml = xs.toXML (data);
            return HttpClientUtils.post (SERVER_URL, xml);
        } catch (Exception e) {
            e.printStackTrace ();
            logger.error ("=== 微信支付: 调用统一下单方法错误", e.getMessage ());
        }
        return null;
    }

    private static String createSign(String payKey, SortedMap<Object, Object> param) {
        StringBuilder sb = new StringBuilder ();
        //所有参与传参的参数按照accsii排序（升序）
        Set set = param.entrySet ();
        for (Object elem : set) {
            Map.Entry entry = (Map.Entry) elem;
            String k = (String) entry.getKey ();
            Object v = entry.getValue ();
            if (null != v && ! "".equals (v) && ! "sign".equals (k) && ! "key".equals (k)) {
                sb.append (k).append ("=").append (v).append ("&");
            }
        }
        sb.append ("key=").append (payKey);
        return WxSupportUtil.MD5Encode (sb.toString (), CHARACTER).toUpperCase ();
    }

    private static WxPayRequestParam buildWxPaySenData(String orderNo, String body, int totalFee, String openId, String attach, String appId, String mchId, String ip, String notifyUrl, String tradeType) {
        WxPayRequestParam sendData = new WxPayRequestParam ();
        //WapPublicConstants.APPID
        sendData.setAppid (appId);
        sendData.setAttach (attach);
        sendData.setBody (body);
        //WapPublicConstants.MCH_ID
        sendData.setMch_id (mchId);
        sendData.setNonce_str (WxSupportUtil.MD5Encode (String.valueOf (new Random ().nextInt (10000)), "UTF-8"));
        //这里使用APP微信异步回调//WapPayConstants.PAY_NOTIFY_URL);
        sendData.setNotify_url (notifyUrl);
        sendData.setOut_trade_no (orderNo);
        //单位：分
        sendData.setTotal_fee (totalFee);
        sendData.setTrade_type (tradeType);
        sendData.setSpbill_create_ip (ip);
        sendData.setOpenid (openId);
        sendData.setTime_start (new SimpleDateFormat ("yyyyMMddHHmmss").format (new Date ()));
        return sendData;
    }


    private static WxPayResultData parseWxPayResultData(String result) {
        logger.info ("解析微信下单返回值 result: {}", result);
        XStream xstream = new XStream (new DomDriver ());
        xstream.alias ("xml", WxPayResultData.class);
        return (WxPayResultData) xstream.fromXML (result);
    }


    /**
     * 微信支付
     *
     * @param order 订单信息
     * @param user 用户信息
     * @param notifyUrl 支付后回调地址
     * @return com.jiuyuan.service.WxPrepay4WxUtil.ResultHelp
     * @author Charlie
     * @date 2018/9/14 13:00
     */
    private static ResultHelp acquirePayInfo(OrderInfo order, UserInfo user, String notifyUrl) {
        return acquirePayInfoInvoke (order.getOrderNo (), order.getProductName (), order.getFee (),
                user.getOpenId (), user.getBusinessName (), user.getAppId (), user.getMchId (), user.getPayKey (), user.getUserIp (),
                notifyUrl);
    }

    /**
     * 获取支付信息
     *
     * @param orderNo orderNo
     * @param body body
     * @param fee fee
     * @param openId openId
     * @param attch attch
     * @param appId appId
     * @param mchId mchId
     * @param payKey payKey
     * @param ip ip
     * @param notifyUrl notifyUrl
     * @return com.jiuyuan.service.WxPrepay4WxUtil.ResultHelp
     * @author Charlie
     * @date 2018/9/14 12:55
     */
    private static ResultHelp acquirePayInfoInvoke(String orderNo, String body, int fee, String openId, String attch, String appId, String mchId, String payKey, String ip, String notifyUrl) {
        logger.info ("=== 微信支付: orderNo = [" + orderNo + "], body = [" + body + "], fee = [" + fee + "], openId = [" + openId + "], attch = [" + attch + "], appId = [" + appId + "], mchId = [" + mchId + "], payKey = [" + payKey + "], ip = [" + ip + "], notifyUrl = [" + notifyUrl + "]");
        String payResponse = doAcquirePayInfo (orderNo, body, fee, openId, attch, appId, mchId, payKey, ip, notifyUrl);
        logger.info ("=== 微信支付: payResponse[{}] ", payResponse);
        WxPayResultData resultData = parseWxPayResultData (payResponse);
        logger.info ("=== 微信支付: WxPayResultData[{}]", resultData);
        if (SUCCESS.equals (resultData.getResult_code ()) && SUCCESS.equals (resultData.getReturn_code ())) {
            return ResultHelp.ok (buildSignData (resultData, payKey), resultData.getPrepay_id ());
        }
        else {
            return ResultHelp.error (payResponse);
        }
    }


    /**
     * 构建前端需要的支付信息
     *
     * @param resultData resultData
     * @param payKey payKey
     * @return
     *<p>
     *  公众号id ===> appId
     *</p>
     *<p>
     *  时间戳 		===> timeStamp	String	是	时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
     *</p>
     *<p>
     *  随机字符串	===> nonceStr	String	是	随机字符串，长度为32个字符以下。
     *</p>
     *<p>
     *  订单详情扩展字符串  ===> package	String	是	统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
     *</p>
     *<p>
     *  签名方式		===> signType	String	是	签名算法，暂支持 MD5
     *</p>
     *<p>
     *  签名	===> paySign	String	是	签名,具体签名方案参见小程序支付接口文档;
     *</p>
     * @author Charlie
     * @date 2018/9/14 13:01
     */
    private static Map<Object, Object> buildSignData(WxPayResultData resultData, String payKey) {
        SortedMap<Object, Object> signMap = new TreeMap<> ();
        //公众号id
        signMap.put ("appId", resultData.getAppid ());
        //时间戳 		timeStamp	String	是	时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
        signMap.put ("timeStamp", String.valueOf (System.currentTimeMillis () / 1000));
        //随机字符串	nonceStr	String	是	随机字符串，长度为32个字符以下。
        signMap.put ("nonceStr", resultData.getNonce_str ());
        //订单详情扩展字符串  packageValue	package	String	是	统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
        signMap.put ("package", "prepay_id=" + resultData.getPrepay_id ());
        //签名方式		signType	String	是	签名算法，暂支持 MD5
        signMap.put ("signType", "MD5");
        //WapPublicConstants.WX_PAY_KEY
        String paySign = createSign (payKey, signMap);
        //签名	paySign	String	是	签名,具体签名方案参见小程序支付接口文档;
        signMap.put ("paySign", paySign);
        return signMap;
    }


    //=================================================================
    @Data
    @AllArgsConstructor
    public static class OrderInfo{
        /**
         * 订单号
         */
        private String orderNo;
        /**
         * 商品名
         */
        private String productName;
        /**
         * 金额分
         */
        private int fee;
    }

    @Data
    @AllArgsConstructor
    public static class UserInfo{
        private String openId;
        /**
         * 商户名
         */
        private String businessName;
        private String appId;
        /**
         * 商品id
         */
        private String mchId;
        /**
         * key
         */
        private String payKey;
        /**
         * 用户真实IP
         */
        private String userIp;
    }


    /**
     * 接收请求返回值
     */
    @Data
    public static class ResultHelp{
        /**
         * 支付签名信息
         */
        private Map<Object, Object> signMap;
        /**
         * 微信支付id
         */
        private String prepayId;
        private boolean isOk;
        private String errorMsg;

        ResultHelp(Map<Object, Object> signMap, String prepayId) {
            this.signMap = signMap;
            this.prepayId = prepayId;
        }

        private static ResultHelp ok(Map<Object, Object> signMap, String prepayId) {
            ResultHelp instance = new ResultHelp (signMap, prepayId);
            instance.setOk (true);
            return instance;
        }


        private static ResultHelp error(String errorMsg) {
            ResultHelp instance = new ResultHelp (new HashMap<> (), null);
            instance.setOk (false);
            instance.setErrorMsg (errorMsg);
            return instance;
        }
    }


    /**
     * 请求参数的封装
     */
    @Data
    static class WxPayRequestParam{
        /**
         * 微信分配的公众账号ID（企业号corpid即为此appId）  小程序ID	appid	是	String(32)	wxd678efh567hg6787	微信分配的小程序ID
         */
        private String appid;
        /**
         * 微信支付分配的商户号 	商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
         */
        private String mch_id;

        /**
         * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"	设备号	device_info	否	String(32)	013467007045764	终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
         */
        private String device_info;
        /**
         * 随机字符串，不长于32位	随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
         */
        private String nonce_str;
        /**
         * 签名	签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
         */
        private String sign;
        /**
         * 签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
         */
        private String sign_type;
        /**
         * 商品描述 商品或支付单简要描述	商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值	 商品简单描述，该字段须严格按照规范传递，具体请见参数规定
         */
        private String body;
        /**
         * 商品名称明细列表	商品详情	detail	否	String(6000)		单品优惠字段(暂未上线)
         */
        private String detail;
        /**
         * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据	附加数据	attach	否	String(127)	深圳分店	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
         */
        private String attach;
        /**
         * 商户系统内部的订单号,32个字符内、可包含字母	商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
         */
        private String out_trade_no;
        /**
         * 符合ISO 4217标准的三位字母代码，默认人民币：CNY		货币类型	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
         */
        private String fee_type;
        /**
         * 订单总金额	总金额	total_fee	是	Int	888	订单总金额，单位为分，详见支付金额
         */
        private Integer total_fee;
        /**
         * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。	终端IP	spbill_create_ip	是	String(16)	123.12.12.123	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
         */
        private String spbill_create_ip;
        /**
         * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010		交易起始时间	time_start	否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
         */
        private String time_start;
        /**
         * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010		交易结束时间	time_expire	否	String(14)	20091227091010 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则 注意：最短失效时间间隔必须大于5分钟
         */
        private String time_expire;
        /**
         * 商品标记，代金券或立减优惠功能的参数	商品标记	goods_tag	否	String(32)	WXG	商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
         * c
         */
        private String goods_tag;
        /**
         * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。	通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
         */
        private String notify_url;
        /**
         * 交易类型,取值如下：JSAPI，NATIVE，APP	交易类型	trade_type	是	String(16)	JSAPI	小程序取值如下：JSAPI，详细说明见参数规定
         */
        private String trade_type;
        /**
         * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
         */
        private String product_id;
        /**
         * no_credit--指定不能使用信用卡支付	指定支付方式	limit_pay	否	String(32)	no_credit	no_credit--指定不能使用信用卡支付
         */
        private String limit_pay;
        /**
         * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识		用户标识	openid	否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
         */
        private String openid;
    }


    @Data
    public class WxPayResultData{

        /**
         * 返回状态码
         */
        private String return_code;
        /**
         * 返回信息
         */
        private String return_msg;
        /**
         * 公众账号ID（调用接口提交的公众账号ID）	小程序ID	appid	是	String(32)	wx8888888888888888	调用接口提交的小程序ID
         */
        private String appid;
        /**
         * 商户号（调用接口提交的商户号）	商户号	mch_id	是	String(32)	1900000109	调用接口提交的商户号
         */
        private String mch_id;
        /**
         * 设备号	device_info	否	String(32)	013467007045764	调用接口提交的终端设备号，
         */
        private String device_info;
        /**
         * 随机字符串（微信返回的随机字符串）	随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	微信返回的随机字符串
         */
        private String nonce_str;
        /**
         * 签名（微信返回的签名值）签名算法官方文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
         * 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	微信返回的签名，详见签名算法
         */
        private String sign;
        /**
         * 业务结果（SUCCESS/FAIL）
         * 业务结果	result_code	是	String(16)	SUCCESS	SUCCESS/FAIL
         */
        private String result_code;
        /**
         * 错误代码
         * 错误代码	err_code	否	String(32)	SYSTEMERROR	详细参见第6节错误列表
         * 错误代码描述	err_code_des	否	String(128)	系统错误	错误返回的信息描述
         * <p>
         * 错误码
         * 名称	描述	原因	解决方案
         * NOAUTH	商户无此接口权限	商户未开通此接口权限	请商户前往申请此接口权限
         * NOTENOUGH	余额不足	用户帐号余额不足	用户帐号余额不足，请用户充值或更换支付卡后再支付
         * ORDERPAID	商户订单已支付	商户订单已支付，无需重复操作	商户订单已支付，无需更多操作
         * ORDERCLOSED	订单已关闭	当前订单已关闭，无法支付	当前订单已关闭，请重新下单
         * SYSTEMERROR	系统错误	系统超时	系统异常，请用相同参数重新调用
         * APPID_NOT_EXIST	APPID不存在	参数中缺少APPID	请检查APPID是否正确
         * MCHID_NOT_EXIST	MCHID不存在	参数中缺少MCHID	请检查MCHID是否正确
         * APPID_MCHID_NOT_MATCH	appid和mch_id不匹配	appid和mch_id不匹配	请确认appid和mch_id是否匹配
         * LACK_PARAMS	缺少参数	缺少必要的请求参数	请检查参数是否齐全
         * OUT_TRADE_NO_USED	商户订单号重复	同一笔交易不能多次提交	请核实商户订单号是否重复提交
         * SIGNERROR	签名错误	参数签名结果不正确	请检查签名参数和方法是否都符合签名算法要求
         * XML_FORMAT_ERROR	XML格式错误	XML格式错误	请检查XML参数格式是否正确
         * REQUIRE_POST_METHOD	请使用post方法	未使用post传递参数 	请检查请求参数是否通过post方法提交
         * POST_DATA_EMPTY	post数据为空	post数据不能为空	请检查post数据是否为空
         * NOT_UTF8	编码格式错误	未使用指定编码格式	请使用UTF-8编码格式
         */
        private String err_code;
        /**
         * 错误代码描述
         */
        private String err_code_des;
        /**
         * 交易类型
         */
        private String trade_type;
        /**
         * 微信支付交易会话标识
         */
        private String prepay_id;
        /**
         * 二维码链接 trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
         */
        private String code_url;
    }
    //==================================================================


    public static void main(String[] args) {

        // 微信Uid
        String openId = "o01of0ZqovVlXCPMi03oo-Hz15vU";
        // 商户号
        String mchId = "1459044102";
        // 商户密钥
        String payKey = "NnZ7chTcyBgGNPM3Yk2cESzmes53gqDV";
        String appId = "wxf99f985dc7f79695";
        ResultHelp resultHelp = acquirePayInfo (
                new OrderInfo ("sss0025840932", "商品名称", 100),
                new UserInfo (openId, "businessName", appId, mchId, payKey, "192.168.10.105"),
                "http://192.168.10.105:8085/product/getsign"
        );
        System.out.println ("resultHelp = " + resultHelp);
    }
}
