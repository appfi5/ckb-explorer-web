package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.DistributionDataResponse;
import com.ckb.explorer.entity.RollingAvgBlockTime;
import com.ckb.explorer.facade.IDistributionDataCacheFacade;
import com.ckb.explorer.service.DistributionDataService;
import com.ckb.explorer.util.CacheUtils;
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
  private CacheUtils cacheUtils;

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private DistributionDataService distributionDataService;

  private static final String CACHE_PREFIX = "distribution_data:";
  private static final String CACHE_VERSION = "v1";
  private static final String CACHE_KEY = "average_block_time";
  // 缓存 TTL: 1小时
  private static final long TTL_SECONDS = 60 * 60;

  @Override
  public DistributionDataResponse getDistributionDataByIndicator(String indicator) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:%s", CACHE_PREFIX, CACHE_VERSION, indicator);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(indicator),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  @Override
  public DistributionDataResponse getAverageBlockTime() {

    RBucket<String> bucket = redissonClient.getBucket(CACHE_KEY);
    if (bucket == null || StringUtils.isEmpty(bucket.get())) {
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