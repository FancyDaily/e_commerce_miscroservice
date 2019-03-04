package com.e_commerce.miscroservice.commons.util.colligate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 功能描述:MD5算法
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:04:54
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class MD5 {
	
	private static Logger LOG = LoggerFactory.getLogger(MD5.class);
	

	/**
	 * Encodes a string
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws NoSuchAlgorithmException
	 */
	public static String crypt(String str) {
		return crypt(str, null);
	}
	
	public static String crypt(String str,String salt){
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			if(salt!=null && !"".equals(salt)){
				md.reset();
				md.update(salt.getBytes());
			}
			byte[] hash = md.digest(str.getBytes());
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			LOG.info("MD5加密出错,没有相应加密算法");
		}
		return hexString.toString();
	}
	
	
	public static void main(String []args){
		String pwd = "123";
		String salt = "abc";		
		System.out.println(MD5.crypt(pwd));
		
		System.out.println(MD5.crypt(pwd,salt));
	}

}
