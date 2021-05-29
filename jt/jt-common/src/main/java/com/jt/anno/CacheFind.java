package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//实现缓存的查询
@Target(ElementType.METHOD)  //注解对方法有效
@Retention(RetentionPolicy.RUNTIME)  //注解的作用周期
public @interface CacheFind {

    /**
     * 策略：
     * 1.用户可以自己定义redis中的key，如果定义，则使用用户自己的
     * 如果没定义，则使用自动生成的key：包名.类名.方法名::第一个参数
     * 2.可以为数据添加超时时间
     */
    public String key() default "";  //如果用户不写，则自动生成
    public int seconds() default 0;  //设置超时时间，0表示永不超时
}
