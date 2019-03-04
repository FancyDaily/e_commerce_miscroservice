package com.e_commerce.miscroservice.commons.util.colligate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 功能描述:区别于丁卫江的AES工具类
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年1月9日 下午5:36:41
 */
public class AESCommonUtil {
	/**
	 * 加密算法为AES
	 */
	private static final String KEY_ALGORITHM = "AES";
	/**
	 * 原始的明文key
	 */
	private static final String DEFAULT_ORIGINKEY = "xiaoshitimebank";
	
	/**
	 * 功能描述:使用默认的明文key进行加密
	 * 作者:马晓晨
	 * 创建时间:2019年1月9日 下午6:11:40
	 * @param clearText
	 * @return
	 */
	public static String encript(String clearText) {
		return encript(clearText, DEFAULT_ORIGINKEY);
	}
	/**
	 * 功能描述:使用默认的明文key进行解密
	 * 作者:马晓晨
	 * 创建时间:2019年1月9日 下午6:11:40
	 * @param clearText
	 * @return
	 */
	public static String decript(String clearText) {
		return decript(clearText, DEFAULT_ORIGINKEY);
	}
	
	/**
	 * AES的加密运算
	 * @param clearText
	 * @param originKey
	 * @return
	 */
	public static String encript(String clearText, String originKey) {
		//1、获取加密算法工具类对象
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		//2、对工具类对象进行初始化
		//mode:加密/解密模式    key：对原始密钥处理后的密钥
		SecretKey key = getKey(originKey);
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		//3、用加密工具对明文进行加密
		byte[] doFinal = null;
		try {
			doFinal = cipher.doFinal(clearText.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		String base64Encode = Base64.encode(doFinal);	
		return base64Encode;
	}
	
	/**
	 * AES解密
	 * @param cipherText
	 * @param originKey
	 * @return
	 */
	public static String decript(String cipherText, String originKey) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		//生成密钥
		SecretKey key = getKey(originKey);
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] decode = Base64.decode(cipherText);
		String clearText = null;
		try {
			clearText = new String(cipher.doFinal(decode));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return clearText;
	}
	
	/**
	 * 使用明文的key生成密钥
	 * 不论originKey多长，我们都要形成一个16个字节长度的原始密钥
	 * @param key
	 * @return
	 */
	private static SecretKey getKey(String originKey) {
		//byte数组每个元素默认初始值为0
		byte[] buffer = new byte[16];
		//获取用户提供的原始密钥字节数组
		byte[] originBytes = originKey.getBytes();
		//如果originBytes.length > 16,只要16个字节，如果没有超过16个字节，就用默认初始值来填充
		for (int i = 0; i < 16 && i < originBytes.length; i++) {
			buffer[i] = originBytes[i];
		}
		return new SecretKeySpec(buffer, KEY_ALGORITHM);
	}
	
	public static void main(String[] args) throws Exception {
		String clearText = "马晓晨";
		String originKey = "afoweu1245";
		String cipherText = encript(clearText);
		System.out.println(cipherText);
		clearText = decript(cipherText);
		System.out.println(clearText);
	}
}
