package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import com.ckb.explorer.facade.DailyStatisticsCacheFacade;
import com.ckb.explorer.service.DailyStatisticsService;
import jakarta.annotation.Resource;
import java.util.List;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * DailyStatisticsCacheFacadeImpl 实现了 DailyStatisticsCacheFacade 接口，提供每日统计数据缓存相关的具体实现
 */
@Component
@Transactional(readOnly = true)
public class DailyStatisticsCacheFacadeImpl implements DailyStatisticsCacheFacade {

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private DailyStatisticsService dailyStatisticsService;

  private static final String DAILY_STATISTICS_CACHE_PREFIX = "daily:statistics:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60 * 60;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public List<DailyStatisticResponse> getDailyStatisticsByIndicator(String indicator) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:indicator:%s", DAILY_STATISTICS_CACHE_PREFIX,
        CACHE_VERSION, indicator);

    RBucket<List<DailyStatisticResponse>> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    List<DailyStatisticResponse> cached = bucket.get();
    if (cached != null) {
      return cached;
    }

    // 2. 缓存未命中，使用分布式锁防止击穿
    String lockKey = cacheKey + ":lock";
    RLock lock = redissonClient.getLock(lockKey);

    try {
      // 双重检查
      cached = bucket.get();
      if (cached != null) {
        return cached;
      }

      if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
        try {
          // 再次检查
          cached = bucket.get();
          if (cached != null) {
            return cached;
          }

          // 真正加载数据
          List<DailyStatisticResponse> result = loadFromDatabase(indicator);

          // 写入缓存
          bucket.set(result, Duration.ofMillis(TTL_MILLIS));

          return result;
        } finally {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
        }
      } else {
        // 获取锁失败，降级：直接查库
        List<DailyStatisticResponse> result = loadFromDatabase(indicator);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(indicator);
    }
  }

  private List<DailyStatisticResponse> loadFromDatabase(String indicator) {
    // 根据指标名称从数据库加载数据
    return dailyStatisticsService.getByIndicator(indicator);
  }
}