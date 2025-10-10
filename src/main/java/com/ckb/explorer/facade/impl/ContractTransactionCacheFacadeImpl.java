package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.ContractTransactionsPageReq;
import com.ckb.explorer.domain.resp.ContractTransactionPageResponse;
import com.ckb.explorer.facade.IContractTransactionCacheFacade;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ContractTransactionCacheFacadeImpl 实现了 IContractTransactionCacheFacade 接口，提供合约交易缓存相关的具体实现
 */
@Component
@Slf4j
public class ContractTransactionCacheFacadeImpl implements IContractTransactionCacheFacade {

    @Resource
    private CacheUtils cacheUtils;

    @Resource
    private CkbTransactionService transactionService;

    private static final String CONTRACT_TRANSACTIONS_CACHE_PREFIX = "contract:transactions:";
    private static final String CACHE_VERSION = "v1";

    // 缓存 TTL：10 秒
    private static final long TTL_SECONDS = 10;

    @Override
    public Page<ContractTransactionPageResponse> getContractTransactions(ContractTransactionsPageReq req) {
        // 创建缓存键
        String cacheKey = String.format("%s%s:page:%d:size:%d:txHash:%s:addressHash:%s",
                CONTRACT_TRANSACTIONS_CACHE_PREFIX, CACHE_VERSION,
                req.getPage(), req.getPageSize(),
                StringUtils.isEmpty(req.getTxHash()) ? "empty" : req.getTxHash(),
                StringUtils.isEmpty(req.getAddressHash()) ? "empty" : req.getAddressHash());

        return cacheUtils.getCache(
                cacheKey,                    // 缓存键
                () -> loadFromDatabase(req),  // 数据加载函数
                TTL_SECONDS,                 // 缓存过期时间
                TimeUnit.SECONDS             // 时间单位
        );
    }

    /**
     * 从数据库加载合约的交易列表
     */
    private Page<ContractTransactionPageResponse> loadFromDatabase(ContractTransactionsPageReq req) {
        // 调用CkbTransactionService获取合约的交易列表
        return transactionService.getContractTransactions(req);
    }
}