package com.ckb.explorer.util;

import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * 缓存通用工具类，提供统一的缓存操作方法
 */
@Component
@Slf4j
public class CacheUtils {

  @Resource
  private RedissonClient redissonClient;

  // 防击穿锁等待时间（秒）
  private static final long DEFAULT_LOCK_WAIT_TIME = 1;
  // 防击穿锁持有时间（秒）
  private static final long DEFAULT_LOCK_LEASE_TIME = 8;
  private static final String LOCK_KEY_SUFFIX = ":lock";

  /**
   * 获取缓存数据，如果缓存不存在则通过supplier加载数据并存入缓存
   *
   * @param cacheKey 缓存键
   * @param supplier 数据加载函数
   * @param ttl      缓存过期时间
   * @param timeUnit 时间单位
   * @param <T>      缓存值泛型
   * @return 缓存数据
   */
  public <T> T getCache(String cacheKey, Supplier<T> supplier,
      long ttl, TimeUnit timeUnit) {
    return getCache(
        cacheKey, supplier, ttl, timeUnit, DEFAULT_LOCK_WAIT_TIME,
        DEFAULT_LOCK_LEASE_TIME
    );
  }


  /**
   * 获取缓存数据，允许自定义锁等待时间和持有时间
   *
   * @param cacheKey      缓存键
   * @param supplier      数据加载函数
   * @param ttl           缓存过期时间
   * @param timeUnit      时间单位
   * @param lockWaitTime  锁等待时间
   * @param lockLeaseTime 锁持有时间
   * @param <T>           缓存值泛型
   * @return 缓存数据
   */
  public <T> T getCache(String cacheKey, Supplier<T> supplier,
      long ttl, TimeUnit timeUnit, long lockWaitTime, long lockLeaseTime) {
    return getCacheWithLock(
        () -> redissonClient.getBucket(cacheKey),
        supplier, ttl, timeUnit, lockWaitTime, lockLeaseTime,
        cacheKey
    );
  }

  /**
   * 新增：支持“无锁模式”（针对非热点数据，避免锁开销） （业务可根据数据热度选择是否加锁）
   */
  public <T> T getCacheWithoutLock(String cacheKey, Class<T> valueType, Supplier<T> supplier,
      long ttl, TimeUnit timeUnit) {
    RBucket<T> bucket = redissonClient.getBucket(cacheKey);
    T cachedValue = bucket.get();
    if (cachedValue != null) {
      return cachedValue;
    }
    // 直接查库写缓存，不加锁
    cachedValue = supplier.get();
    if (cachedValue != null) {
      bucket.set(cachedValue, Duration.ofMillis(timeUnit.toMillis(ttl)));
    }
    return cachedValue;
  }

  /**
   * 手动清除指定的缓存
   *
   * @param cacheKey 缓存键
   */
  public void evictCache(String cacheKey) {
    RBucket<?> bucket = redissonClient.getBucket(cacheKey);
    bucket.delete();
    log.debug("Cache evicted for key: {}", cacheKey);
  }

  /**
   * 判断缓存是否存在
   *
   * @param cacheKey 缓存键
   * @return 是否存在
   */
  public boolean hasCache(String cacheKey) {
    RBucket<?> bucket = redissonClient.getBucket(cacheKey);
    return bucket.isExists();
  }

  /**
   * 公共加锁缓存逻辑（内部调用，对外不暴露）
   *
   * @param bucketSupplier 提供 RBucket 的函数（传入 Class/TypeReference）
   * @param dataSupplier   数据加载函数
   * @param ttl            缓存过期时间
   * @param timeUnit       时间单位
   * @param lockWaitTime   锁等待时间
   * @param lockLeaseTime  锁持有时间
   * @param cacheKey       缓存键（用于日志和锁键）
   * @param <T>            缓存值泛型
   * @return 缓存数据
   */
  private <T> T getCacheWithLock(Supplier<RBucket<T>> bucketSupplier, Supplier<T> dataSupplier,
      long ttl, TimeUnit timeUnit, long lockWaitTime, long lockLeaseTime,
      String cacheKey) {

    // 1. 获取带明确类型的 RBucket（关键：解决反序列化问题）
    RBucket<T> bucket = bucketSupplier.get();
    T cachedValue = bucket.get();

    // 2. 首次 缓存命中：直接返回
    if (cachedValue != null) {
      log.info("Cache hit (with lock), key: {}", cacheKey);
      return cachedValue;
    }

    // 3. 缓存未命中：初始化分布式锁
    String lockKey = cacheKey + LOCK_KEY_SUFFIX;
    RLock lock = redissonClient.getLock(lockKey);
    AtomicBoolean lockAcquired = new AtomicBoolean(false);
    try {

      // 3.1 双重检查：防止其他线程已加载数据
      cachedValue = bucket.get();
      if (cachedValue != null) {
        log.info("Cache hit (double check), key: {}", cacheKey);
        return cachedValue;
      }

      // 3.2 尝试获取锁
      if (lock.tryLock(lockWaitTime, lockLeaseTime, TimeUnit.SECONDS)) {
        lockAcquired.set(true);
        log.info("Acquired lock for cache key: {}", cacheKey);

        // 3.3 锁内再次检查：避免锁等待期间 数据已被加载
        cachedValue = bucket.get();
        if (cachedValue != null) {
          log.info("Cache hit (in lock), key: {}", cacheKey);
          return cachedValue;
        }

        // 3.4 查库加载数据
        log.info("Loading data from supplier for cache key: {}", cacheKey);
        cachedValue = dataSupplier.get();

        // 3.5 写缓存（仅非 null 值，避免缓存穿透）
        if (cachedValue != null) {
          long ttlMillis = timeUnit.toMillis(ttl);
          bucket.set(cachedValue, Duration.ofMillis(ttlMillis));
          log.info("Cache loaded and saved, key: {}, ttl: {} ms", cacheKey, ttlMillis);
        } else {
          log.info("Loaded null value from supplier, skip caching, key: {}", cacheKey);
        }
      } else {

        // 3.6 锁获取失败：降级查库（避免返回 null）
        log.warn("Failed to acquire lock for cache key: {}", cacheKey);
        cachedValue = dataSupplier.get();
        if (cachedValue != null) {
          bucket.set(cachedValue, Duration.ofMillis(timeUnit.toMillis(ttl)));
          log.info("Degraded load and saved cache, key: {}", cacheKey);
        }
      }
      return cachedValue;
    } catch (InterruptedException e) {

      // 3.7 线程中断：恢复中断状态 + 降级查库
      Thread.currentThread().interrupt();
      log.error("Thread interrupted while getting cache, key: {}", cacheKey, e);
      cachedValue = dataSupplier.get();
      if (cachedValue != null) {

        bucket.set(cachedValue, Duration.ofMillis(timeUnit.toMillis(ttl)));
      }

      return cachedValue;
    } finally {

      // 3.8 释放锁（仅当前线程持有锁时）
      if (lockAcquired.get() && lock.isHeldByCurrentThread()) {
        lock.unlock();
        log.info("Released lock for cache key: {}", cacheKey);
      }
    }
  }
}