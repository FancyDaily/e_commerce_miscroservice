package com.e_commerce.miscroservice.commons.enums;

/**
 * 小程序服务通知
 * 	
//	待付款提醒（AT0008）：下单时间（4）、商品详情（6）、支付提醒（9）
//	付款成功通知（AT0005）：付款时间（2）、商品详情（23）、订单状态（59）、备注说明（25）
//	订单取消通知（AT0024）：商品详情（6）、订单状态（14）、取消原因（1）
 //	退款申请通知("AT0637","退款申请通知","11,4,8,29","备注,退款金额,商品名称,更新时间"),
*/

public enum ServiceAdvice {
	waitPayAdvice("AT0008", "待付款提醒","4,6,9","下单时间,商品详情,支付提醒"),
	paySuccessAdvice("AT0005", "付款成功通知","2,23,59,25","付款时间,商品详情,订单状态,备注说明"),
	applyRefund("AT0637","退款申请通知","11,4,8,29","备注,商品名称,退款金额,更新时间"),
	applyRefust("AT0329","退款失败通知","6,2,3,7","备注,商品名称,退款金额,失败时间"),
	applySuccess("AT0787","退款成功通知","18,14,17,15","备注,商品名称,退款金额,退款时间"),
	orderCancelAdvice("AT0024", "订单取消通知","6,14,1","商品详情,订单状态,取消原因");



	private String id;//服务通知模板ID
	private String title;//服务通知模板标题
	private String keywordIds;//模板关键字Id集合，逗号分隔
	private String keywordNames;//模板关键字名称集合，逗号分隔
    
	private ServiceAdvice(String id, String title, String keywordIds, String keywordNames) {
		 this.id = id;
		 this.title = title;
		 this.keywordIds = keywordIds;
		 this.keywordNames = keywordNames;
	}

	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getKeywordIds() {
		return keywordIds;
	}
	public String getKeywordNames() {
		return keywordNames;
	}
}
