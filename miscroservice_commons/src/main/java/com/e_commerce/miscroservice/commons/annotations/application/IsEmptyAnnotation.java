package com.e_commerce.miscroservice.commons.annotations.application;

import java.lang.annotation.*;

/**
 * 验证请求参数是否为空
 * Create by hyf on 2018/9/17
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsEmptyAnnotation {
    public boolean isEmpty() default true;
    public String message() default "字段不能为空！";
}
