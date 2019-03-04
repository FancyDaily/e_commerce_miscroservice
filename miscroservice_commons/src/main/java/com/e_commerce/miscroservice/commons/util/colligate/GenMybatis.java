/*
package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;


*/
/**
 * 
 * 功能描述:程序自动生成底层数据库操作
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:01:46
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 *//*

public class GenMybatis {

	public static void main(String[] args) throws URISyntaxException {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
//        String filePath = ClassLoader.getSystemResource("generatorConfig.xml").getPath();
        String filePath = ClassLoader.getSystemResource("generatorConfig.xml").toURI().getPath();
        System.out.println(filePath);

		File configFile = new File(filePath);
		Configuration config;
		try {
			
			ConfigurationParser cp = new ConfigurationParser(warnings);

			config = cp.parseConfiguration(configFile);
			

			DefaultShellCallback shellCallback = new DefaultShellCallback(overwrite);

			MyGenerator myBatisGenerator = new MyGenerator(config, shellCallback, warnings);

			ProgressCallback progressCallback = new VerboseProgressCallback();

			myBatisGenerator.generate(progressCallback);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} 
      

    }
		

}
*/
