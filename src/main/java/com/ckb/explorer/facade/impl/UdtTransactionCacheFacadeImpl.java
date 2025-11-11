package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.facade.IUdtTransactionCacheFacade;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * UdtTransactionCacheFacadeImpl 实现了 IUdtTransactionCacheFacade 接口，提供UDT交易缓存相关的具体实现
 */
@Component
@Slf4j
public class UdtTransactionCacheFacadeImpl implements IUdtTransactionCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

    @Resource
    private CkbTransactionService transactionService;

    private static final String UDT_TRANSACTIONS_CACHE_PREFIX = "udt:transactions:";
    private static final String CACHE_VERSION = "v1";

    // 缓存 TTL：10 秒
    private static final long TTL_SECONDS = 10;

    @Override
    public Page<UdtTransactionPageResponse> getUdtTransactions(String typeHash, UdtTransactionsPageReq req) {

      req.setSort(StringUtils.defaultIfBlank(req.getSort(), "time.desc"));
      // 创建缓存键
        String cacheKey = String.format("%s%s:%s:sort:%s:page:%d:size:%d:txHash:%s:addressHash:%s",
                UDT_TRANSACTIONS_CACHE_PREFIX, CACHE_VERSION, typeHash, req.getSort(), req.getPage(), req.getPageSize(),req.getTxHash(),req.getAddressHash());

      return cacheUtils.getCache(
          cacheKey,                    // 缓存键
          () -> loadFromDatabase(typeHash,req),  // 数据加载函数
          TTL_SECONDS,                 // 缓存过期时间
          TimeUnit.SECONDS             // 时间单位
      );
    }

    /**
     * 从数据库加载地址的交易列表
     */
    private Page<UdtTransactionPageResponse> loadFromDatabase(String typeScriptHash, UdtTransactionsPageReq req) {
        return transactionService.getUdtTransactions(typeScriptHash, req);
    }
}