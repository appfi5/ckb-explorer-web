package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsResponse;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsStartEndTimeResponse;
import com.ckb.explorer.facade.IMinerDailyStatisticsCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/miner_daily_statistics")
public class MinerDailyStatisticsController {

  @Resource
  private IMinerDailyStatisticsCacheFacade minerDailyStatisticsCacheFacade;

  /**
   * 近30天的avgROR数据
   * @return
   */
  @GetMapping("/avg_ror")
  @Operation(summary = "获取矿工统计信息索引")
  public ResponseEntity<ResponseInfo<List<MinerDailyStatisticsResponse>>> avgROR() {
    CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
        .cachePublic() // 允许公共缓存（CDN、代理服务器等）
        .mustRevalidate()
        .staleWhileRevalidate(60, TimeUnit.MINUTES);
    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(ResponseInfo.SUCCESS(minerDailyStatisticsCacheFacade.getAvgRor()));
  }

  /**
   * 获取已统计的开始结束时间
   */
  @GetMapping("/start_end_time")
  @Operation(summary = "获取已统计的开始结束时间")
public ResponseEntity<ResponseInfo<MinerDailyStatisticsStartEndTimeResponse>> getStartEndTime() {
    CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
        .cachePublic() // 允许公共缓存（CDN、代理服务器等）
        .mustRevalidate()
        .staleWhileRevalidate(60, TimeUnit.MINUTES);
    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(ResponseInfo.SUCCESS(minerDailyStatisticsCacheFacade.getStartEndTime()));
}

  /**
   * 获取指定日期的统计数据
   */
  @GetMapping("/{date}")
  @Operation(summary = "获取指定日期的统计数据")
  public ResponseEntity<ResponseInfo<MinerDailyStatisticsResponse>> getByDate(@PathVariable("date") LocalDate date) {
    // 验证查询参数
    if (date == null) {
      return ResponseEntity.badRequest().body(ResponseInfo.FAILURE("Invalid date"));
    }
    CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
        .cachePublic() // 允许公共缓存（CDN、代理服务器等）
        .mustRevalidate()
        .staleWhileRevalidate(60, TimeUnit.MINUTES);
    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(ResponseInfo.SUCCESS(minerDailyStatisticsCacheFacade.getByDate(date)));
  }
}
