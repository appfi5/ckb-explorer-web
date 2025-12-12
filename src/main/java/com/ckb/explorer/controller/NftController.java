package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.req.NftHoldersPageReq;
import com.ckb.explorer.domain.req.NftTransfersPageReq;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.*;
import com.ckb.explorer.facade.INftCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/nft")
public class NftController {

    @Resource
    INftCacheFacade iNftCacheFacade;

    @Resource
    private QueryKeyUtils queryKeyUtils;

    @Resource
    private I18n i18n;

    @GetMapping("/collections")
    @Operation(summary = "获取collections列表")
    public ResponseInfo<Page<CollectionsResp>> collections(CollectionsPageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.collectionsPage(req));
    }


    @GetMapping("/collections/{typeScriptHash}")
    @Operation(summary = "获取collections详情")
    public ResponseInfo<CollectionsResp> show(@PathVariable String typeScriptHash){
        return ResponseInfo.SUCCESS(iNftCacheFacade.findByTypeScriptHash(typeScriptHash));
    }

    @GetMapping("/collections/{typeScriptHash}/transfers")
    @Operation(summary = "获取transfers列表")
    public  ResponseEntity<ResponseInfo<Page<NftTransfersResp>>> transfers(@PathVariable String typeScriptHash,NftTransfersPageReq req){
        validAddress(req.getAddressHash());
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
                .cachePublic() // 允许公共缓存（CDN、代理服务器等）
                .staleWhileRevalidate(10, TimeUnit.MINUTES)
                .staleIfError(60, TimeUnit.MINUTES);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(ResponseInfo.SUCCESS(iNftCacheFacade.nftTransfersPage(typeScriptHash,req)));
    }


    @GetMapping("/collections/{typeScriptHash}/holders")
    @Operation(summary = "获取holders列表")
    public  ResponseInfo<Page<NftHolderResp>> transfers(@PathVariable String typeScriptHash, NftHoldersPageReq req){
        validAddress(req.getAddressHash());
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftHolders(typeScriptHash,req));
    }

    @GetMapping("/collections/{typeScriptHash}/items")
    @Operation(summary = "获取items列表")
    public  ResponseEntity<ResponseInfo<Page<NftItemDetailResponse>>> items(@PathVariable String typeScriptHash, BasePageReq req){
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
                .cachePublic() // 允许公共缓存（CDN、代理服务器等）
                .staleWhileRevalidate(10, TimeUnit.MINUTES)
                .staleIfError(60, TimeUnit.MINUTES);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(ResponseInfo.SUCCESS(iNftCacheFacade.nftItems(typeScriptHash,req)));
    }


    @GetMapping("/collections/{typeScriptHash}/items/{tokenId}")
    @Operation(summary = "获取items详情")
    public  ResponseEntity<ResponseInfo<NftItemResponse>> items(@PathVariable String typeScriptHash, @PathVariable String tokenId ){
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
                .cachePublic() // 允许公共缓存（CDN、代理服务器等）
                .staleWhileRevalidate(10, TimeUnit.MINUTES)
                .staleIfError(60, TimeUnit.MINUTES);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(ResponseInfo.SUCCESS(iNftCacheFacade.itemInfo(typeScriptHash,tokenId)));
    }

    // 地址校验
    private boolean validAddress(String address) {
        if(!StringUtils.hasLength(address)){
            return true;
        }
        // 检查是否为有效的十六进制字符串
        if (!queryKeyUtils.isValidHex(address) && !queryKeyUtils.isValidAddress(address)) {
            throw new ServerException(I18nKey.ADDRESS_HASH_INVALID_CODE,
                    i18n.getMessage(I18nKey.ADDRESS_HASH_INVALID_MESSAGE));
        }
        return true;
    }

    @GetMapping("/storeCell/{tokenId}")
    @Operation(summary = "获取store cell")
    public ResponseInfo<Long> getStoreCellIdByTokenId(@PathVariable String tokenId ){
          return ResponseInfo.SUCCESS(iNftCacheFacade.getStoreCellId(tokenId));
    }



}
