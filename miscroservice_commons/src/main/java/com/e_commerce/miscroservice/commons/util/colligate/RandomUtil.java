package com.e_commerce.miscroservice.commons.util.colligate;

import java.util.UUID;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年12月6日 下午3:49:22
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class RandomUtil {
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	/**
	 * 功能描述: 生成6位随机字符串(含字母数字) 
	 * 作者: 许方毅
	 * 创建时间: 2018年12月6日 下午3:52:18
	 * @return
	 */
	public static String generateUniqueChars() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 6; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

	/**
	 * 功能描述: 生成默认昵称 
	 * 作者: 许方毅
	 * 创建时间: 2018年12月6日 下午4:30:44
	 * @return
	 */
	public static String getDefaultName() {
		String header = "晓主";
		long currentTimeMillis = System.currentTimeMillis();
		String nowStringSuffix = String.valueOf(currentTimeMillis).substring(5);
		return header + nowStringSuffix;
	}

}
