package com.jt.config;

import com.jt.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //类似于web.xml文件
public class MvcConfigurer implements WebMvcConfigurer{

    //开启匹配后缀型配置
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //2.2.5以后开始不使用
        configurer.setUseSuffixPatternMatch(true);
    }

    @Autowired
    private UserInterceptor userInterceptor;

    //新增拦截器，不登陆  不允许访问购物车、订单
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将自己的拦截器添加到注册中心,拦截器需要设定拦截路径
        // /cart/** 购物车的多级目录 /cart/* 购物车的一级目录
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/cart/**","/order/**");
    }
}
