package com.framework.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//多例bean
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)//只能使用在类下
public @interface Scope {
    String value() default "";
}
