package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * CkbTransactionCacheFacadeImpl 实现了 ICkbTransactionCacheFacade 接口，提供交易缓存相关的具体实现
 */
@Component
@Slf4j
public class CkbTransactionCacheFacadeImpl implements ICkbTransactionCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private CkbTransactionService ckbTransactionService;

  private static final String TRANSACTION_CACHE_PREFIX = "transaction:";
  private static final String TRANSACTIONS_LIST_CACHE_PREFIX = "transactions:page:";
  private static final String TRANSACTIONS_INPUT_CACHE_PREFIX = "transactions:input:page:";
  private static final String TRANSACTIONS_OUTPUT_CACHE_PREFIX = "transactions:output:page:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL：10 秒
  private static final long TTL_SECONDS = 10;
  private static final long TTL_SECONDS_DETAIL = 30;

  @Override
  public List<TransactionPageResponse> getHomePageTransactions(int size) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:list:size:%d",
        TRANSACTION_CACHE_PREFIX, CACHE_VERSION, size);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(size),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private List<TransactionPageResponse> loadFromDatabase(Integer pageSize) {

    // 执行分页查询
    return ckbTransactionService.getHomePageTransactions(pageSize);
  }

  @Override
  public Page<TransactionPageResponse> getTransactionsByPage(Integer page, Integer size,
      String sort) {
    // 设置默认排序
    sort = !StringUtils.isEmpty(sort) ? sort : "id.desc";

    // 创建缓存键
    String cacheKey = String.format("%s%s:page:%d:size:%d:sort:%s",
        TRANSACTIONS_LIST_CACHE_PREFIX, CACHE_VERSION, page, size, sort);

    String finalSort = sort;
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(page, size, finalSort),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  /**
   * 从数据库加载交易列表
   */
  private Page<TransactionPageResponse> loadFromDatabase(Integer page, Integer pageSize,
      String sort) {

    // 执行分页查询
    return ckbTransactionService.getCkbTransactionsByPage(page, pageSize,
        sort);

  }

  @Override
  public TransactionResponse getTransactionByHash(String txHash) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:%s",
        TRANSACTION_CACHE_PREFIX, CACHE_VERSION, txHash);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadTransactionFromDatabase(txHash),  // 数据加载函数
        TTL_SECONDS_DETAIL,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  /**
   * 从数据库加载交易详情
   *
   * @param txHash 交易哈希
   * @return 交易详情响应
   */
  private TransactionResponse loadTransactionFromDatabase(String txHash) {
    // 调用service层方法获取交易数据
    return ckbTransactionService.getTransactionByHash(txHash);

  }

  @Override
  public Page<CellInputResponse> getDisplayInputs(String txHash,Integer page, Integer size) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:txHash:%s:page:%d:size:%d",
        TRANSACTIONS_INPUT_CACHE_PREFIX, CACHE_VERSION, txHash,page, size);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadInputFromDatabase(txHash, page, size),  // 数据加载函数
        TTL_SECONDS_DETAIL,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private Page<CellInputResponse> loadInputFromDatabase(String txHash,Integer page, Integer size){
    return ckbTransactionService.getDisplayInputs(txHash,page,size);
  }

  @Override
  public Page<CellOutputResponse> getDisplayOutputs(String txHash,Integer page, Integer size) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:txHash:%s:page:%d:size:%d",
        TRANSACTIONS_OUTPUT_CACHE_PREFIX, CACHE_VERSION, txHash, page, size);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadOutputFromDatabase(txHash, page, size),  // 数据加载函数
        TTL_SECONDS_DETAIL,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private Page<CellOutputResponse> loadOutputFromDatabase(String txHash,Integer page, Integer size){
    return ckbTransactionService.getDisplayOutputs(txHash,page,size);
  }
}