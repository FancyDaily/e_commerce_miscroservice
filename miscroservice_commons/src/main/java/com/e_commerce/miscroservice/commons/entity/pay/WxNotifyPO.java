package com.e_commerce.miscroservice.commons.entity.pay;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/15 18:18
 * @Copyright 玖远网络
 */
@Data
public class WxNotifyPO {
//    private String appid;
//    private String mch_id;
//    private String nonce_str;
//    private String sign;
//    private String result_code;
//    private String err_code;
    private String out_trade_no;
    private String transaction_id;
    private String result_code;
}
