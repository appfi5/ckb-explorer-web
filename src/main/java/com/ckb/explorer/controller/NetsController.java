package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.NetInfoResponse;
import com.ckb.explorer.facade.INetInfoCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/nets")
public class NetsController {

    @Resource
    private INetInfoCacheFacade netInfoCacheFacade;

    @GetMapping("/version")
    @Operation(summary = "获取本地节点信息版本号")
    public ResponseInfo<NetInfoResponse> index() {
        NetInfoResponse response = netInfoCacheFacade.getLocalNodeInfoVersion();
        return ResponseInfo.SUCCESS(response);
    }
}