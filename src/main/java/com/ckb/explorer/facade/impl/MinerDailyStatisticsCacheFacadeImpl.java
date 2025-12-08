package com.ckb.explorer.facade.impl;

import static com.ckb.explorer.constants.CommonConstantsKey.CACHE_VERSION;
import static com.ckb.explorer.constants.CommonConstantsKey.ONE_HOUR_TTL_SECONDS;

import com.ckb.explorer.domain.resp.MinerDailyStatisticsResponse;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsStartEndTimeResponse;
import com.ckb.explorer.facade.IMinerDailyStatisticsCacheFacade;
import com.ckb.explorer.service.MinerDailyStatisticsService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MinerDailyStatisticsCacheFacadeImpl implements IMinerDailyStatisticsCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private MinerDailyStatisticsService minerDailyStatisticsService;

  private static final String MINER_DAILY_STATISTICS_CACHE_PREFIX = "miner:daily:statistics:";


  @Override
  public List<MinerDailyStatisticsResponse> getAvgRor() {
    // 创建缓存键
    String cacheKey = String.format("%s%s:indicator:avg_ror", MINER_DAILY_STATISTICS_CACHE_PREFIX,
        CACHE_VERSION);

    return cacheUtils.getCacheWithoutLock(
        cacheKey,                    // 缓存键
        () -> minerDailyStatisticsService.getAvgRor(),  // 数据加载函数
        ONE_HOUR_TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  @Override
  public MinerDailyStatisticsStartEndTimeResponse getStartEndTime() {
    // 创建缓存键
    String cacheKey = String.format("%s%s:start_end_time", MINER_DAILY_STATISTICS_CACHE_PREFIX,
        CACHE_VERSION);

    return cacheUtils.getCacheWithoutLock(
        cacheKey,                    // 缓存键
        () -> minerDailyStatisticsService.getStartEndTime(),  // 数据加载函数
        ONE_HOUR_TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  @Override
  public MinerDailyStatisticsResponse getByDate(LocalDate date) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:date:%s", MINER_DAILY_STATISTICS_CACHE_PREFIX,
        CACHE_VERSION, date);

    return cacheUtils.getCacheWithoutLock(
        cacheKey,                    // 缓存键
        () -> minerDailyStatisticsService.getByDate(date),  // 数据加载函数
        ONE_HOUR_TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }
}
