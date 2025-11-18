package com.ckb.explorer.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.annotation.Resource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * RisingWave数据库健康检查指示器 通过DataSourceHealthIndicator检查RisingWave数据源的健康状态
 */
@Component("risingWaveHealth")
public class RisingWaveHealthIndicator implements HealthIndicator {

  @Resource
  private JdbcTemplate jdbcTemplate; // 动态数据源会自动注入「多数据源兼容的 JdbcTemplate」

  // 你的 RisingWave 数据源名称（必须和配置文件中一致：spring.datasource.dynamic.datasource.risingwave）
  private static final String RISINGWAVE_DS_NAME = "risingwave";

  @Override
  public Health health() {
    // 保存当前数据源名称（避免切换后影响其他业务）
    String currentDsName = DynamicDataSourceContextHolder.peek();
    try {
      // 1. 切换到 RisingWave 数据源
      DynamicDataSourceContextHolder.push(RISINGWAVE_DS_NAME);

      // 2. 执行简单查询验证连接（RisingWave 兼容 PostgreSQL 协议）
      String validationSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'live_cells'";
      Integer tableCount = jdbcTemplate.queryForObject(validationSql, Integer.class);

      // 4. 返回健康状态（UP + 详细信息）
      return Health.up()
          .withDetail("datasource", RISINGWAVE_DS_NAME)
          .withDetail("database", "RisingWave")
          .withDetail("live_cells_materialized_view_exists", tableCount != null && tableCount > 0)
          .build();

    } catch (Exception e) {
      // 连接失败或查询异常，返回 DOWN 状态 + 异常信息
      return Health.down(e)
          .withDetail("datasource", RISINGWAVE_DS_NAME)
          .withDetail("reason", "RisingWave 连接失败或服务异常")
          .withDetail("error_msg", e.getMessage())
          .build();
    } finally {
      // 5. 切换回原来的数据源（关键！避免污染其他业务的数据源上下文）
      DynamicDataSourceContextHolder.poll();
      if (currentDsName != null) {
        DynamicDataSourceContextHolder.push(currentDsName);
      }
    }
  }
}