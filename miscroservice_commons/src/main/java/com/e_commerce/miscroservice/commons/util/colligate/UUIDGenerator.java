/*
package com.e_commerce.miscroservice.commons.util.colligate;

import java.util.Calendar;
import java.util.Random;

import org.apache.commons.lang.time.FastDateFormat;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

*/
/**
 * 
 * 功能描述:UUID工具类
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:11:47
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 *//*


public class UUIDGenerator {

	protected static TimeBasedGenerator timeBasedGenerator;
	static {
		new UUIDGenerator();
	}

	public UUIDGenerator() {
		ensureGeneratorInitialized();
	}

	protected void ensureGeneratorInitialized() {
		if (timeBasedGenerator == null) {
			synchronized (StrongUuidGenerator.class) {
				if (timeBasedGenerator == null) {
					timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
				}
			}
		}
	}

	public static String getNextId() {
		return timeBasedGenerator.generate().toString().replaceAll("-", "");
	}


	*/
/**
	 * 返回6位随机数
	 *//*

	public static String messageCode() {
		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		String result = "";
		for (int i = 0; i < 6; i++)
			result += String.valueOf(array[i]);
		return String.valueOf(result);
	}

	*/
/**
	 * 根据时间生成24位序号 
	 *//*

	public synchronized static String nextSerial() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(FastDateFormat.getInstance("yyyyMMddHHmmssSSSS").format(Calendar.getInstance()));
		stringBuffer.append(messageCode());
		String str = stringBuffer.toString();
		return str;
	}
	
	
	*/
/**
	 * 根据时间生成21位序号
	 * 委贷系统专用
	 *//*

	public synchronized static String nextSerialWithTag() {
		StringBuffer stringBuffer = new StringBuffer("A");
		stringBuffer.append(FastDateFormat.getInstance("yyyyMMddHHmmss").format(Calendar.getInstance()));
		stringBuffer.append(messageCode());
		String str = stringBuffer.toString();
		return str;
	}
	
	

	*/
/**
	 * 生成带前缀标记的序号
	 * 
	 * @param tag
	 * @return
	 *//*

	public synchronized static String nextSerialWithTag(String tag) {
		return (tag + nextSerial());
	}

	*/
/**
	 * 使用推特的分布式ID生成方式
	 * 
	 * @return
	 *//*

	public static long nextId() {
		return IdWorker.getInstance().nextId();
	}

	public static void main(String[] args) {
		System.out.println(nextSerialWithTag("D"));
	}
	

}
*/
