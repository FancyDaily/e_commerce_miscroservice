package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.user.vo.WechatLoginVIew;

import java.util.Map;



/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 27, 2018 4:23:56 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface LoginService {

	/**
	 * 电话授权
	 * @param encryptedData
	 * @param iv
	 */
	String phoneAutho(String openid, String encryptedData, String iv);

	/**
	 * 校验短信验证码
	 * @param validCode
	 * @param uuid 设备号
	 */
	Map<String, Object> validSmsCode(String openid, String validCode,String uuid);

	/**
	 * 登陆校验
	 * @return
	 */
	Map<String, String> checkLogin(WechatLoginVIew view);

	/**
	 * 功能描述:  使用openid获取token
	 * 作者: 许方毅
	 * 创建时间: 2018年11月27日 下午6:29:37
	 * @param openid
	 */
	Map<String, Object> loginByOpenid(String openid,String uuid);

	/**
	 * 观照微信公众号登录
	 * @param openid
	 * @param encryptedData
	 * @param iv
	 * @param session_key
	 * @param uuid
	 * @return
	 */
	Map<String, Object> gzWxLogin(String openid, String encryptedData, String iv, String session_key, String uuid);

	/**
	 * 观照微信公众号登录
	 * @param openid
	 * @param telephone
	 * @param uuid
	 * @return
	 */
	Map<String, Object> gzWxLogin(String openid, String telephone, String uuid);
}