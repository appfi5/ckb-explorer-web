package com.ckb.explorer.facade.impl;


import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.domain.resp.UdtsListResponse;
import com.ckb.explorer.facade.IUdtCacheFacade;
import com.ckb.explorer.service.UdtHolderAllocationsService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * UdtCacheFacadeImpl 实现了 IUdtCacheFacade 接口，提供UDT列表缓存相关的具体实现
 */
@Component
@Slf4j
public class UdtCacheFacadeImpl implements IUdtCacheFacade {

    @Resource
    private CacheUtils cacheUtils;

    @Resource
    private UdtHolderAllocationsService udtHolderAllocationsService;

    private static final String UDT_LIST_CACHE_PREFIX = "udt:list:";
    private static final String UDT_HOLDER_ALLOCATIONS_CACHE_PREFIX = "udt:holder:allocations:";
    private static final String UDT_DETAIL_CACHE_PREFIX = "udt:detail:";
    private static final String CACHE_VERSION = "v1";

    // 缓存 TTL：10 秒
    private static final long TTL_SECONDS = 10;


    @Override
    public List<UdtHolderAllocationsResponse> findByTypeScriptHash(String typeScriptHash) {
        String cacheKey = String.format("%s%s:%s", UDT_HOLDER_ALLOCATIONS_CACHE_PREFIX, CACHE_VERSION, typeScriptHash);
        return cacheUtils.getCache(cacheKey,                    // 缓存键
                () -> loadHolderAllocationsResponseFromDataBase(typeScriptHash),  // 数据加载函数
                TTL_SECONDS,                 // 缓存过期时间
                TimeUnit.SECONDS             // 时间单位
        );
    }

    @Override
    public List<UdtsListResponse> udtListStatistic() {
        String cacheKey = String.format("%s%s", UDT_LIST_CACHE_PREFIX, CACHE_VERSION);
        return cacheUtils.getCache(cacheKey,                    // 缓存键
                () -> loadFromDatabase(),  // 数据加载函数
                TTL_SECONDS,                 // 缓存过期时间
                TimeUnit.SECONDS             // 时间单位
        );
    }

    @Override
    public UdtDetailResponse findDetailByTypeHash(String typeHash) {
        String cacheKey = String.format("%s%s:%s", UDT_DETAIL_CACHE_PREFIX, CACHE_VERSION, typeHash);
        return cacheUtils.getCache(cacheKey,                    // 缓存键
                () -> loadDetailFromDatabase(typeHash),  // 数据加载函数
                TTL_SECONDS,                 // 缓存过期时间
                TimeUnit.SECONDS             // 时间单位
        );
    }

    private List<UdtsListResponse> loadFromDatabase() {
        return udtHolderAllocationsService.udtListStatistic();
    }

    private List<UdtHolderAllocationsResponse> loadHolderAllocationsResponseFromDataBase(String typeScriptHash) {
        return udtHolderAllocationsService.findByTypeScriptHash(typeScriptHash);
    }


    private UdtDetailResponse loadDetailFromDatabase(String typeHash) {
        return udtHolderAllocationsService.findDetailByTypeHash(typeHash);
    }

}