package com.ckb.explorer.config;

import com.ckb.explorer.feign.WorkerService;
import jakarta.annotation.Resource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("workerServiceHealth") // 健康组件名称
public class WorkerServiceHealthIndicator implements HealthIndicator {

  @Resource
  private WorkerService workerService;

  @Override
  public Health health() {
    try {
      // 1. 调用 worker 健康接口（设置超时时间，避免卡住，Feign 默认超时可在配置中调整）
      Boolean healthResponse = workerService.healthCheck();

      // 2. 根据 worker 返回的状态判断健康状态
      if (healthResponse) {
        return Health.up()
            .withDetail("service", "worker-service")
            .withDetail("status", "worker 服务正常")
            .build();
      } else {
        return Health.down()
            .withDetail("service", "worker-service")
            .withDetail("status", "worker 服务状态异常")
            .withDetail("reason", "worker 返回状态：" + healthResponse)
            .build();
      }
    } catch (Exception e) {
      // 3. 调用失败（网络不通、worker 宕机等），返回 DOWN 状态
      return Health.down(e)
          .withDetail("service", "worker-service")
          .withDetail("reason", "worker 服务调用失败：" + e.getMessage())
          .build();
    }
  }
}
