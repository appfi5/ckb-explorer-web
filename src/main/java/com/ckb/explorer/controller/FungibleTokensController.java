package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.FungibleTokensPageReq;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.domain.resp.FungibleTokensPageResponse;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.enums.UdtType;
import com.ckb.explorer.mapstruct.UdtsConvert;
import com.ckb.explorer.service.UdtsService;
import com.ckb.explorer.util.I18n;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fungible_tokens")
public class FungibleTokensController {

    @Resource
    private I18n i18n;

    @Resource
    UdtsService udtsService;

    @GetMapping
    @Operation(summary = "UDT列表")
    public ResponseInfo<Page<FungibleTokensPageResponse>> index(FungibleTokensPageReq req){
        UdtsPageReq pageReq = UdtsConvert.INSTANCE.fungibletoUdtsPageReq(req);
        List<Integer> udtTypes = Arrays.asList(UdtType.XUDT.getCode(),UdtType.XUDT_COMPATIBLE.getCode(),UdtType.SUDT.getCode(),UdtType.SSRI.getCode());
        pageReq.setPublished(true);
        pageReq.setUdtType(udtTypes);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(Duration.ofMinutes(1)).cachePublic());
        return  ResponseInfo.SUCCESS(UdtsConvert.INSTANCE.udtsPagetoFungibleTokensPage(udtsService.getUdtsPageBy(pageReq)));
    }


    @GetMapping("/{typeScriptHash}")
    @Operation(summary = "UDT详情")
    public ResponseInfo<UdtDetailResponse> show(@PathVariable String typeScriptHash){
        UdtDetailResponse udtDetailResponse = udtsService.findUdtDetailByTypeScriptHash(typeScriptHash);
        if(!udtDetailResponse.getPublished()){
            throw new ServerException(I18nKey.UDTS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.UDTS_NOT_FOUND_MESSAGE));
        }
        return  ResponseInfo.SUCCESS(udtsService.findUdtDetailByTypeScriptHash(typeScriptHash));
    }

}
