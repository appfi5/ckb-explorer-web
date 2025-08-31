package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.AddressLiveCellsPageReq;
import com.ckb.explorer.domain.resp.AddressLiveCellsResponse;
import com.ckb.explorer.facade.ICellOutputCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/address_live_cells")
@Validated
public class AddressLiveCellsController {

  @Resource
  private ICellOutputCacheFacade cellOutputCacheFacade;
  @GetMapping("/{address}")
  @Operation(summary = "获取地址的liveCell列表")
  public ResponseInfo<Page<AddressLiveCellsResponse>> index(@PathVariable @NotNull String address, @Valid AddressLiveCellsPageReq req) {

    // 查询带缓存
    return ResponseInfo.SUCCESS(cellOutputCacheFacade.getAddressLiveCellsByAddress(address, req.getTag(), req.getSort(), req.getBoundStatus(), req.getPage(), req.getPageSize()));

  }
}
