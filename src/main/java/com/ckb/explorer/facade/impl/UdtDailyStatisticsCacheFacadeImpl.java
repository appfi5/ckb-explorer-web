package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.UdtDailyStatisticsResponse;
import com.ckb.explorer.facade.IUdtDailyStatisticsCacheFacade;
import com.ckb.explorer.service.UdtDailyStatisticsService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class UdtDailyStatisticsCacheFacadeImpl implements IUdtDailyStatisticsCacheFacade {
  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private UdtDailyStatisticsService udtDailyStatisticsService;

  private static final String CACHE_PREFIX = "udt:daily:statistics:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL: 15分钟
  private static final long TTL_SECONDS = 15 * 60;
  @Override
  public List<UdtDailyStatisticsResponse> index() {
    // 创建缓存键
    String cacheKey = String.format("%s%s", CACHE_PREFIX,
        CACHE_VERSION);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        this::loadFromDatabase,  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );

  }

  private List<UdtDailyStatisticsResponse> loadFromDatabase() {
    return udtDailyStatisticsService.getUdtDailyStatistics();
  }
}
