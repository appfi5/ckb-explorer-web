package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.facade.ICellInfoCacheFacade;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class CellInfoCacheFacadeImpl implements ICellInfoCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private OutputService outputService;

  private static final String Cell_INFO_CACHE_PREFIX = "cellInfo:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;

  @Override
  public CellInfoResponse findByOutputId(String id) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:id:%s", Cell_INFO_CACHE_PREFIX, CACHE_VERSION, id);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(id),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private CellInfoResponse loadFromDatabase(String id){

    return outputService.getCellInfo(Long.parseLong(id));
  }
}
