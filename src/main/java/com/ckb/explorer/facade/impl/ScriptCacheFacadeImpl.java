package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.facade.IScriptCacheFacade;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScriptCacheFacadeImpl implements IScriptCacheFacade {

  @Resource
  private ScriptService scriptService;

  @Resource
  private CacheUtils cacheUtils;

  private static final String ADDRESS_CACHE_PREFIX = "address:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;

  @Override
  public AddressResponse getAddressInfo(String address) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:address:%s", ADDRESS_CACHE_PREFIX, CACHE_VERSION,address);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(address),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );

  }

  private AddressResponse loadFromDatabase(String address) {
    return scriptService.getAddressInfo(address);
  }
}
