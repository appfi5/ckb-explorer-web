package com.ckb.explorer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 启用路径扩展名匹配
        configurer// 启用参数匹配 (format=json)
                .favorParameter(true)
                .parameterName("format")
                // 设置默认媒体类型
                //.defaultContentType(MediaType.APPLICATION_JSON)
                // 添加对application/vnd.api+json的支持
                .mediaType("vnd.api+json", MediaType.parseMediaType("application/vnd.api+json"));
                // 添加对json的支持
                //.mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建Jackson消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 设置支持的媒体类型
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        //supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.parseMediaType("application/vnd.api+json"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
        // 添加到转换器列表
        converters.add(converter);
    }
}