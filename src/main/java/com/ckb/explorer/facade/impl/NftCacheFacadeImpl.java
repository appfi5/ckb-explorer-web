package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.resp.CollectionsResp;
import com.ckb.explorer.facade.INftCacheFacade;
import com.ckb.explorer.mapper.DobExtendMapper;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NftCacheFacadeImpl implements INftCacheFacade {


    private static final String COLLECTION_CACHE_PREFIX = "nft:collections:";
    private static final String CACHE_VERSION = "v1";

    @Resource
    DobExtendMapper dobExtendMapper;
    @Resource
    private CacheUtils cacheUtils;

    // 缓存 TTL：10 秒
    private static final long TTL_SECONDS = 10;

    @Override
    public Page<CollectionsResp> page(CollectionsPageReq req) {
        req.setSort(StringUtils.defaultIfBlank(req.getSort(), "blockTimestamp.desc"));
        String cacheKey = String.format("%s%s:sort:%s:page:%d:size:%d:tags:%s",
                COLLECTION_CACHE_PREFIX, CACHE_VERSION, req.getSort(), req.getPage(), req.getPageSize(), req.getTags());

        return cacheUtils.getCache(
                cacheKey,                    // 缓存键
                () -> loadFromDatabase(req),  // 数据加载函数
                TTL_SECONDS,                 // 缓存过期时间
                TimeUnit.SECONDS             // 时间单位
        );
    }

    Page<CollectionsResp> loadFromDatabase(CollectionsPageReq req) {
        Page<CollectionsResp> collectionsPage = new Page<>(req.getPage(), req.getPageSize());
        String[] sortParts = req.getSort().split("\\.", 2);
        String orderBy = sortParts[0];
        String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";
        Long oneDayAgo = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24);
        collectionsPage = dobExtendMapper.dobPage(collectionsPage, oneDayAgo, orderBy, ascOrDesc, req.getTags());
        return collectionsPage;
    }


}
