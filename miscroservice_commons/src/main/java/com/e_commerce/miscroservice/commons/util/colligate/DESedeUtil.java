/*
package com.e_commerce.miscroservice.commons.util.colligate;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
*/
/**
 * 
 * 功能描述:DES加密类
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午3:49:44
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 *//*

public class DESedeUtil {
	private static final String ALGORITHM_MD5 = "md5";

	private static final String ALGORITHM_DESEDE = "DESede";

	private static final String CIPHER_TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	private static final String CHARSET_UTF_8 = "UTF-8";

	*/
/**
	 * test
	 * 
	 * @param args
	 * @throws Exception
	 *//*

	public static void main(String[] args) throws Exception {
		System.out.println("DESede加解密测试：");
		
		//final String privateKey = "HG58YZ3CR9HG58YZ3CR9HG58YZ3CR9";
		final String privateKey = UUIDGenerator.getNextId();
		String text = "123456789012345678";// origin data
		System.out.println("加密前:" + text + "密钥长度为:" + privateKey.length());
		byte[] codedtext = DESedeUtil.encrypt(text, privateKey);

		String codedtextb = Base64.encode(codedtext);// data transfer as text
		System.out.println("Base64 format:" + codedtextb);
		codedtext = Base64.decode(codedtextb);

		String decodedtext = DESedeUtil.decrypt(codedtext, privateKey);
		System.out.println("解密后:" + decodedtext); // This correctly shows "hello world!"
	}

	*/
/**
	 * encoded message
	 * 
	 * @param message
	 *            origin message
	 * @param sKey
	 *            origin privateKey
	 * @return
	 * @throws Exception
	 *//*

	public static byte[] encrypt(String message, String sKey) throws Exception {
		final byte[] keyBytes = getKeyBytes(sKey);

		final SecretKey mainKey = new SecretKeySpec(keyBytes, ALGORITHM_DESEDE);
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, mainKey, iv);

		final byte[] plainTextBytes = message.getBytes(CHARSET_UTF_8);
		final byte[] cipherText = cipher.doFinal(plainTextBytes);

		return cipherText;
	}

	*/
/**
	 * decode from encoded message
	 * 
	 * @param message
	 *            encoded message
	 * @param sKey
	 *            origin privateKey
	 * @return
	 * @throws Exception
	 *//*

	public static String decrypt(byte[] message, String sKey) throws Exception {
		final byte[] keyBytes = getKeyBytes(sKey);

		final SecretKey mainKey = new SecretKeySpec(keyBytes, ALGORITHM_DESEDE);
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
		decipher.init(Cipher.DECRYPT_MODE, mainKey, iv);

		final byte[] plainText = decipher.doFinal(message);
		return new String(plainText, CHARSET_UTF_8);
	}

	*/
/**
	 * generate keyBytes
	 * 
	 * @param sKey
	 *            origin privateKey
	 * @return
	 * @throws Exception
	 *//*

	private static byte[] getKeyBytes(String sKey) throws Exception {
		final MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
		final byte[] digestOfPassword = md.digest(sKey.getBytes(CHARSET_UTF_8));
		final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
		for (int j = 0, k = 16; j < 8;) {
			keyBytes[k++] = keyBytes[j++];
		}
		return keyBytes;
}
}
*/
