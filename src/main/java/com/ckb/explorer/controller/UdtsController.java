package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.SudtsPageReq;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.domain.resp.SudtsPageResponse;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.enums.UdtType;
import com.ckb.explorer.mapstruct.UdtsConvert;
import com.ckb.explorer.service.UdtHolderAllocationsService;
import com.ckb.explorer.service.UdtsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/udts")
public class UdtsController {


    @Resource
    UdtsService udtsService;

    @Resource
    UdtHolderAllocationsService udtHolderAllocationsService;

    @GetMapping
    @Operation(summary = "SUDT列表")
    public ResponseInfo<Page<SudtsPageResponse>> index(SudtsPageReq req){
        UdtsPageReq pageReq = UdtsConvert.INSTANCE.sudtsPageReqtoUdtsPageReq(req);
        List<Integer> udtTypes = Arrays.asList(UdtType.SUDT.getCode());
        pageReq.setUdtType(udtTypes);
        return  ResponseInfo.SUCCESS(UdtsConvert.INSTANCE.udtsPagetoSudtsPage(udtsService.getUdtsPageBy(pageReq)));
    }


    @GetMapping("/{typeScriptHash}")
    @Operation(summary = "SUDT详情")
    public ResponseInfo<UdtDetailResponse> show(@PathVariable String typeScriptHash){
        return  ResponseInfo.SUCCESS(udtsService.findUdtDetailByTypeScriptHash(typeScriptHash));
    }


    @GetMapping("/{typeScriptHash}/holder_allocation")
    @Operation(summary = "Udt holder allocation")
    public ResponseInfo<List<UdtHolderAllocationsResponse>> holderAllocation(@PathVariable String typeScriptHash){
        return  ResponseInfo.SUCCESS(udtHolderAllocationsService.findByTypeScriptHash(typeScriptHash));
    }

}
