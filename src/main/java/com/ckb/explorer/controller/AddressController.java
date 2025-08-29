package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.facade.IScriptCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/addresses")
@Validated
public class AddressController {
    
    @Resource
    private IScriptCacheFacade scriptCacheFacade;
    
    @GetMapping("/{address}")
    @Operation(summary = "获取地址详情")
    public ResponseInfo<AddressResponse> show(@PathVariable @NotNull String address) {
        
        // 查询地址详情
        AddressResponse addressResponse = scriptCacheFacade.getAddressInfo(address);
        // 返回响应
        return ResponseInfo.SUCCESS(addressResponse);
    }
}