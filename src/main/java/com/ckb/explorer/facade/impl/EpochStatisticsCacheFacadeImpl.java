package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import com.ckb.explorer.facade.IEpochStatisticsCacheFacade;
import com.ckb.explorer.service.EpochStatisticsService;
import java.util.ArrayList;
import java.util.List;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * EpochStatisticsCacheFacadeImpl 实现IEpochStatisticsCacheFacade接口，提供纪元统计数据的缓存操作
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class EpochStatisticsCacheFacadeImpl implements IEpochStatisticsCacheFacade {

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private EpochStatisticsService epochStatisticsService;

  private static final String CACHE_PREFIX = "epoch_statistics:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL: 30分钟
  private static final long TTL_MINUTES = 30;
  private static final long TTL_MILLIS = TimeUnit.MINUTES.toMillis(TTL_MINUTES);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public List<EpochStatisticsResponse> getEpochStatistics(Integer limit, String indicator) {
    // 创建缓存键，包含limit和indicator参数
    String cacheKey = String.format("%s%s:limit:%s:indicator:%s",
        CACHE_PREFIX, CACHE_VERSION,
        limit == null ? "all" : limit.toString(),
        indicator);

    RBucket<List<EpochStatisticsResponse>> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    List<EpochStatisticsResponse> cached = bucket.get();
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
          List<EpochStatisticsResponse> result = loadFromDatabase(limit, indicator);

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
        List<EpochStatisticsResponse> result = loadFromDatabase(limit, indicator);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理：直接查库
      return loadFromDatabase(limit, indicator);
    }
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