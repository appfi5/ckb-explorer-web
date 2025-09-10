package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.domain.req.XudtsPageReq;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.XudtsPageResponse;
import com.ckb.explorer.enums.UdtType;
import com.ckb.explorer.mapstruct.UdtsConvert;
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
@RequestMapping("/api/v1/xudts")
public class XudtsController {


    @Resource
    UdtsService udtsService;

    @GetMapping
    @Operation(summary = "XUDT列表")
    public ResponseInfo<Page<XudtsPageResponse>> index(XudtsPageReq req){
        UdtsPageReq pageReq = UdtsConvert.INSTANCE.xudtsPageReqtoUdtsPageReq(req);
        List<Integer> udtTypes = Arrays.asList(UdtType.XUDT.getCode(),UdtType.XUDT_COMPATIBLE.getCode());
        pageReq.setUdtType(udtTypes);
        return  ResponseInfo.SUCCESS(UdtsConvert.INSTANCE.udtsPagetoXudtsPage(udtsService.getUdtsPageBy(pageReq)));
    }


    @GetMapping("/{typeScriptHash}")
    @Operation(summary = "XUDT详情")
    public ResponseInfo<UdtDetailResponse> show(@PathVariable String typeScriptHash){
        return  ResponseInfo.SUCCESS(udtsService.findUdtDetailByTypeScriptHash(typeScriptHash));
    }

}
