package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.facade.IScriptCacheFacade;
import com.ckb.explorer.service.ScriptService;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(readOnly = true)
public class ScriptCacheFacadeImpl implements IScriptCacheFacade {
  @Resource
  private RedissonClient redissonClient;

  @Resource
  private ScriptService scriptService;

  private static final String ADDRESS_CACHE_PREFIX = "address:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public AddressResponse getAddressInfo(String address) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:address:%s", ADDRESS_CACHE_PREFIX, CACHE_VERSION,address);

    RBucket<AddressResponse> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    AddressResponse cached = bucket.get();
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
          AddressResponse result = loadFromDatabase(address);

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
        AddressResponse result = loadFromDatabase(address);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(address);
    }
  }

  private AddressResponse loadFromDatabase(String address) {
    return scriptService.getAddressInfo(address);
  }
}
