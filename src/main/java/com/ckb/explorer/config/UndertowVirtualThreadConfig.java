package com.ckb.explorer.config;

import io.undertow.servlet.api.DeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class UndertowVirtualThreadConfig {

  @Bean
  public UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer() {
    return new VirtualThreadDeploymentCustomizer();
  }

  static class VirtualThreadDeploymentCustomizer implements UndertowDeploymentInfoCustomizer {
    @Override
    public void customize(DeploymentInfo deploymentInfo) {
      // 关键：使用虚拟线程执行器处理所有请求
      ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();
      deploymentInfo.setExecutor(virtualThreads);
//      System.out.println("🎯 Undertow 已设置虚拟线程执行器: " + virtualThreads);
    }
  }
}
