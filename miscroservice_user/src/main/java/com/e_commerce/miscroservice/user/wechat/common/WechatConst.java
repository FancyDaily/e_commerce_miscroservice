package com.e_commerce.miscroservice.user.wechat.common;
/**
 * 功能描述:小程序所用接口常量描述
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 20, 2018 10:48:18 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface WechatConst {
	
	//小程序APPID
	String APP_ID = "wx0fc132bec055b894";//生产
	//String APP_ID = "wx763852e49dce9817";//测试
	//小程序密钥
	String APP_SECRET = "d29e69777caab57d7aa0bc3000b5e220";
	//String APP_SECRET = "a43d7f982e06956696deb36542227280";
	
	//授权类型
	String GRANT_TYPE = "grant_type";
	
	//校验session
	String JSCODE_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
	
	//获取token
	String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	
	//获取二维码
	String QCODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";
	

}
