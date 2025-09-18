package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.LiveCellsResponse;
import com.ckb.explorer.facade.ILiveCellsCacheFacade;
import com.ckb.explorer.service.LiveCellsService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(readOnly = true)
public class LiveCellsCacheFacadeImpl implements ILiveCellsCacheFacade {
  @Resource
  private CacheUtils cacheUtils;
  @Resource
  private LiveCellsService liveCellsService;

  private static final String LIVE_CELL_CACHE_PREFIX = "liveCell:page:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;

  @Override
  public Page<LiveCellsResponse> getAddressLiveCellsByAddress(String address, String typeHash,
      int page, int pageSize) {
    typeHash = StringUtils.isEmpty(typeHash)?"ckb":typeHash;
    // 创建缓存键
    String cacheKey = String.format("%s%s:address:%s:typeHash:%s:page:%d:size:%d", 
        LIVE_CELL_CACHE_PREFIX, CACHE_VERSION, address, typeHash, page, pageSize);

    String finalTypeHash = typeHash;
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(address, finalTypeHash, page, pageSize),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }
  
  /**
   * 从数据库加载Live Cells数据
   * @param address 地址哈希
   * @param typeHash type script哈希
   * @param page 页码
   * @param pageSize 每页大小
   * @return Live Cells分页数据
   */
  private Page<LiveCellsResponse> loadFromDatabase(String address, String typeHash, int page, int pageSize) {

      return liveCellsService.getLiveCellsByAddressWithTypeHash(address, typeHash, page, pageSize);
  }
}
