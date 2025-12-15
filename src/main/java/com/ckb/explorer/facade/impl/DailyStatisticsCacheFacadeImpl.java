package com.ckb.explorer.facade.impl;

import static com.ckb.explorer.constants.CommonConstantsKey.CACHE_VERSION;
import static com.ckb.explorer.constants.CommonConstantsKey.ONE_HOUR_TTL_SECONDS;

import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import com.ckb.explorer.facade.DailyStatisticsCacheFacade;
import com.ckb.explorer.service.DailyStatisticsService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * DailyStatisticsCacheFacadeImpl 实现了 DailyStatisticsCacheFacade 接口，提供每日统计数据缓存相关的具体实现
 */
@Component
public class DailyStatisticsCacheFacadeImpl implements DailyStatisticsCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private DailyStatisticsService dailyStatisticsService;

  private static final String DAILY_STATISTICS_CACHE_PREFIX = "daily:statistics:";


  @Override
  public List<DailyStatisticResponse> getDailyStatisticsByIndicator(String indicator) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:indicator:%s", DAILY_STATISTICS_CACHE_PREFIX,
        CACHE_VERSION, indicator);

    return cacheUtils.getCacheWithoutLock(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(indicator),  // 数据加载函数
        ONE_HOUR_TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private List<DailyStatisticResponse> loadFromDatabase(String indicator) {
    // 根据指标名称从数据库加载数据
    return dailyStatisticsService.getByIndicator(indicator);
  }
}