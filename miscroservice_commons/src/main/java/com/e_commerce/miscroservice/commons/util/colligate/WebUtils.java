package com.e_commerce.miscroservice.commons.util.colligate;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
/**
 * 
 * 功能描述:spring ajax判断工具
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:2017年7月7日 下午4:12:00
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class WebUtils extends org.springframework.web.util.WebUtils implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext; //应用上下文对象
	
	/**
	 * 判断是否ajax请求
	 * spring ajax 返回含有 ResponseBody 或者 RestController注解
	 * @param handlerMethod HandlerMethod
	 * @return 是否ajax请求
	 */
	public static boolean isAjax(HandlerMethod handlerMethod) {
		ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
		if (null != responseBody) {
			return true;
		}
		RestController restAnnotation = handlerMethod.getBeanType().getAnnotation(RestController.class);
		if (null != restAnnotation) {
			return true;
		}
		return false;
	}
	

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebUtils.applicationContext = applicationContext;
    }

	public static <T> T getBean(String beanName, Class<T> cls) {
		return applicationContext.getBean(beanName, cls);
	}

	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
}
