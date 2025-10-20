package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.UdtDailyStatisticsResponse;
import com.ckb.explorer.facade.IUdtDailyStatisticsCacheFacade;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/udt_hourly_statistics")
public class UdtHourlyStatisticsController {

  @Resource
  private I18n i18n;

  @Resource
  private IUdtDailyStatisticsCacheFacade udtDailyStatisticsCacheFacade;

  @GetMapping
  public ResponseInfo<List<UdtDailyStatisticsResponse>> index() {

    // 从缓存门面获取数据
    List<UdtDailyStatisticsResponse> dailyStatistics = udtDailyStatisticsCacheFacade.index();

    // 返回成功响应
    return ResponseInfo.SUCCESS(dailyStatistics);
  }
}
