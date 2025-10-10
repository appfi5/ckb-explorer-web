package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.resp.CollectionsResp;
import com.ckb.explorer.facade.INftCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
        return ResponseInfo.SUCCESS(iNftCacheFacade.page(req));
    }


}
