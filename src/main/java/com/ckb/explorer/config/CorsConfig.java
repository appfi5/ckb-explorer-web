package com.ckb.explorer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有路径使用跨域配置
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOrigins("*")
                // 允许的请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 是否允许发送Cookie
                //.allowCredentials(true)
                // 预检请求的有效期，单位为秒
                .maxAge(3600);
    }
}