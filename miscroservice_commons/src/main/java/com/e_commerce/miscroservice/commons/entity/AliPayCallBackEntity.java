package com.e_commerce.miscroservice.commons.entity;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/12 19:28
 * @Copyright 玖远网络
 */
@Data
public class AliPayCallBackEntity {
    //        discount=0.00&payment_type=1&subject=测试&trade_no=2013082244524842&buyer_email=dlw***@gmail.com&gmt_create=2013-08-22 14:45:23
// &notify_type=trade_status_sync&quantity=1&out_trade_no=082215222612710&seller_id=2088501624816263&notify_time=2013-08-22 14:45:24&
// body=测试测试&trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee=1.00&gmt_payment=2013-08-22 14:45:24&seller_email=xxx@alipay.com&
// price=1.00&buyer_id=2088602315385429&notify_id=64ce1b6ab92d00ede0ee56ade98fdf2f4c&use_coupon=N&sign_type=RSA&
// sign=1glihU9DPWee+UJ82u3+mw3Bdnr9u01at0M/xJnPsGuHh+JA5bk3zbWaoWhU6GmLab3dIM4JNdktTcEUI9/FBGhgfLO39BKX/eBCFQ3bXAmIZn4l26fiwoO613BptT44GTEtnPiQ6+tnLsGlVSrFZaLB9FVhrGfipH2SWJcnwYs=
    /**
     * 折扣	 支付宝系统会把discount的值加到交易金额上，如果有折扣，本参数为负数，单位为元。
     */
    private String discount;
    /**
     * 签名
     */
    private String sign;
    /**
     * 商品单价 price等于total_fee（请求时使用的是total_fee）。
     */
    private String price;
    /**
     * 买家支付宝用户号 买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
     */
    private String buyer_id;
    /**
     * 通知校验ID
     */
    private String notify_id;
    /**
     * 是否使用红包买家 是否在交易过程中使用了红包。
     */
    private String use_coupon;
    /**
     * 签名方式 固定取值为RSA。
     */
    private String sign_type;
    /**
     * 支付类型 支付类型。默认值为：1（商品购买）。
     */
    private String payment_type;
    /**
     * 商品名称 商品的标题/交易标题/订单标题/订单关键字等。它在支付宝的交易明细中排在第一列，对于财务对账尤为重要。是请求时对应的参数，原样通知回来。
     */
    private String subject;
    /**
     * 支付宝交易号 该交易在支付宝系统中的交易流水号。最短16位，最长64位。
     */
    private String trade_no;
    /**
     * 买家支付宝账号 买家支付宝账号，可以是Email或手机号码。
     */
    private String buyer_email;
    /**
     * 交易创建时间 该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    private String gmt_create;
    /**
     *	通知类型	通知的类型。
     */
    private String notify_type;
    /**
     * 	购买数量	购买数量，固定取值为1（请求时使用的是total_fee）。
     */
    private String quantity;
    /**
     * 商户网站唯一订单号 对应商户网站的订单系统中的唯一订单号，非支付宝交易号。需保证在商户网站中的唯一性。是请求时对应的参数，原样返回。
     */
    private String out_trade_no;
    /**
     * 卖家支付宝用户号 卖家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
     */
    private String seller_id;
    /**
     * 通知时间 通知的发送时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    private String notify_time;
    /**
     * 商品描述 该笔订单的备注、描述、明细等。对应请求时的body参数，原样通知回来。
     */
    private String body;
    /**
     * 交易状态 交易状态，取值范围请参见“交易状态”。
     */
    private String trade_status;
    /**
     * 是否调整总价 该交易是否调整过价格。
     */
    private String is_total_fee_adjust;
    /**
     * 交易金额 该笔订单的总金额。请求时对应的参数，原样通知回来。
     */
    private String total_fee;
    /**
     * 交易付款时间 该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss。
     */
    private String gmt_payment;
    /**
     * 卖家支付宝账号 卖家支付宝账号，可以是email和手机号码。
     */
    private String seller_email;

}
