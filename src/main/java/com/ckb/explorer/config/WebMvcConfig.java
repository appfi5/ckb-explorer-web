package com.ckb.explorer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 注册安全响应头拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对所有请求添加安全响应头
        registry.addInterceptor(new SecurityHeaderInterceptor())
                .addPathPatterns("/**"); // 匹配所有接口
    }

}