package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;

import com.ckb.explorer.domain.req.UdtPageReq;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.domain.resp.UdtsListResponse;
import com.ckb.explorer.facade.IUdtCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/udts")
public class UdtsController {



    @Resource
    IUdtCacheFacade iUdtCacheFacade;

    @GetMapping
    @Operation(summary = "UDT列表")
    public ResponseInfo<Page<UdtsListResponse>> index(UdtPageReq req) {
        return ResponseInfo.SUCCESS(iUdtCacheFacade.udtListStatistic(req));
    }


    @GetMapping("/{typeScriptHash}")
    @Operation(summary = "UDT详情")
    public ResponseInfo<UdtDetailResponse> show(@PathVariable String typeScriptHash) {
        return ResponseInfo.SUCCESS(iUdtCacheFacade.findDetailByTypeHash(typeScriptHash));
    }


    @GetMapping("/{typeScriptHash}/holder_allocation")
    @Operation(summary = "Udt holder allocation")
    public ResponseInfo<List<UdtHolderAllocationsResponse>> holderAllocation(@PathVariable String typeScriptHash) {
        return ResponseInfo.SUCCESS(iUdtCacheFacade.findByTypeScriptHash(typeScriptHash));
    }

}
