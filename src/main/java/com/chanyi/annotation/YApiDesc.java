package com.chanyi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface YApiDesc {

	String title() default "";// 接口名

	String path() default "";// 接口路径

	String[] paramsInfo() default { "" };// 参数描述

	String filed() default "";// 自定义参数类中参数描述

	String groupName() default "";// 分组名（只能标注在类上）

	String groupDesc() default "";// 分组描述（只能标注在类上）
}