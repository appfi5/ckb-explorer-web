package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.DaoContractResponse;
import com.ckb.explorer.facade.IContractCacheFacade;
import com.ckb.explorer.service.DaoContractService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContractCacheFacadeImpl implements IContractCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private DaoContractService daoContractService;

  // 缓存键前缀
  public static final String CACHE_KEY_PREFIX = "dao_contract:";
  // 缓存版本
  public static final String CACHE_VERSION = "v1";
  @Override
  public DaoContractResponse getDaoContract() {

    // 构建缓存键
    String cacheKey = buildCacheKey();

    // 使用CacheUtils获取缓存数据，3秒防缓存击穿
    // 这里使用3秒的race_condition_ttl对应Ruby代码中的配置
    return cacheUtils.getCache(
        cacheKey,
        daoContractService::getDefaultContract,
        3,
        TimeUnit.SECONDS
    );
  }

  /**
   * 构建缓存键
   * 对应Ruby代码中的dao_contract.cache_key
   *
   * @return 缓存键
   */
  public String buildCacheKey() {
    return CACHE_KEY_PREFIX + CACHE_VERSION;
  }
}
