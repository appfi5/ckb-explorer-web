package com.ckb.explorer.config;

import jakarta.annotation.Resource;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 彻底移除健康检查中的 discoveryComposite 组件
 * 适配 Spring Boot 3.x + Spring Cloud 2023.x
 */
@Component
public class DiscoveryCompositeCleaner {

  @Resource
  private HealthContributorRegistry healthContributorRegistry;

  // 应用启动完成后执行（确保健康组件已全部注册）
  @EventListener(ApplicationStartedEvent.class)
  public void removeDiscoveryComposite() {
    // 检查 discoveryComposite 是否存在，存在则移除
    if (healthContributorRegistry.getContributor("discoveryComposite") != null) {
      healthContributorRegistry.unregisterContributor("discoveryComposite");
    }
  }
}
