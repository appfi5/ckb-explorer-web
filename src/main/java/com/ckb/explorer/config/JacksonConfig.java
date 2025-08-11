package com.ckb.explorer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 注册ByteArrayToStringArraySerializer序列化器和反序列化器
        //mapper.registerModule(ByteArrayToStringArraySerializer.registerModule());

        return mapper;
    }
}