package com.ckb.explorer.controller;

import com.ckb.explorer.feign.WorkerService;
import com.ckb.explorer.common.dto.ResponseInfo;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Resource;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Hidden
@RestController
@RequestMapping("/api/v1/manual_trigger")
public class ResetDailyStatisticsController {

  @Resource
  WorkerService workerService;

  /**
   * 手动重置每日统计任务的个别字段
   * @param field
   * @return
   */
  @PostMapping("reset_daily_statistics")
  public ResponseInfo<Boolean> reset(@RequestParam String field) {
    // 验证查询参数
    if(field == null || field.isEmpty()){
      return ResponseInfo.FAILURE("field is empty");
    }

    // 从缓存门面获取数据
    var result = workerService.reset(field);

    // 返回成功响应
    return ResponseInfo.SUCCESS(result);
  }

  /**
   * 手动触发每日统计任务
   * @param startDate
   * @return
   */
  @PostMapping("daily_statistics")
  public ResponseInfo<Boolean> manualTriggerDailyStatistics(@RequestParam(required = false) LocalDate startDate) {

    var result = workerService.manualTriggerDailyStatistics(startDate);

    // 返回成功响应
    return ResponseInfo.SUCCESS(result);
  }

  /**
   * 手动触发每日统计任务
   * @param startDate
   * @return
   */
  @PostMapping("udt_daily_statistics")
  public ResponseInfo<Boolean> manualTriggerUdtDailyStatistics(@RequestParam(required = false) LocalDate startDate) {

    var result = workerService.manualTriggerUdtDailyStatistics(startDate);

    // 返回成功响应
    return ResponseInfo.SUCCESS(result);
  }


}
