package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import com.ckb.explorer.facade.IDaoDepositorCacheFacade;
import com.ckb.explorer.mapper.DepositCellMapper;
import com.ckb.explorer.service.DepositCellService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DaoDepositorCacheFacadeImpl implements IDaoDepositorCacheFacade {
  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private DepositCellService depositCellService;

  private static final String DAO_DEPOSITOR_CACHE_PREFIX = "dao:depositor:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL：10 秒
  private static final long TTL_SECONDS = 10;
  @Override
  public List<DaoDepositorResponse> getTopDaoDepositors() {
    // 创建缓存键
    String cacheKey = String.format("%s%s",
        DAO_DEPOSITOR_CACHE_PREFIX, CACHE_VERSION);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> depositCellService.getTopDaoDepositors(),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }
}
