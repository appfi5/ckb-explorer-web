package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.req.NftHoldersPageReq;
import com.ckb.explorer.domain.req.NftTransfersPageReq;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.*;
import com.ckb.explorer.facade.INftCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/nft")
public class NftController {

    @Resource
    INftCacheFacade iNftCacheFacade;

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
    public  ResponseInfo<Page<NftTransfersResp>> transfers(@PathVariable String typeScriptHash,NftTransfersPageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftTransfersPage(typeScriptHash,req));
    }


    @GetMapping("/collections/{typeScriptHash}/holders")
    @Operation(summary = "获取holders列表")
    public  ResponseInfo<Page<NftHolderResp>> transfers(@PathVariable String typeScriptHash, NftHoldersPageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftHolders(typeScriptHash,req));
    }

    @GetMapping("/collections/{typeScriptHash}/items")
    @Operation(summary = "获取items列表")
    public  ResponseInfo<Page<NftItemDetailResponse>> items(@PathVariable String typeScriptHash, BasePageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftItems(typeScriptHash,req));
    }


    @GetMapping("/items/{tokenId}")
    @Operation(summary = "获取items详情")
    public  ResponseInfo<Page<NftItemResponse>> items( @PathVariable String tokenId ){

        return ResponseInfo.SUCCESS(iNftCacheFacade.itemInfo(tokenId));
    }


}
