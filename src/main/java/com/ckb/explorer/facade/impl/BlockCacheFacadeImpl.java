package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.mapstruct.BlockConvert;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class BlockCacheFacadeImpl implements IBlockCacheFacade {

  @Autowired
  private RedissonClient redissonClient;

  @Autowired
  private BlockService blockService;

  @Resource
  private I18n i18n;

  // 缓存 TTL：5 秒
  private static final long TTL_SECONDS = 5;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);

  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  private static final String CACHE_PREFIX = "ckb:blocks:";
  private static final String CACHE_VERSION = "v1";

  @Override
  public Page<BlockListResponse> getBlocksByPage(int page,
      int pageSize, String sort) {
    sort = !StringUtils.isEmpty(sort) ? sort : "blockNumber.desc";
    String cacheKey = String.format("%s%s:page:%d:size:%d:sort:%s",
        CACHE_PREFIX, CACHE_VERSION, page, pageSize, sort);

    RBucket<Page<BlockListResponse>> bucket = redissonClient.getBucket(
        cacheKey);

    // 1. 先尝试读缓存
    Page<BlockListResponse> cached = bucket.get();
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
        // 再次检查
        cached = bucket.get();
        if (cached != null) {
          return cached;
        }

        // 真正加载数据
        Page<BlockListResponse> result = loadFromDatabase(page,
            pageSize, sort);

        // 写入缓存
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));

        return result;
      } else {
        // 获取锁失败，降级：直接查库
        Page<BlockListResponse> result = loadFromDatabase(page,
            pageSize, sort);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(page, pageSize, sort);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  @Override
  public BlockResponse findBlock(String id) {
    String cacheKey = String.format("%s%s:block:%s", CACHE_PREFIX, CACHE_VERSION, id);

    RBucket<BlockResponse> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    BlockResponse cached = bucket.get();
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
        // 再次检查
        cached = bucket.get();
        if (cached != null) {
          return cached;
        }

        // 真正加载数据
        BlockResponse result = loadBlockFromDatabase(id);

        // 写入缓存 对应Rails.cache.fetch([name, query_key], race_condition_ttl: 3.seconds, expires_in: 30.minutes)
        bucket.set(result, Duration.ofMinutes(30));

        return result;
      } else {
        // 获取锁失败，降级：直接查库
        BlockResponse result = loadBlockFromDatabase(id);
        bucket.set(result, Duration.ofMinutes(30));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadBlockFromDatabase(id);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  /**
   * 从数据库加载区块数据
   * @param id 区块ID（区块哈希或区块号）
   * @return 区块响应信息
   */
  private BlockResponse loadBlockFromDatabase(String id) {

    return blockService.getBlock(id);
  }

  private Page<BlockListResponse> loadFromDatabase(
      int page, int pageSize, String sort) {
    // 执行分页查询
    Page<Block> pageResult = blockService.getBlocksByPage(page, pageSize, sort);

    return BlockConvert.INSTANCE.toConvertPage(pageResult);

  }
}
