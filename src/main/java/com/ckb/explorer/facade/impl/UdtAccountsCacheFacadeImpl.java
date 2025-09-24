package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import com.ckb.explorer.facade.IUdtAccountsCacheFacade;
import com.ckb.explorer.service.UdtAccountsService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * UdtAccountsCacheFacadeImpl 实现IUdtAccountsCacheFacade接口，提供UDT账户余额的缓存操作
 */
@Component
@Slf4j
public class UdtAccountsCacheFacadeImpl implements IUdtAccountsCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private UdtAccountsService udtAccountsService;

  private static final String CACHE_PREFIX = "address:udts:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL: 60秒
  private static final long TTL_SECONDS = 60;

  /**
   * 获取地址的UDT余额列表
   *
   * @param address 地址哈希
   * @return UDT余额列表
   */
  @Override
  public List<AccountUdtBalanceResponse> getUdtBalance(String address) {
    String cacheKey = String.format("%s%s:address:%s", CACHE_PREFIX, CACHE_VERSION, address);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(address),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
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
