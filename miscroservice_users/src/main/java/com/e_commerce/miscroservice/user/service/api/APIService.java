package com.e_commerce.miscroservice.user.service.api;

import java.util.Map;

/**
 * 功能描述:所有接口的service统一接口
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Nov 22, 2017 9:45:53 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public interface APIService {

	/**
	 * 应用系统统一接口方法
	 * @param params 服务参数
	 * @return
	 */
	public String execute(Map<String, Object> params);
	
}
