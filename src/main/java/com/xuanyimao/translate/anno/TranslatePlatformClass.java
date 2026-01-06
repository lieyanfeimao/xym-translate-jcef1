package com.xuanyimao.translate.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 翻译平台
 * @author liuming
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslatePlatformClass {
	/**平台名*/
	String name();
	/**描述*/
	String desc();
	/**序号，用于页面下拉框展示的排序**/
	int order() default 0;
}
