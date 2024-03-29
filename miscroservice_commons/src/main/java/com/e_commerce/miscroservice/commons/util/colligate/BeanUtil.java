package com.e_commerce.miscroservice.commons.util.colligate;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

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
 * 创建时间:2017年7月7日 下午3:38:32
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public final class BeanUtil extends org.springframework.beans.BeanUtils {
	private BeanUtil(){}

	/**
	 * 实例化对象
	 * @param clazz 类
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		return (T) instantiate(clazz);
	}

	/**
	 * 实例化对象
	 * @param clazzStr 类名
	 * @return 对象
	 */
	public static <T> T newInstance(String clazzStr) {
		try {
			Class<?> clazz = Class.forName(clazzStr);
			return newInstance(clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取Bean的属性
	 * @param bean bean
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	public static Object getProperty(Object bean, String propertyName) {
		PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), propertyName);
		if (pd == null) {
			throw new RuntimeException("Could not read property '" + propertyName + "' from bean PropertyDescriptor is null");
		}
		Method readMethod = pd.getReadMethod();
		if (readMethod == null) {
			throw new RuntimeException("Could not read property '" + propertyName + "' from bean readMethod is null");
		}
		if (!readMethod.isAccessible()) {
			readMethod.setAccessible(true);
		}
		try {
			return readMethod.invoke(bean);
		} catch (Throwable ex) {
			throw new RuntimeException("Could not read property '" + propertyName + "' from bean", ex);
		}
	}
	
	/**
	 * 设置Bean属性
	 * @param bean bean
	 * @param propertyName 属性名
	 * @param value 属性值
	 */
	public static void setProperty(Object bean, String propertyName, Object value) {
		PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), propertyName);
		if (pd == null) {
			throw new RuntimeException("Could not set property '" + propertyName + "' to bean PropertyDescriptor is null");
		}
		Method writeMethod = pd.getWriteMethod();
		if (writeMethod == null) {
			throw new RuntimeException("Could not set property '" + propertyName + "' to bean writeMethod is null");
		}
		if (!writeMethod.isAccessible()) {
			writeMethod.setAccessible(true);
		}
		try {
			writeMethod.invoke(bean, value);
		} catch (Throwable ex) {
			throw new RuntimeException("Could not set property '" + propertyName + "' to bean", ex);
		}
	}

	
	/**
	 * copy 对象属性到另一个对象，默认不使用Convert
	 * @param src
	 * @param clazz 类名
	 * @return T
	 */
	public static <T> T copy(Object src, Class<T> clazz) {
		BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);

		T to = newInstance(clazz);
		copier.copy(src, to, null);
		return to;
	}

	/**
	 * 拷贝对象
	 * @param src 源对象
	 * @param dist 需要赋值的对象
	 */
	public static void
	copy(Object src, Object dist) {
		BeanCopier copier = BeanCopier
				.create(src.getClass(), dist.getClass(), false);

		copier.copy(src, dist, null);
	}

	/**
	 * 将对象装成map形式
	 * @param src
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(Object src) {
		return BeanMap.create(src);
	}

	/**
	 * 将map 转为 bean
	 */
	public static <T> T toBean(Map<String, String> beanMap, Class<T> valueType) {
		T bean = BeanUtil.newInstance(valueType);
		PropertyDescriptor[] beanPds = getPropertyDescriptors(valueType);
		for (PropertyDescriptor propDescriptor : beanPds) {
			String propName = propDescriptor.getName();
			// 过滤class属性 
			if (propName.equals("class")) {
				continue;
			}
			if (beanMap.containsKey(propName)) { 
				Method writeMethod = propDescriptor.getWriteMethod();
				if (null == writeMethod) {
					continue;
				}
				Object value = beanMap.get(propName);
				if (!writeMethod.isAccessible()) {
					writeMethod.setAccessible(true);
				}
				try {
					writeMethod.invoke(bean, value);
				} catch (Throwable e) {
					throw new RuntimeException("Could not set property '" + propName + "' to bean", e);
				}
			} 
		}
		return bean;
	}

}
