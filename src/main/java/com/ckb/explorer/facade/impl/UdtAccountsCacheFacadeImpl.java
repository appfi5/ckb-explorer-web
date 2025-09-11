package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import com.ckb.explorer.facade.IUdtAccountsCacheFacade;
import com.ckb.explorer.service.UdtAccountsService;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * UdtAccountsCacheFacadeImpl 实现IUdtAccountsCacheFacade接口，提供UDT账户余额的缓存操作
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class UdtAccountsCacheFacadeImpl implements IUdtAccountsCacheFacade {

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private UdtAccountsService udtAccountsService;

  private static final String CACHE_PREFIX = "address:udts:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL: 60秒
  private static final long TTL_SECONDS = 60;
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  /**
   * 获取地址的UDT余额列表
   *
   * @param address 地址哈希
   * @return UDT余额列表
   */
  @Override
  public List<AccountUdtBalanceResponse> getUdtBalance(String address) {
    String cacheKey = String.format("%s%s:address:%s", CACHE_PREFIX, CACHE_VERSION, address);

    // 1. 尝试从缓存获取数据
    RBucket<List<AccountUdtBalanceResponse>> bucket = redissonClient.getBucket(cacheKey);
    List<AccountUdtBalanceResponse> udtBalances = bucket.get();

    if (udtBalances != null) {
      return udtBalances;
    }

    // 2. 缓存未命中，获取分布式锁
    String lockKey = cacheKey + ":lock";
    RLock lock = redissonClient.getLock(lockKey);

    try {
      // 尝试获取锁
      if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
        try {
          // 双重检查，防止其他线程已经加载了数据
          udtBalances = bucket.get();
          if (udtBalances != null) {
            return udtBalances;
          }

          // 3. 从数据库加载数据
          udtBalances = loadFromDatabase(address);

          // 4. 放入缓存
          bucket.set(udtBalances, TTL_SECONDS, TimeUnit.SECONDS);
          return udtBalances;
        } finally {
          // 释放锁
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
        }
      } else {
        log.warn("Failed to acquire lock for udt balance of address: {}", address);
        udtBalances = loadFromDatabase(address);
        bucket.set(udtBalances, TTL_SECONDS, TimeUnit.SECONDS);
        return udtBalances;
      }
    } catch (InterruptedException e) {
      log.error("Interrupted while trying to acquire lock for udt balance", e);
      Thread.currentThread().interrupt();
      return loadFromDatabase(address);
    }
  }

  /**
   * 从数据库加载UDT余额数据
   *
   * @param address 地址哈希
   * @return UDT余额列表
   */
  private List<AccountUdtBalanceResponse> loadFromDatabase(String address) {

      return udtAccountsService.getUdtBalanceByAddress(address);

  }
}
