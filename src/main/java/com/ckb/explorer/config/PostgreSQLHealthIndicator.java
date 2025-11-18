package com.ckb.explorer.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.annotation.Resource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("postgreSQLHealth")
public class PostgreSQLHealthIndicator implements HealthIndicator {

  @Resource
  private JdbcTemplate jdbcTemplate;

  private static final String POSTGRES_DS_NAME = "postgres"; // 和配置文件中主数据源名称一致

  @Override
  public Health health() {
    String currentDsName = com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder.peek();
    try {
      DynamicDataSourceContextHolder.push(POSTGRES_DS_NAME);

      // 验证 PostgreSQL 连接 + 检查业务表（如block 表）
      String validationSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'block'";
      Integer tableCount = jdbcTemplate.queryForObject(validationSql, Integer.class);

      return Health.up()
          .withDetail("datasource", POSTGRES_DS_NAME)
          .withDetail("database", "PostgreSQL")
          .withDetail("block_table_exists", tableCount != null && tableCount > 0)
          .build();

    } catch (Exception e) {
      return Health.down(e)
          .withDetail("datasource", POSTGRES_DS_NAME)
          .withDetail("reason", "PostgreSQL 连接失败或表不存在")
          .build();
    } finally {
      DynamicDataSourceContextHolder.poll();
      if (currentDsName != null) {
        DynamicDataSourceContextHolder.push(currentDsName);
      }
    }
  }
}
