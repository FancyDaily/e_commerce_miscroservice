package com.e_commerce.miscroservice.commons.utils.wx;



import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/15 19:05
 * @Copyright 玖远网络
 */
public class AliPrepayUtil{


    private static final String UTF8 = "UTF-8";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";


    /**
     *
     * @param totalFee 支付总额,内部会再处理,保留两位小数
     * @param partner 签约合作者身份ID
     * @param sellerId 签约卖家支付宝账号
     * @param outTradeNo 商户网站唯一订单号
     * @param subject 商品名称
     * @param body 商品详情
     * @param notifyUrl 服务器异步通知页面路径
     * @param returnUrl 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
     * @param rsaPrivateKey rsaPrivateKey
     * @param paymentType paymentType
     * @param charset charset
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/15 20:31
     */
    public static String prepay(Double totalFee, String partner, String sellerId, String outTradeNo, String subject, String body, String notifyUrl, String returnUrl, String rsaPrivateKey, String paymentType, String charset) {
        StringBuilder builder = new StringBuilder();
        // 签约合作者身份ID
        builder.append("partner=\"").append(partner).append("\"");
        // 签约卖家支付宝账号
        builder.append("&seller_id=\"").append(sellerId).append("\"");
        // 商户网站唯一订单号
        builder.append("&out_trade_no=\"").append(outTradeNo).append("\"");
        // 商品名称
        builder.append("&subject=\"").append(subject).append("\"");
        // 商品详情
        builder.append("&body=\"").append(body).append("\"");
        // 商品金额(保存两位)
        BigDecimal freeBig = new BigDecimal(String.valueOf (totalFee));
        totalFee = freeBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        builder.append("&total_fee=\"").append(totalFee).append("\"");
        // 服务器异步通知页面路径
        builder.append("&notify_url=\"").append(notifyUrl).append("\"");
        // 服务接口名称， 固定值
        builder.append("&service=\"mobile.securitypay.pay\"");
        // 支付类型， 固定值
        builder.append("&payment_type=\"").append(paymentType).append("\"");
        // 参数编码， 固定值
        builder.append("&_input_charset=\"").append(charset).append("\"");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        builder.append("&return_url=\"").append(returnUrl).append("\"");
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        // 对订单做RSA 签名
        String sign = sign(builder.toString(), rsaPrivateKey, UTF8);
        // 仅需对sign 做URL编码
        sign = encodeURL(sign);
        // 完整的符合支付宝参数规范的订单信息
        return builder.append("&sign=\"").append(sign).append("\"").append("&sign_type=\"RSA\"").toString();
    }


    /**
     * 签名
     *
     * @param content content
     * @param privateKey 私钥
     * @param charset 字符集
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/15 20:28
     */
    private static String sign(String content, String privateKey, String charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, AliPrepayUtil.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
