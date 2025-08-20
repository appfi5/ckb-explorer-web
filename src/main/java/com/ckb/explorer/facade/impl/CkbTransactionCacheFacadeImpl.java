package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import com.ckb.explorer.mapstruct.CkbTransactionConvert;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * CkbTransactionCacheFacadeImpl 实现了 ICkbTransactionCacheFacade 接口，提供交易缓存相关的具体实现
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class CkbTransactionCacheFacadeImpl implements ICkbTransactionCacheFacade {

  @Autowired
  private RedissonClient redissonClient;

  @Autowired
  private CkbTransactionService ckbTransactionService;

  @Resource
  private I18n i18n;

  private static final String TRANSACTION_CACHE_PREFIX = "transaction:";
  private static final String TRANSACTIONS_LIST_CACHE_PREFIX = "transactions:page:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL：5 秒
  private static final long TTL_SECONDS = 5;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);

  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public Page<TransactionPageResponse> getTransactionsByPage(Integer page, Integer size,
      String sort) {
    // 设置默认排序
    sort = !StringUtils.isEmpty(sort) ? sort : "id.desc";

    // 创建缓存键
    String cacheKey = String.format("%s%s:page:%d:size:%d:sort:%s",
        TRANSACTIONS_LIST_CACHE_PREFIX, CACHE_VERSION, page, size, sort);

    RBucket<Page<TransactionPageResponse>> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    Page<TransactionPageResponse> cached = bucket.get();
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
          Page<TransactionPageResponse> result = loadFromDatabase(page, size, sort);

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
        Page<TransactionPageResponse> result = loadFromDatabase(page, size, sort);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(page, size, sort);
    }
  }

  /**
   * 从数据库加载交易列表
   */
  private Page<TransactionPageResponse> loadFromDatabase(Integer page, Integer pageSize,
      String sort) {

    // 执行分页查询
    Page<CkbTransaction> pageResult = ckbTransactionService.getCkbTransactionsByPage(page, pageSize,
        sort);
    // 转换为响应对象
    return CkbTransactionConvert.INSTANCE.toConvertPage(pageResult);
  }

  @Override
  public TransactionPageResponse getTransactionByHash(String txHash) {
    return null;
  }

  /**
   * 从数据库加载交易详情
   *
   * @param txHash 交易哈希
   * @return 交易详情响应
   */
  private TransactionPageResponse loadTransactionFromDatabase(String txHash) {
    // 实际项目中需要调用DAO或Repository层获取数据
    // 这里仅作为示例返回null
    return null;
  }

}