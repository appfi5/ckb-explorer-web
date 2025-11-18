package com.ckb.explorer.feign;


import com.ckb.explorer.config.FeignLogConfig;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "worker", url = "${remote.worker.url}", configuration = FeignLogConfig.class)
public interface WorkerService {

  @Operation(summary = "重新更新每日统计的指定字段")
  @PostMapping(value ="/internal/daily_statistics/reset", produces = "application/json; charset=UTF-8")
  Boolean reset(@RequestParam String field);

  @Operation(summary = "手动触发每日统计任务")
  @PostMapping(value ="/internal/daily_statistics/manual_trigger", produces = "application/json; charset=UTF-8")
  Boolean manualTriggerDailyStatistics(@RequestParam(required = false) LocalDate startDate);

  @Operation(summary = "手动触发每日统计任务")
  @PostMapping(value ="/internal/udt_daily_statistics/manual_trigger", produces = "application/json; charset=UTF-8")
  Boolean manualTriggerUdtDailyStatistics(@RequestParam(required = false) LocalDate startDate);

  @Operation(summary = "服务健康检查")
  @GetMapping(value ="/internal/health_check")
  Boolean healthCheck();
}
