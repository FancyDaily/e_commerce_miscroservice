package com.e_commerce.miscroservice.user.wechat.service;

import com.e_commerce.miscroservice.commons.enums.application.UploadPathEnum;
import com.e_commerce.miscroservice.user.wechat.entity.WechatSession;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 20, 2018 10:39:12 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface WechatService {

	/**
	 * 登录凭证校验
	 * @param code 小程序登录后获取的临时授权码
	 * @return
	 */
	public WechatSession checkAuthCode(String code);

	WechatSession checkAuthCode(String code, String appid, String appSecret);

	/**
	 * 获取微信后台token
	 */
	public String getToken();

	String getToken(String appid, String appSecret);

	/**
	 * 获取二维码
	 * @param scene 作为二维码唯一标识的值
	 * @param page 作为小程序入口的页面值
	 * @return
	 */
	public String genQRCode(String scene, String page);

	String genQRCode(String scene, String page, UploadPathEnum.innerEnum uploadEnum);

	/**
	 * 获取个人手机号码信息
	 * @param encryptedData
	 * @param iv
	 * @param session
	 * @return
	 */
	public String getPhoneNumber(String encryptedData, String iv, WechatSession session);

}
