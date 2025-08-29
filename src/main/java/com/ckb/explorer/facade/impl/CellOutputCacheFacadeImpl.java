package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.AddressLiveCellsResponse;
import com.ckb.explorer.facade.ICellOutputCacheFacade;
import com.ckb.explorer.service.OutputService;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(readOnly = true)
public class CellOutputCacheFacadeImpl implements ICellOutputCacheFacade {
  @Resource
  private RedissonClient redissonClient;

  @Resource
  private OutputService outputService;

  private static final String ADDRESS_LIVE_CELL_CACHE_PREFIX = "addressLiveCell:page:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public Page<AddressLiveCellsResponse> getAddressLiveCellsByAddress(String address,
      String tag, String sort, Boolean boundStatus, int page, int pageSize) {
    // 设置默认排序
    sort = !StringUtils.isEmpty(sort) ? sort : "block_timestamp.desc";
    boundStatus = boundStatus == null? false:boundStatus;

    String cacheKey = String.format("%s%s:tag:%s:boundStatus:%s:page:%d:size:%d:sort:%s", ADDRESS_LIVE_CELL_CACHE_PREFIX, CACHE_VERSION, tag, boundStatus, page, pageSize, sort);

    RBucket<Page<AddressLiveCellsResponse>> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    Page<AddressLiveCellsResponse> cached = bucket.get();
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
          Page<AddressLiveCellsResponse> result = loadFromDatabase(address, tag, sort, boundStatus, page, pageSize);

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
        Page<AddressLiveCellsResponse> result = loadFromDatabase(address, tag, sort, boundStatus, page, pageSize);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(address, tag, sort, boundStatus, page, pageSize);
    }
  }
  private Page<AddressLiveCellsResponse> loadFromDatabase(String address,
      String tag, String sort, Boolean boundStatus, int page, int pageSize) {
    return outputService.getAddressLiveCellsByAddress(address, tag, sort, boundStatus, page, pageSize);
  }
}
