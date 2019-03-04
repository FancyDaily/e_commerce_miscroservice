package com.e_commerce.miscroservice.commons.util.colligate;

import java.util.Random;

/**
 * 
 * 功能描述:随机数/字串实用类
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:07:14
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class RandomStringUtil {
	
	private enum Type{
		LOWER_LETTER			//小写字母
		,UPPER_LETTER			//大字字母
		,LETTER					//字母
		,DIGITAL				//数字
		,LETTER_OR_DIGITAL   //数字或字母
		,ASCII					//ascii码
	}
	
	private static final int LOWER_LETTER_ASCII_START = 97;
	private static final int UPPER_LETTER_ASCII_START = 65;
	private static final int DIGITAL_ASCII_START = 48;

	/**
	 * 随机余数
	 */
	private static int randomResidue(int seed) {
		Random random = new Random();
		return Math.abs(random.nextInt() % seed);
	}

	/**
	 * 随机boolean
	 * @return
	 */
	private static boolean randomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	/**
	 * 随机小写字母
	 * @return char
	 */
	public static char lowerLetter() {
		return (char) (randomResidue(26) + LOWER_LETTER_ASCII_START);
	}

	/**
	 * 随机大写字母
	 * @return char
	 */
	public static char upperLetter() {
		return (char) (randomResidue(26) + UPPER_LETTER_ASCII_START);
	}

	/**
	 * 随机字母
	 * @return
	 */
	public static char letter() {
		if (randomBoolean())
			return lowerLetter();
		else
			return upperLetter();
	}

	/**
	 * 随机数字
	 * @return char
	 */
	public static char digital() {
		return (char) (randomResidue(10) + DIGITAL_ASCII_START);
	}
	
	/**
	 * 字符
	 * @return
	 */
	public static char character(){
		return (char)randomResidue(255);
	}

	/**
	 * 随机字母或数字
	 * @return char
	 */
	public static char letterOrDigital() {
		if (randomBoolean())
			return letter();
		else
			return digital();
	}
	
	/**
	 * 随机字母或数字
	 * @return char
	 */
	public static char digitalOrLetter() {
		return letterOrDigital();
	}
	
	/**
	 * 数字字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String digital( int length ){
		return string( Type.DIGITAL,length );
	}
	
	/**
	 * 字母字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String letter( int length ){
		return string( Type.LETTER,length );
	}
	
	/**
	 * 小写字母字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String lowerLetter( int length ){
		return string( Type.LOWER_LETTER,length );
	}
	
	/**
	 * 大写字母字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String upperLetter( int length ){
		return string( Type.UPPER_LETTER,length );
	}
	
	/**
	 * 字母或数字字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String letterOrDigital( int length ){
		return string( Type.LETTER_OR_DIGITAL,length );	
	}
	
	/**
	 * 字母或数字字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String digitalOrLetter( int length ){
		return string( Type.LETTER_OR_DIGITAL,length );	
	}
		
	/**
	 * 字符串
	 * @param length 字符长度
	 * @return String
	 */
	public static String character( int length ){
		return string( Type.ASCII,length );	
	}

	/**
	 * 生成随机字符串
	 * @param mode
	 * @param length
	 * @return String
	 */
	public static String string(Type type, int length) {
		StringBuffer chars = new StringBuffer();
		char item;
		for (int i = 0; i < length; i++) {
			if( Type.DIGITAL.equals( type ) ){
				item = digital( );
			}else if( Type.LETTER.equals( type ) ){
				item = letter();
			}else if( Type.LOWER_LETTER.equals( type ) ){
				item = lowerLetter();
			}else if( Type.UPPER_LETTER.equals( type ) ){
				item = upperLetter();
			}else if( Type.LETTER_OR_DIGITAL.equals( type ) ){
				item = digitalOrLetter( );
			}else{
				item = character();
			}
			chars.append(item);
		}
		return chars.toString();
	}

}
