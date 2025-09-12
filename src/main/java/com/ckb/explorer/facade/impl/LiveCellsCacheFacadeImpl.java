package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.LiveCellsResponse;
import com.ckb.explorer.facade.ILiveCellsCacheFacade;
import com.ckb.explorer.service.LiveCellsService;
import jakarta.annotation.Resource;
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
public class LiveCellsCacheFacadeImpl implements ILiveCellsCacheFacade {
  @Resource
  private RedissonClient redissonClient;
  @Resource
  private LiveCellsService liveCellsService;

  private static final String LIVE_CELL_CACHE_PREFIX = "liveCell:page:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public Page<LiveCellsResponse> getAddressLiveCellsByAddress(String address, String typeHash,
      int page, int pageSize) {
    typeHash = StringUtils.isEmpty(typeHash)?"ckb":typeHash;
    // 创建缓存键
    String cacheKey = String.format("%s%s:address:%s:typeHash:%s:page:%d:size:%d", 
        LIVE_CELL_CACHE_PREFIX, CACHE_VERSION, address, typeHash, page, pageSize);
    
    RBucket<Page<LiveCellsResponse>> bucket = redissonClient.getBucket(cacheKey);
    
    // 1. 尝试从缓存获取数据
    Page<LiveCellsResponse> cached = bucket.get();
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
          Page<LiveCellsResponse> result = loadFromDatabase(address, typeHash, page, pageSize);
          
          // 写入缓存
          bucket.set(result, TTL_SECONDS, TimeUnit.SECONDS);
          
          return result;
        } finally {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
        }
      } else {
        // 获取锁失败，降级：直接查库
        log.warn("Failed to acquire lock for live cells of address: {}, typeHash: {}", address, typeHash);
        Page<LiveCellsResponse> result = loadFromDatabase(address, typeHash, page, pageSize);
        bucket.set(result, TTL_SECONDS, TimeUnit.SECONDS);
        return result;
      }
    } catch (InterruptedException e) {
      log.error("Interrupted while trying to acquire lock for live cells", e);
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(address, typeHash, page, pageSize);
    }
  }
  
  /**
   * 从数据库加载Live Cells数据
   * @param address 地址哈希
   * @param typeHash type script哈希
   * @param page 页码
   * @param pageSize 每页大小
   * @return Live Cells分页数据
   */
  private Page<LiveCellsResponse> loadFromDatabase(String address, String typeHash, int page, int pageSize) {

      return liveCellsService.getLiveCellsByAddressWithTypeHash(address, typeHash, page, pageSize);
  }
}
