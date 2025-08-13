package com.ckb.explorer.service;

import java.util.concurrent.TimeUnit;

public interface RedissonExampleService {
    /**
     * 设置缓存值
     * @param key 键
     * @param value 值
     * @param timeout 超时时间
     */
    void setValue(String key, Object value, long timeout);

    /**
     * 获取缓存值
     * @param key 键
     * @return 值
     */
    Object getValue(String key);

    /**
     * 删除缓存值
     * @param key 键
     * @return 是否删除成功
     */
    boolean deleteValue(String key);

    /**
     * 尝试获取锁
     * @param lockKey 锁键
     * @param waitTime 等待时间
     * @param leaseTime 租赁时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

    /**
     * 释放锁
     * @param lockKey 锁键
     */
    void unlock(String lockKey);
}