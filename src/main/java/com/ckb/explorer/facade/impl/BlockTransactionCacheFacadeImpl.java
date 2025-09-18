package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockTransactionPageResponse;
import com.ckb.explorer.facade.IBlockTransactionCacheFacade;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

/**
 * BlockTransactionCacheFacadeImpl 实现了 IBlockTransactionCacheFacade 接口，提供块内交易缓存相关的具体实现
 */
@Component
@Slf4j
@Transactional(readOnly = true)
public class BlockTransactionCacheFacadeImpl implements IBlockTransactionCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private CkbTransactionService transactionService;

  private static final String BLOCK_TRANSACTIONS_CACHE_PREFIX = "block:transactions:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL：10 秒
  private static final long TTL_SECONDS = 10;

  @Override
  public Page<BlockTransactionPageResponse> getBlockTransactions(String blockHash, String txHash,
      String addressHash, Integer page, Integer pageSize) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:blockHash:%s:txHash:%s:addressHash:%s:page:%d:size:%d",
        BLOCK_TRANSACTIONS_CACHE_PREFIX, CACHE_VERSION, blockHash,
        StringUtils.isEmpty(txHash) ? "empty" : txHash,
        StringUtils.isEmpty(addressHash) ? "empty" : addressHash,
        page, pageSize);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(blockHash, txHash, addressHash, page, pageSize),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  /**
   * 从数据库加载区块内的交易列表
   */
  private Page<BlockTransactionPageResponse> loadFromDatabase(String blockHash, String txHash,
      String addressHash, Integer page, Integer pageSize) {

    // 调用BlockTransactionService获取区块内的交易列表
    return transactionService.getBlockTransactions(blockHash, txHash, addressHash, page, pageSize);
  }
}