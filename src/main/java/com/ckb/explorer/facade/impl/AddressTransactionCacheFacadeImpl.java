package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.AddressTransactionsPageReq;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.facade.IAddressTransactionCacheFacade;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * AddressTransactionCacheFacadeImpl 实现了 IAddressTransactionCacheFacade 接口，提供地址交易缓存相关的具体实现
 */
@Component
@Slf4j
public class AddressTransactionCacheFacadeImpl implements IAddressTransactionCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

    @Resource
    private CkbTransactionService transactionService;

    private static final String ADDRESS_TRANSACTIONS_CACHE_PREFIX = "address:transactions:";
    private static final String CACHE_VERSION = "v1";

    // 缓存 TTL：10 秒
    private static final long TTL_SECONDS = 10;

    @Override
    public Page<AddressTransactionPageResponse> getAddressTransactions(String address, AddressTransactionsPageReq req) {
      var sort = StringUtils.defaultIfBlank(req.getSort(), "time.desc");
      var startTime = req.getStartTime() == null ? "null" : req.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
      var endTime = req.getEndTime() == null ? "null" : req.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
      // 创建缓存键
        String cacheKey = String.format("%s%s:%s:sort:%s:page:%d:size:%d:start:%s:end:%s",
                ADDRESS_TRANSACTIONS_CACHE_PREFIX, CACHE_VERSION, address, sort, req.getPage(), req.getPageSize(),startTime,endTime);

      String finalSort = sort;
      return cacheUtils.getCache(
          cacheKey,                    // 缓存键
          () -> loadFromDatabase(address, finalSort, req.getPage(), req.getPageSize(),req.getStartTime(), req.getEndTime()),  // 数据加载函数
          TTL_SECONDS,                 // 缓存过期时间
          TimeUnit.SECONDS             // 时间单位
      );
    }

    /**
     * 从数据库加载地址的交易列表
     */
    private Page<AddressTransactionPageResponse> loadFromDatabase(String address, String sort, Integer page, Integer pageSize, LocalDate startTime, LocalDate endTime) {
        return transactionService.getAddressTransactions(address, sort, page, pageSize,startTime, endTime);
    }
}