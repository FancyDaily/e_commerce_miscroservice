package com.e_commerce.miscroservice.commons.util.colligate.pay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.e_commerce.miscroservice.commons.entity.colligate.AliPayPo;

public class AliPayUtil {

    private static String APIURL = "https://openapi.alipay.com/gateway.do";

    private static String APPID = "2018091261395191";

    private static String PRIVATEKEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCZtpyNQIOebbXdQvAW49V2r8CxrtsGK+PQKrGGPQDnX1I/C3rG72rz6wL6H2u+EX6kSzrveg61ss/LxpzCd0inKWmHrmt3Zgu4KiD6Yuy/jaOI00vKltyA79X5AfkAWEkkcAoCVVOmqNHAvjsbEulDQlyew8ak2ZtzEANir7le/v/pw+EiVj9+/m8W3L8US5YW3/eqv/2WkwC6VvFCgEbiY+XOo/jcvHxnFb9lfQn778dfuob2MRc4mkTqaUUrgEdrp9B+JgSXLnk+lBdh9lgubzv8RthzvbjUb8lOV/FM63cJvS3qNCuQEKw5w6OZZA9HdWtrV56kpUenouiZZl0dAgMBAAECggEANP2YMLXZ6gcGWDXYUPvQPi9OrbKK/TCqQ7xEIPPs2NaqouNROz2UUGxnRVUJrqeXUgpBUgdxBMCVFDnvFrdKi54iLE077Rh319BmOAtcdJjelK/LBBdIAwcra8F7VtwYswAQ40cJJH40eU7bT3UQh0hcZ7s0QuZlyZ/umtnbfNzBEpH0paw482j1ppb3iVOQcU/T0OtIr6ulfZMHPYXue5E26eo9FlUBbTKV442VkEA7y62ZTEvCwhUzz5uJ+LWWn6qsW6PlH+UPubYzoRyaQndj7faN/YfaaKWiIs/x4IsTZB+Uazp9+9apdUtaZXw1Tm1ahClkzkaR3cUCoXo7XQKBgQDIl/3p1353T00XVjZlXm2/dtdYh/1INgrC8tMSB8kvb6CLMU+ytwE8FXPdjb8mZ67HfXrYWOV6V+MlSzkXMtGTR5lvijHplLyW3IHfGcfiZMH0ixQpb+9W4Pp0f6Od8tmn41sY6lh3z/PE5Bc+mytQkG+nrOTkNNoPsdIF9BkMuwKBgQDEK7N1yKysCHdOFL22odUSIvt57YbUNYcJ1fewCoLsgnuIcl8yb7ixdL8u24sotgijj20qLffaln+DMGECZrAvK1mU+EGlVClZEOLDEjWmqzBz8jTkymH67KXgIK7PmfcdPZPxgHQ/jEFUSyNFW+Bayp4ZNRhxlR0bD/3o85bMBwKBgFWOxqVj3bQj5dzLT95CvYrJtKY1MqoHKuXlznNFUOFjulVEThsLBYLMH6RjHiHSXXEHK0t190MMk3zHre0gJtWr110E068uWpX6LYasJ15/3V5hp9LYxHwI76qa+n2XJJEAX02sPtmzn+Td6LAyXPI6+PTKwMI5bLz++nbBm6GbAoGAcJJDYYvOysnw7LWqSGuFweqhNM7BWHV/EH3grPzbNK2MBjjbuC20ZuD/9Pu3V51eR72+fz3xxVYnxji08pPK9saBmaL8yMsLDzlJZ3fDoekK3P5YrBIXN88lQQCAUFGiJP15MFTuYKkztBOKIfsfO/qZzw1RAZz4HteGdW+TUXECgYACU8DPKztcQe4sX2swDuiT3MtbAgjYMJ5W0k4k4ps1XC0ij6pwHJPCsBYt+ZhqTm0xL3cHgxC8y88qA0FNC49A+yPxlmH5V1Q+x3DiqNdSwCotOWIRrbLLyH77vL80Dv0ctPZVJbpRs0oam3iVuS954Wnr3RW4SL1NdAtzL2o63Q==";
    private static String PUBLICKEY  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGPgmxK5YV0/fH939lHFxg6lgTqByC9Hom3re4gnphpYFtWKLTvTo8b3YTUMKlmRUsF63MtUrrFfjND9s43XzIiEKj7R12qye8X76/RZfbmvYHF7JVE14KnucBOnQ+hlqf7PskJbQa7+1n3IjknY8iRBHrnGKSDW9iTmCjeHiexwIDAQAB";

    private static String ALIPUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmbacjUCDnm213ULwFuPVdq/Asa7bBivj0Cqxhj0A519SPwt6xu9q8+sC+h9rvhF+pEs673oOtbLPy8acwndIpylph65rd2YLuCog+mLsv42jiNNLypbcgO/V+QH5AFhJJHAKAlVTpqjRwL47GxLpQ0JcnsPGpNmbcxADYq+5Xv7/6cPhIlY/fv5vFty/FEuWFt/3qr/9lpMAulbxQoBG4mPlzqP43Lx8ZxW/ZX0J++/HX7qG9jEXOJpE6mlFK4BHa6fQfiYEly55PpQXYfZYLm87/EbYc7241G/JTlfxTOt3Cb0t6jQrkBCsOcOjmWQPR3Vra1eepKVHp6LomWZdHQIDAQAB";

    private static String SIGN_TYPE = "RSA2";

    private static String CHARSET = "utf-8";

    private static String FORMAT = "json";

    public static AliPayPo doAppTrade(AliPayPo payPo) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(
                APIURL,
                APPID,
                PRIVATEKEY,
                FORMAT,
                CHARSET,
                ALIPUBLICKEY,
                SIGN_TYPE
        );
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizContent("{" +
                "\"timeout_express\":\"90m\"," +
                "\"total_amount\": " + payPo.getPayMoney() + "," +
                "\"product_code\":\"QUICK_MSECURITY_PAY\"," +
                "\"subject\": \"观照训练营订单" + payPo.getOrderNo() + "\"," +
                "\"out_trade_no\":\" " + payPo.getOrderNo() + " \"" +
                "  }");
        AlipayTradeAppPayResponse response = alipayClient.pageExecute(request);
        payPo.setIsSuccess(response.isSuccess());
        payPo.setCode(response.getCode());
        payPo.setMsg(response.getMsg());
        payPo.setMethod("alipay.trade.app.pay");
        payPo.setTradeNo(response.getTradeNo());
        payPo.setOutTradeNo(response.getOutTradeNo());
        payPo.setSellerId(response.getSellerId());
        payPo.setSubCode(response.getSubCode());
        payPo.setSubMsg(response.getSubMsg());
        return payPo;
    }

    public static AliPayPo doQrCodeTradePre(AliPayPo payPo) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(
                APIURL,
                APPID,
                PRIVATEKEY,
                FORMAT,
                CHARSET,
                ALIPUBLICKEY,
                SIGN_TYPE
        );
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("http://122.152.237.154:8073/pay/alipay/qr_code/notify");
        request.setBizContent("{" +
                "\"out_trade_no\":\" " + payPo.getOrderNo() + " \"," +
                "\"total_amount\": " + payPo.getPayMoney() + "," +
                "\"subject\": \"观照训练营订单" + payPo.getOrderNo() + "\"" +
                "  }");
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        payPo.setIsSuccess(response.isSuccess());
        payPo.setCode(response.getCode());
        payPo.setMsg(response.getMsg());
        payPo.setMethod("alipay.trade.precreate");
        payPo.setQrCode(response.getQrCode());
        payPo.setOutTradeNo(response.getOutTradeNo());
        payPo.setSubCode(response.getSubCode());
        payPo.setSubMsg(response.getSubMsg());
        return payPo;
    }
}
