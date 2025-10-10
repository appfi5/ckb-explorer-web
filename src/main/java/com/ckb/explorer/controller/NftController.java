package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.req.NftHoldersPageReq;
import com.ckb.explorer.domain.req.NftTransfersPageReq;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.CollectionsResp;
import com.ckb.explorer.domain.resp.NftHolderResp;
import com.ckb.explorer.domain.resp.NftItemResponse;
import com.ckb.explorer.domain.resp.NftTransfersResp;
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


    @GetMapping("/collections/{id}")
    @Operation(summary = "获取collections详情")
    public ResponseInfo<CollectionsResp> show(@PathVariable Long id){
        return ResponseInfo.SUCCESS(iNftCacheFacade.findById(id));
    }

    @GetMapping("/collections/{id}/transfers")
    @Operation(summary = "获取transfers列表")
    public  ResponseInfo<Page<NftTransfersResp>> transfers(@PathVariable Long id,NftTransfersPageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftTransfersPage(id,req));
    }


    @GetMapping("/collections/{id}/holders")
    @Operation(summary = "获取holders列表")
    public  ResponseInfo<Page<NftHolderResp>> transfers(@PathVariable Long id, NftHoldersPageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftHolders(id,req));
    }

    @GetMapping("/collections/{id}/items")
    @Operation(summary = "获取items列表")
    public  ResponseInfo<Page<NftItemResponse>> items(@PathVariable Long id, BasePageReq req){
        return ResponseInfo.SUCCESS(iNftCacheFacade.nftItems(id,req));
    }


}
