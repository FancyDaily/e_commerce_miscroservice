package com.e_commerce.miscroservice.guanzhao_proj.product_order.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;

public class Test {
	public static final String URL = "https://openapi.alipay.com/gateway.do";
	public static final String ALIPAY_APPID = "2018091261395191";

	public static final String SIGNTYPE = "RSA2";
	public static final String FORMAT = "json";
	public static final String CHARSET = "utf-8";// 编码集
	// 私钥
	public static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCZtpyNQIOebbXdQvAW49V2r8CxrtsGK+PQKrGGPQDnX1I/C3rG72rz6wL6H2u+EX6kSzrveg61ss/LxpzCd0inKWmHrmt3Zgu4KiD6Yuy/jaOI00vKltyA79X5AfkAWEkkcAoCVVOmqNHAvjsbEulDQlyew8ak2ZtzEANir7le/v/pw+EiVj9+/m8W3L8US5YW3/eqv/2WkwC6VvFCgEbiY+XOo/jcvHxnFb9lfQn778dfuob2MRc4mkTqaUUrgEdrp9B+JgSXLnk+lBdh9lgubzv8RthzvbjUb8lOV/FM63cJvS3qNCuQEKw5w6OZZA9HdWtrV56kpUenouiZZl0dAgMBAAECggEANP2YMLXZ6gcGWDXYUPvQPi9OrbKK/TCqQ7xEIPPs2NaqouNROz2UUGxnRVUJrqeXUgpBUgdxBMCVFDnvFrdKi54iLE077Rh319BmOAtcdJjelK/LBBdIAwcra8F7VtwYswAQ40cJJH40eU7bT3UQh0hcZ7s0QuZlyZ/umtnbfNzBEpH0paw482j1ppb3iVOQcU/T0OtIr6ulfZMHPYXue5E26eo9FlUBbTKV442VkEA7y62ZTEvCwhUzz5uJ+LWWn6qsW6PlH+UPubYzoRyaQndj7faN/YfaaKWiIs/x4IsTZB+Uazp9+9apdUtaZXw1Tm1ahClkzkaR3cUCoXo7XQKBgQDIl/3p1353T00XVjZlXm2/dtdYh/1INgrC8tMSB8kvb6CLMU+ytwE8FXPdjb8mZ67HfXrYWOV6V+MlSzkXMtGTR5lvijHplLyW3IHfGcfiZMH0ixQpb+9W4Pp0f6Od8tmn41sY6lh3z/PE5Bc+mytQkG+nrOTkNNoPsdIF9BkMuwKBgQDEK7N1yKysCHdOFL22odUSIvt57YbUNYcJ1fewCoLsgnuIcl8yb7ixdL8u24sotgijj20qLffaln+DMGECZrAvK1mU+EGlVClZEOLDEjWmqzBz8jTkymH67KXgIK7PmfcdPZPxgHQ/jEFUSyNFW+Bayp4ZNRhxlR0bD/3o85bMBwKBgFWOxqVj3bQj5dzLT95CvYrJtKY1MqoHKuXlznNFUOFjulVEThsLBYLMH6RjHiHSXXEHK0t190MMk3zHre0gJtWr110E068uWpX6LYasJ15/3V5hp9LYxHwI76qa+n2XJJEAX02sPtmzn+Td6LAyXPI6+PTKwMI5bLz++nbBm6GbAoGAcJJDYYvOysnw7LWqSGuFweqhNM7BWHV/EH3grPzbNK2MBjjbuC20ZuD/9Pu3V51eR72+fz3xxVYnxji08pPK9saBmaL8yMsLDzlJZ3fDoekK3P5YrBIXN88lQQCAUFGiJP15MFTuYKkztBOKIfsfO/qZzw1RAZz4HteGdW+TUXECgYACU8DPKztcQe4sX2swDuiT3MtbAgjYMJ5W0k4k4ps1XC0ij6pwHJPCsBYt+ZhqTm0xL3cHgxC8y88qA0FNC49A+yPxlmH5V1Q+x3DiqNdSwCotOWIRrbLLyH77vL80Dv0ctPZVJbpRs0oam3iVuS954Wnr3RW4SL1NdAtzL2o63Q==";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApgZiHa3F/7RUBRIHKiHeXQcAVN1bEZA8zfYTwt31osA0Mz4+U8kt01RgLyEkULrYfBCTKqL2tqpD6lI2cjycp0Ayb/bPx+A7aQswvJcd8GC99Or8SJoHHYDoPAQX1HYROTy49FL3jAV43Ri5h4djyXjzCocyDW2ET5igLZmwYbylPl6UPr7+rrPqffyGi2Kpf+Hve9IWbnL8IfZxpTIcVWYLP9JQxGcuM8WUcvzaqLRi5aO4aa8OOHycQzFfAAuM51MG1Klx6OqUY2EzFlYyyu24RJwc9VjxOzrVW5d4LLC+ghwZKUmmxvHjYw6antPkmW55qBfNhlPCmg4RcdX5+QIDAQAB";
	private static AlipayClient alipayClient = null;

	public static final String notify_url = "http://www.example.com";
	/**
	 * 获得初始化的AlipayClient
	 *
	 * @return 支付宝客户端
	 */
	public static AlipayClient getAlipayClient() {
		if (alipayClient == null) {
			/*synchronized (AlipayConfig.class) {
				if (null == alipayClient) {
					alipayClient = new DefaultAlipayClient(URL, ALIPAY_APPID, APP_PRIVATE_KEY, FORMAT, CHARSET,
						ALIPAY_PUBLIC_KEY, SIGNTYPE);
				}
			}*/
		}
		return alipayClient;
	}
	public static void main(String[] args)  {
		String orderNo=String.valueOf(System.currentTimeMillis());
		//第一步生成订单点击第一个链接进行支付 注释这个
//		pay(orderNo);
		//根据第一步打印的订单单替换
		orderNo="1562898482305";
		System.out.println("订单号为:"+orderNo);

		JSONObject jsonObject=new JSONObject();
		jsonObject.put("out_trade_no",orderNo);
		AlipayClient alipayClient = new DefaultAlipayClient(URL,
			ALIPAY_APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent(jsonObject.toJSONString());
		try {
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if(response.isSuccess()){

				System.out.println("调用成功");
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();

		}

	}

	private static void pay(String orderNo){
		// 实例化客户端
		AlipayClient alipayClient = getAlipayClient();

		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay

		//
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("1");// shopProduct.getProductName()
		model.setSubject("1");// shopProduct.getProductName()
		model.setOutTradeNo(orderNo);// orderIdString
		model.setTimeoutExpress("30m");
		model.setTotalAmount("0.01");// money
		model.setProductCode("kdk");

		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//

		alipayRequest.setBizModel(model);
		alipayRequest.setNotifyUrl(notify_url);// 在公共参数中设置回跳和通知地址
		String html = "";
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
			html = response.getBody();
			System.out.println(html);
		} catch (AlipayApiException e) {

		}
	}
}
