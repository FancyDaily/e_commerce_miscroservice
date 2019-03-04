package com.e_commerce.miscroservice.commons.util.colligate;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午3:37:12
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class AESUtil {
	public static boolean initialized = false;
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法
	
	public static final String CBC_MODE = "AES/CBC/PKCS7Padding";

	
	static{
		initialize();
	}
	
	public static byte[] initSecretKey() {

		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		// 初始化此密钥生成器，使其具有确定的密钥大小
		// AES 要求密钥长度为 128
		kg.init(128);
		// 生成一个密钥
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	public static void initialize() {
		if (initialized)
			return;
		Security.addProvider(new BouncyCastleProvider());
		initialized = true;
	}
	
	
	private static Key toKey(byte[] key) {
		// 生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}

	public static byte[] encrypt(byte[] data, Key key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM,null);
	}

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm,null);
	}

	public static byte[] encrypt(byte[] data, Key key,byte[] iv) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM,iv);
	}

	public static byte[] encrypt(byte[] data, byte[] key,byte[] iv) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM,iv);
	}

	public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm,byte[] iv) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm,iv);
	}
	
	
	public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm,byte[] iv) throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为加密模式
		if(iv!=null && CBC_MODE.equals(cipherAlgorithm)){
			cipher.init(Cipher.DECRYPT_MODE, key,generateIV(iv));
		}
		else{
			cipher.init(Cipher.DECRYPT_MODE, key);
		}
		// 执行操作
		return cipher.doFinal(data);
	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	public static byte[] decrypt(byte[] data, Key key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM,null);
	}

	public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm,null);
	}
	
	
	public static byte[] decrypt(byte[] data, byte[] key,byte[] iv) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM,iv);
	}

	public static byte[] decrypt(byte[] data, Key key,byte[] iv) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM,iv);
	}

	public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm,byte[] iv) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm,iv);
	}
	
	

	public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm,byte[] iv) throws Exception {
		// 实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		// 使用密钥初始化，设置为解密模式
		if(iv!=null && CBC_MODE.equals(cipherAlgorithm)){
			cipher.init(Cipher.DECRYPT_MODE, key,generateIV(iv));
		}
		else{
			cipher.init(Cipher.DECRYPT_MODE, key);
		}
		
		// 执行操作
		return cipher.doFinal(data);
	}

	/**
	 * 生成初始算法参数
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
		AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
		params.init(new IvParameterSpec(iv));
		return params;

	}
	
	
	
	private static String showByteArray(byte[] data) {
		if (null == data) {
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for (byte b : data) {
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		byte[] key = initSecretKey();
		System.out.println("key：" + showByteArray(key));
		Key k = toKey(key); // 生成秘钥
		String data = "AES数据";
		System.out.println("加密前数据: string:" + data);
		System.out.println("加密前数据: byte[]:" + showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = encrypt(data.getBytes(), k);// 数据加密
		System.out.println("加密后数据: byte[]:" + showByteArray(encryptData));
		
		System.out.println();
		byte[] decryptData = decrypt(encryptData, k);// 数据解密
		System.out.println("解密后数据: byte[]:" + showByteArray(decryptData));
		System.out.println("解密后数据: string:" + new String(decryptData));
	}
}
