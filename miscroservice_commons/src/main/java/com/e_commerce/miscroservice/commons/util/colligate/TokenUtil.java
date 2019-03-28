package com.e_commerce.miscroservice.commons.util.colligate;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.DesUtil;

import java.util.Calendar;



/**
 * 功能描述:生成用户token
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Aug 8, 2018 10:20:51 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class TokenUtil {

	private static String KEY = "yixiaoshi&&time";
	/**
	 * 不带时间戳的token生成
	 * @param val
	 * @return
	 */
//	public static String genToken(String val){
//		val = val + String.valueOf(Calendar.getInstance().getTimeInMillis());
//		return MD5.crypt(val, AppConstant.TOKEN_KEY);
//	}
	
	/**
	 * 根据时间戳生成token
	 * @param val 用户ID
	 * @param time 生成token时间
	 * @return
	 */
	public static String genToken(String val,Long time){
		val = val + String.valueOf(Calendar.getInstance().getTimeInMillis());
		return MD5.crypt(val, AppConstant.TOKEN_KEY+time);
	}

	/**
	 * 根据key，userId生成token
	 * 用于后期解析还原id
	 * @param val
	 * @return
	 */
	public static String genToken(String val){
		String desVal = null;
		try {
			desVal = DesUtil.encrypt(val,KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String token = Base64.encode(desVal.getBytes());

		return token;

	}
	/**
	 * 根据token解析还原id
	 * @param token
	 * @return
	 */
	public static String genUserId(String token){

		String basVal = new String(Base64.decode(token));
		String userId = DesUtil.decrypt(basVal,KEY);

		return userId;

	}

	public static void main(String[] args) {
		String des = genToken("1");
		System.out.println(des);
		System.out.println(genUserId(des));
	}
	
}
