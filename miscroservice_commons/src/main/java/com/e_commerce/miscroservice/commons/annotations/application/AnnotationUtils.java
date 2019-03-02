package com.e_commerce.miscroservice.commons.annotations.application;

import com.e_commerce.miscroservice.commons.helper.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/17 9:35
 * @Copyright 玖远网络
 */
public class AnnotationUtils {
    private static Log logger = Log.getInstance(AnnotationUtils.class);

    private static final String MSG = "message";
    private static final String RESULT = "result";

    /**
     * 注解验证电泳方法
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> validate(Object bean) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", "验证通过");
        result.put("result", true);
        Class<?> cls = bean.getClass();

        // 检测field是否存在
        try {
            // 获取实体字段集合
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                // 通过反射获取该属性对应的值
                f.setAccessible(true);
                // 获取字段值
                Object value = f.get(bean);
                // 获取字段上的注解集合
                Annotation[] arrayAno = f.getAnnotations();
                for (Annotation annotation : arrayAno) {
                    // 获取注解类型（注解类的Class）
                    Class<?> clazz = annotation.annotationType();
                    // 获取注解类中的方法集合
                    Method[] methodArray = clazz.getDeclaredMethods();
                    for (Method method : methodArray) {
                        // 获取方法名
                        String methodName = method.getName();
                        // 过滤错误提示方法的调用
                        if (methodName.equals("message")) {
                            continue;
                        }
                        // 初始化注解验证的方法处理类 （我的处理方法卸载本类中）
                        Object obj = AnnotationUtils.class.newInstance();
                        // 获取方法
                        try {
                            // 根据方法名获取该方法
                            Method m = obj.getClass().getDeclaredMethod(methodName, Object.class, Field.class);
                            // 调用该方法
                            result = (Map<String, Object>) m.invoke(obj, value, f);
                            /* 验证结果 有一处失败则退出 */
                            if (result.get("result").equals(false)) {
                                return result;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.info("找不到该方法:" + methodName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("验证出错");
        }
        return result;
    }

    /**
     * 验证是否空值
     *
     * @param value 参数值
     * @param field 字段
     * @return
     */
    public Map<String, Object> isEmpty(Object value, Field field) {
        Map<String, Object> validateResult = new HashMap<String, Object>();
        IsEmptyAnnotation annotation = field.getAnnotation(IsEmptyAnnotation.class);
        if (value == null || value.equals("")) {
            validateResult.put(MSG, field.getName() + annotation.message());
            validateResult.put(RESULT, false);
        } else {
            validateResult.put(MSG, "验证通过");
            validateResult.put(RESULT, true);
        }
        return validateResult;
    }



    /**
     * 校验非空
     *
     * @param source source
     * {@link IsEmptyAnnotation}
     * @return com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils.Result
     * @author Charlie
     * @date 2018/10/17 14:14
     */
    public static Result verifyNull(Object source) {
        return Result.build (validate (source));
    }

    public static class Result extends HashMap<String, Object>{
        private static Result build(Map<String, Object> source) {
            Result instance = new Result ();
            instance.putAll (source);
            return instance;
        }

        private Result(){}

        public boolean noProblem() {
            return Boolean.TRUE.equals (this.get (RESULT));
        }
        public String msg(){
            Object msg = this.get (MSG);
            return msg != null ? msg.toString () : "";
        }

    }

}
