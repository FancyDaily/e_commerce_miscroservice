package com.e_commerce.miscroservice.commons.util.colligate;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Nov 2, 2017 9:42:25 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class StrongUuidGenerator {
	protected static TimeBasedGenerator timeBasedGenerator;
	static{
		new StrongUuidGenerator();
	}

	  public StrongUuidGenerator() {
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
	    return timeBasedGenerator.generate().toString();
	  }
	  
	  
	  
	  public static void main(String []args){
		  System.out.println(StrongUuidGenerator.getNextId());
		  System.out.println(UUIDGenerator.getNextId());
	  }
}
