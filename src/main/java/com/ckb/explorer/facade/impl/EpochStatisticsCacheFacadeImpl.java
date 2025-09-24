package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import com.ckb.explorer.facade.IEpochStatisticsCacheFacade;
import com.ckb.explorer.service.EpochStatisticsService;
import com.ckb.explorer.util.CacheUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * EpochStatisticsCacheFacadeImpl 实现IEpochStatisticsCacheFacade接口，提供纪元统计数据的缓存操作
 */
@Component
@Slf4j
public class EpochStatisticsCacheFacadeImpl implements IEpochStatisticsCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private EpochStatisticsService epochStatisticsService;

  private static final String CACHE_PREFIX = "epoch_statistics:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL: 30分钟
  private static final long TTL_SECONDS = 30 * 60;

  @Override
  public List<EpochStatisticsResponse> getEpochStatistics(Integer limit, String indicator) {
    // 创建缓存键，包含limit和indicator参数
    String cacheKey = String.format("%s%s:limit:%s:indicator:%s",
        CACHE_PREFIX, CACHE_VERSION,
        limit == null ? "all" : limit.toString(),
        indicator);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(limit, indicator),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private List<EpochStatisticsResponse> loadFromDatabase(Integer limit, String indicator) {
    try {
      // 从服务层获取纪元统计数据
      return epochStatisticsService.getEpochStatistics(limit, indicator);
    } catch (Exception e) {
      log.error("Failed to load epoch statistics from database", e);
      // 降级处理：返回空列表
      return new ArrayList<>();
    }
  }
}