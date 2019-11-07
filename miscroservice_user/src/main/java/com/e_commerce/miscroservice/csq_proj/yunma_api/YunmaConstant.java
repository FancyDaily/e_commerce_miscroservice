package com.e_commerce.miscroservice.csq_proj.yunma_api;

/**
 * 正元校园支付API常量
 * @Author: FangyiXu
 * @Date: 2019-11-01 11:46
 */
public interface YunmaConstant {

	/**
	 * 应用编号
	 */
	String TEST_APP_ID = "116511";

	String APP_ID = TEST_APP_ID;

	/**
	 * 测试地址
	 */
	String TEST_URL_PREFIX = "http://unifiedpay.lsmart.wang";

	/**
	 * 灰度地址
	 */
	String GRAY_URL_PREFIX = "https://paygray.xiaofubao.com";

	/**
	 * 正式地址
	 */
	String DEV_URL_PREFIX = "https://pay.xiaofubao.com";

	/**
	 * 预下单地址
	 */
	String PREORDER_URL = "/pay/unified/preOrder.shtml";

	/**
	 * 异步通知地址
	 */
	String NOTIFY_URL = "http://test.xiaoshitimebank.com/user/yunmaNotify/pay";

	/**
	 * 返回页面地址
	 */
	String RETURN_URL = "http://unified.lsmart.wang/back";

	/**
	 * 商户编码
	 */
	String TEST_CP_CODE = "1005454";

	String CP_CODE = TEST_CP_CODE;
	/**
	 * 签名秘钥
	 */
	String TEST_SIGN_KEY = "1651130";

	String SIGN_KEY = TEST_SIGN_KEY;
	/**
	 * platform参数
	 */
	String PLATFORM_VALUE = "YUNMA_WXAPP";



}
