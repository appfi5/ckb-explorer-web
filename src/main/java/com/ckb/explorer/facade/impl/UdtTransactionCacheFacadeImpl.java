package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.facade.IUdtTransactionCacheFacade;
import com.ckb.explorer.service.CkbTransactionService;
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
 * UdtTransactionCacheFacadeImpl 实现了 IUdtTransactionCacheFacade 接口，提供UDT交易缓存相关的具体实现
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class UdtTransactionCacheFacadeImpl implements IUdtTransactionCacheFacade {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CkbTransactionService transactionService;

    private static final String UDT_TRANSACTIONS_CACHE_PREFIX = "udt:transactions:";
    private static final String CACHE_VERSION = "v1";

    // 缓存 TTL：10 秒
    private static final long TTL_SECONDS = 10;
    private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
    // 防击穿锁等待时间
    private static final long LOCK_WAIT_TIME = 1;
    private static final long LOCK_LEASE_TIME = 8;

    @Override
    public Page<UdtTransactionPageResponse> getUdtTransactions(String typeHash, UdtTransactionsPageReq req) {

      req.setSort(StringUtils.defaultIfBlank(req.getSort(), "time.desc"));
      // 创建缓存键
        String cacheKey = String.format("%s%s:%s:sort:%s:page:%d:size:%d:txHash:%s:addressHash:%s",
                UDT_TRANSACTIONS_CACHE_PREFIX, CACHE_VERSION, typeHash, req.getSort(), req.getPage(), req.getPageSize(),req.getTxHash(),req.getAddressHash());

        RBucket<Page<UdtTransactionPageResponse>> bucket = redissonClient.getBucket(cacheKey);

        // 1. 先尝试读缓存
        Page<UdtTransactionPageResponse> cached = bucket.get();
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
                    Page<UdtTransactionPageResponse> result = loadFromDatabase(typeHash,req);

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
                Page<UdtTransactionPageResponse> result = loadFromDatabase(typeHash, req);
                bucket.set(result, Duration.ofMillis(TTL_MILLIS));
                return result;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 降级处理
            return loadFromDatabase(typeHash, req);
        }
    }

    /**
     * 从数据库加载地址的交易列表
     */
    private Page<UdtTransactionPageResponse> loadFromDatabase(String typeScriptHash, UdtTransactionsPageReq req) {
        return transactionService.getUdtTransactions(typeScriptHash, req);
    }
}