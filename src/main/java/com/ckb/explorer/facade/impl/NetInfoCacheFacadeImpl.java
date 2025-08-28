package com.ckb.explorer.facade.impl;

import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.NetInfoResponse;
import com.ckb.explorer.facade.INetInfoCacheFacade;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.nervos.ckb.CkbRpcApi;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class NetInfoCacheFacadeImpl implements INetInfoCacheFacade {

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private CkbRpcApi ckbRpcApi;

  // 缓存 TTL
  private static final long TTL_HOURS = 4;

  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  private static final String CACHE_PREFIX = "node:info:version";
  private static final String CACHE_VERSION = "v1";
  @Override
  public NetInfoResponse getLocalNodeInfoVersion() {
    String cacheKey = String.format("%s%s", CACHE_PREFIX, CACHE_VERSION);

    RBucket<NetInfoResponse> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    NetInfoResponse cached = bucket.get();
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
        NetInfoResponse result = loadNodeInfoVersion();

        // 写入缓存
        bucket.set(result, Duration.ofHours(TTL_HOURS));

        return result;
      } else {
        // 获取锁失败，降级：直接查库
        NetInfoResponse result = loadNodeInfoVersion();
        bucket.set(result, Duration.ofHours(TTL_HOURS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadNodeInfoVersion();
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }
  private NetInfoResponse loadNodeInfoVersion() {
    NetInfoResponse response = new NetInfoResponse();
    try {
      var nodeInfo = ckbRpcApi.localNodeInfo();
      response.setVersion(nodeInfo.version);
    } catch (IOException e) {
      throw new ServerException(I18nKey.LOCAL_NODE_INFO_NOT_FOUND_CODE, I18nKey.LOCAL_NODE_INFO_NOT_FOUND_MESSAGE);
    }

    return response;
  }
}
