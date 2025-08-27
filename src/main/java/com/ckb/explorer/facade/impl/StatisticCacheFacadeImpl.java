package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ckb.explorer.domain.resp.EpochInfoResponse;
import com.ckb.explorer.domain.resp.IndexStatisticResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.facade.IStatisticCacheFacade;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.service.StatisticService;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(readOnly = true)
public class StatisticCacheFacadeImpl implements IStatisticCacheFacade {

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private BlockMapper blockMapper;

  @Resource
  private StatisticService statisticService;


  private static final String STATISTIC_CACHE_PREFIX = "statistic:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 15;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public IndexStatisticResponse getIndexStatistic() {
    // 创建缓存键
    String cacheKey = String.format("%s%s", STATISTIC_CACHE_PREFIX, CACHE_VERSION);

    RBucket<IndexStatisticResponse> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    IndexStatisticResponse cached = bucket.get();
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
          IndexStatisticResponse result = loadFromDatabase();

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
        IndexStatisticResponse result = loadFromDatabase();
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase();
    }
  }

  private IndexStatisticResponse loadFromDatabase() {
    IndexStatisticResponse response = new IndexStatisticResponse();
    LambdaQueryWrapper wrapper = new LambdaQueryWrapper<Block>().orderByDesc(Block::getId)
        .last("LIMIT 1");

    var tipBlock = blockMapper.selectOne(wrapper);

    var tipBlockNumber = tipBlock.getBlockNumber();
    // epochInfo
    EpochInfoResponse epochInfo = new EpochInfoResponse();
    epochInfo.setEpochNumber(tipBlock.getEpochNumber());
    epochInfo.setEpochLength(tipBlock.getEpochLength());
    epochInfo.setIndex(tipBlock.getBlockNumber() - tipBlock.getStartNumber());
    response.setEpochInfo(epochInfo);

    response.setTipBlockNumber(tipBlockNumber);
    var currentEpochDifficulty = Numeric.toBigInt(tipBlock.getDifficulty());
    response.setCurrentEpochDifficulty(currentEpochDifficulty);
    var averageBlockTime = statisticService.getAverageBlockTime(tipBlock.getBlockNumber(),
        tipBlock.getTimestamp());// TODO 后面考虑从统计表取，不临时计算
    response.setAverageBlockTime(String.format("%.2f", averageBlockTime)); // 保留两位小数
    var hashRate = statisticService.hashRate(tipBlockNumber);
    response.setHashRate(String.format("%.6f", hashRate));
    var estimatedEpochTime =
        currentEpochDifficulty.longValue() * tipBlock.getEpochLength() / hashRate;
    response.setEstimatedEpochTime(String.format("%.6f",
        estimatedEpochTime));
    response.setTransactionsLast24hrs(statisticService.getTransactionsLast24hrs(tipBlock.getTimestamp()));
    response.setTransactionsCountPerMinute(statisticService.getTransactionsCountPerMinute(tipBlockNumber));
    return response;
  }


}
