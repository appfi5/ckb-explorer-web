package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BlockCacheFacadeImpl implements IBlockCacheFacade {

  @Resource
  private BlockService blockService;

  @Resource
  private CacheUtils cacheUtils;

  // 缓存 TTL：10 秒
  private static final long TTL_SECONDS = 10;

  private static final long DETAIL_TTL_SECONDS = 5 * 60;

  private static final String CACHE_PREFIX = "ckb:blocks:";
  private static final String CACHE_VERSION = "v1";

  @Override
  public List<BlockListResponse> getHomePageBlocks(int size) {
    String cacheKey = String.format("%s%s:list:size:%d",
        CACHE_PREFIX, CACHE_VERSION, size);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(size),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }
  private List<BlockListResponse>  loadFromDatabase(int size){
    return blockService.getHomePageBlocks(size);
  }

  @Override
  public Page<BlockListResponse> getBlocksByPage(int page,
      int pageSize, String sort) {
    sort = !StringUtils.isEmpty(sort) ? sort : "blockNumber.desc";
    String cacheKey = String.format("%s%s:page:%d:size:%d:sort:%s",
        CACHE_PREFIX, CACHE_VERSION, page, pageSize, sort);

    String finalSort = sort;
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(page, pageSize, finalSort),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  @Override
  public BlockResponse findBlock(String id) {
    String cacheKey = String.format("%s%s:block:%s", CACHE_PREFIX, CACHE_VERSION, id);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadBlockFromDatabase(id),  // 数据加载函数
        DETAIL_TTL_SECONDS,               // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  /**
   * 从数据库加载区块数据
   * @param id 区块ID（区块哈希或区块号）
   * @return 区块响应信息
   */
  private BlockResponse loadBlockFromDatabase(String id) {

    return blockService.getBlock(id);
  }

  private Page<BlockListResponse> loadFromDatabase(
      int page, int pageSize, String sort) {
    // 执行分页查询
    return blockService.getBlocksByPage(page, pageSize, sort);
  }
}
