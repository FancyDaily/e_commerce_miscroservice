package com.e_commerce.miscroservice.commons.util.colligate;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;

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

	/**
	 * 不带时间戳的token生成
	 * @param val
	 * @return
	 */
	public static String genToken(String val){
		val = val + String.valueOf(Calendar.getInstance().getTimeInMillis());
		return MD5.crypt(val, AppConstant.TOKEN_KEY);
	}
	
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
	
	
}
