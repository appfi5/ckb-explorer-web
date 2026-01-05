package com.ckb.explorer.facade.impl;

import static com.ckb.explorer.constants.CommonConstantsKey.CACHE_VERSION;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.PendingTransactionPageResponse;
import com.ckb.explorer.facade.ICkbPendingTransactionCacheFacade;
import com.ckb.explorer.service.CkbPendingTransactionService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CkbPendingTransactionCacheFacadeImpl implements ICkbPendingTransactionCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private CkbPendingTransactionService ckbPendingTransactionService;

  private static final String TRANSACTIONS_LIST_CACHE_PREFIX = "pending:transactions:page:";

  // 缓存 TTL：10 秒
  private static final long TTL_SECONDS = 10;

  @Override
  public Page<PendingTransactionPageResponse> getPendingTransactionsByPage(Integer page,
      Integer size) {

    // 创建缓存键
    String cacheKey = String.format("%s%s:page:%d:size:%d",
        TRANSACTIONS_LIST_CACHE_PREFIX, CACHE_VERSION, page, size);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> ckbPendingTransactionService.getCkbPendingTransactionsByPage(page, size),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }
}
