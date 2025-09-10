package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.DistributionDataResponse;
import com.ckb.explorer.entity.RollingAvgBlockTime;
import com.ckb.explorer.facade.IDistributionDataCacheFacade;
import com.ckb.explorer.service.DistributionDataService;
import com.ckb.explorer.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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
 * DistributionDataCacheFacadeImpl 实现IDistributionDataCacheFacade接口，提供分布数据的缓存操作
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class DistributionDataCacheFacadeImpl implements IDistributionDataCacheFacade {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private DistributionDataService distributionDataService;

    private static final String CACHE_PREFIX = "distribution_data:";
    private static final String CACHE_VERSION = "v1";
    private static final String CACHE_KEY = "average_block_time";
    // 缓存 TTL: 1小时
    private static final long TTL_HOURS = 1;
    private static final long TTL_MILLIS = TimeUnit.HOURS.toMillis(TTL_HOURS);
    // 防击穿锁等待时间
    private static final long LOCK_WAIT_TIME = 1;
    private static final long LOCK_LEASE_TIME = 8;

    @Override
    public DistributionDataResponse getDistributionDataByIndicator(String indicator) {
        // 创建缓存键
        String cacheKey = String.format("%s%s:%s", CACHE_PREFIX, CACHE_VERSION, indicator);

        RBucket<DistributionDataResponse> bucket = redissonClient.getBucket(cacheKey);

        // 1. 先尝试读缓存
      DistributionDataResponse cached = bucket.get();
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
                  DistributionDataResponse result = loadFromDatabase(indicator);

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
              DistributionDataResponse result = loadFromDatabase(indicator);
                bucket.set(result, Duration.ofMillis(TTL_MILLIS));
                return result;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 降级处理：直接查库
            return loadFromDatabase(indicator);
        }
    }

  @Override
  public DistributionDataResponse getAverageBlockTime() {

    RBucket<String> bucket = redissonClient.getBucket(CACHE_KEY);
    if(bucket==null || StringUtils.isEmpty(bucket.get())){
      DistributionDataResponse response = new DistributionDataResponse();
      response.setAverageBlockTime(new ArrayList<>());
      return response;
    }
    return JsonUtil.parseObject(bucket.get(), DistributionDataResponse.class);
  }

  private DistributionDataResponse loadFromDatabase(String indicator) {
        return distributionDataService.getDistributionDataByIndicator(indicator);
    }
}