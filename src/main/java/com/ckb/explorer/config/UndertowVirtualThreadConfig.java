package com.ckb.explorer.config;

import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class UndertowVirtualThreadConfig {

    // 创建一个应用级别的虚拟线程执行器，可在应用关闭时正确关闭
    @Bean(destroyMethod = "close")
    public ExecutorService virtualThreadExecutor() {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        return executor;
    }

    // 配置Undertow使用虚拟线程
    @Bean
    public UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer(ExecutorService virtualThreadExecutor) {
        return deploymentInfo -> {
            // 使用应用级别的虚拟线程执行器处理所有请求
            deploymentInfo.setExecutor(virtualThreadExecutor);
        };
    }

    // 优化Undertow服务器配置
    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowCustomizer() {
        return factory -> {
            factory.addBuilderCustomizers(builder -> {
                // 增加IO线程数 - 根据CPU核心数动态调整
                int ioThreads = Math.max(Runtime.getRuntime().availableProcessors(), 4);
                builder.setIoThreads(ioThreads);
                // 增加工作线程数
                int workerThreads = Math.max(Runtime.getRuntime().availableProcessors() * 16, 64);
                builder.setWorkerThreads(workerThreads);
                // 开启HTTP2支持
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
                // 设置连接超时时间为30秒
                builder.setServerOption(UndertowOptions.IDLE_TIMEOUT, 30000);
                // 增加缓冲区大小
                builder.setServerOption(UndertowOptions.MAX_BUFFERED_REQUEST_SIZE, 16384);
            });
        };
    }
}
