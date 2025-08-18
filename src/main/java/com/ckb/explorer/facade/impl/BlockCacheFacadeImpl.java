package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponsePageInfo;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.mapstruct.BlockConvert;
import com.ckb.explorer.service.BlockService;
import java.time.Duration;
import java.util.List;
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


  // 缓存 TTL：5 秒
  private static final long TTL_SECONDS = 5;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);

  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  private static final String CACHE_PREFIX = "ckb:blocks:";
  private static final String CACHE_VERSION = "v1";

  @Override
  public ResponsePageInfo<List<BaseResponse<BlockListResponse>>> getBlocksByPage(int page,
      int pageSize, String sort) {
    sort = !StringUtils.isEmpty(sort) ? sort : "block_number.desc";
    String cacheKey = String.format("%s%s:page:%d:size:%d:sort:%s",
        CACHE_PREFIX, CACHE_VERSION, page, pageSize, sort);

    RBucket<ResponsePageInfo<List<BaseResponse<BlockListResponse>>>> bucket = redissonClient.getBucket(
        cacheKey);

    // 1. 先尝试读缓存
    ResponsePageInfo<List<BaseResponse<BlockListResponse>>> cached = bucket.get();
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
        ResponsePageInfo<List<BaseResponse<BlockListResponse>>> result = loadFromDatabase(page,
            pageSize, sort);

        // 写入缓存
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));

        return result;
      } else {
        // 获取锁失败，降级：直接查库（不缓存，避免雪崩）
        return loadFromDatabase(page, pageSize, sort);
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

  private ResponsePageInfo<List<BaseResponse<BlockListResponse>>> loadFromDatabase(
      int page, int pageSize, String sort) {

    // 执行分页查询
    Page<Block> pageResult = blockService.getBlocksByPage(page, pageSize, sort);

    // 计算总页数
    int totalPages = (int) Math.ceil(pageResult.getTotal() / (double) pageResult.getSize());

    // TODO miner_hash的解码待确定
    List<BaseResponse<BlockListResponse>> blockList = BlockConvert.INSTANCE.toConvertList(
        pageResult.getRecords());
    // 使用静态SUCCESS方法构造响应
    return ResponsePageInfo.SUCCESS(
        blockList,
        pageResult.getTotal(),
        pageSize,
        totalPages
    );
  }
}
