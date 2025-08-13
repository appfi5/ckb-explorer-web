package com.ckb.explorer.service.impl;

import com.ckb.explorer.service.RedissonExampleService;
import java.time.Duration;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedissonExampleServiceImpl implements RedissonExampleService {

  @Autowired
  private RedissonClient redissonClient;

  @Override
  public void setValue(String key, Object value, long timeout) {
    redissonClient.getBucket(key).set(value, Duration.ofSeconds(timeout));
  }

  @Override
  public Object getValue(String key) {
    return redissonClient.getBucket(key).get();
  }

  @Override
  public boolean deleteValue(String key) {
    return redissonClient.getBucket(key).delete();
  }

  @Override
  public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      return lock.tryLock(waitTime, leaseTime, unit);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }

  @Override
  public void unlock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    if (lock.isHeldByCurrentThread()) {
      lock.unlock();
    }
  }
}