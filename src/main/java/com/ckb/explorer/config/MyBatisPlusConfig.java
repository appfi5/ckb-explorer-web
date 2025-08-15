package com.ckb.explorer.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 * 用于配置分页插件等功能
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件，不指定数据库类型，让MyBatis Plus自动检测
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}