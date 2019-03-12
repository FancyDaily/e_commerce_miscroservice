package com.e_commerce.miscroservice.commons.wechat.service;

import java.util.List;

import com.e_commerce.miscroservice.commons.enums.SetTemplateIdEnum;
import com.e_commerce.miscroservice.commons.wechat.entity.WechatSession;

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

	/**
	 * 获取微信后台token
	 */
	public String getToken();

	/**
	 * 获取二维码
	 * @param scene 作为二维码唯一标识的值
	 * @param page 作为小程序入口的页面值
	 * @return
	 */
	public String genQRCode(String scene, String page);

	/**
	 * 获取个人手机号码信息
	 * @param encryptedData
	 * @param iv
	 * @param session
	 * @return
	 */
	public String getPhoneNumber(String encryptedData, String iv, WechatSession session);
	/**
	 * 
	 * 功能描述:推送微信消息
	 * 作者:马晓晨
	 * 创建时间:2018年11月21日 下午3:50:57
	 * @param openid
	 * @param formid
	 * @return
	 */
	public String pushOneUser(String openid, String formid); 
	
	/**
	 * 
	 * 功能描述:发送服务通知
	 * 作者:姜修弘
	 * 创建时间:2018年11月24日 下午5:02:24
	 * @param openid
	 * @param formid
	 * @param msg
	 * @param type
	 * @return
	 */
	 public String pushOneUserMsg(String openid, String formid, List<String> msg, SetTemplateIdEnum setTemplateIdEnum, String parameter);
}
