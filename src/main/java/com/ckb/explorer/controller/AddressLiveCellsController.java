package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.AddressLiveCellsPageReq;
import com.ckb.explorer.domain.resp.LiveCellsResponse;
import com.ckb.explorer.facade.ILiveCellsCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
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
  private ILiveCellsCacheFacade liveCellsCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  // 地址的某种资产的livecell列表
  @GetMapping("/{address}")
  @Operation(summary = "获取地址的liveCell列表")
  public ResponseInfo<Page<LiveCellsResponse>> index(@PathVariable @NotNull String address, @Valid AddressLiveCellsPageReq req) {

    if (!queryKeyUtils.isValidHex(address) && !queryKeyUtils.isValidAddress(address)) {
      throw new ServerException(I18nKey.ADDRESS_HASH_INVALID_CODE,
          i18n.getMessage(I18nKey.ADDRESS_HASH_INVALID_MESSAGE));
    }
    // 查询带缓存
    return ResponseInfo.SUCCESS(liveCellsCacheFacade.getAddressLiveCellsByAddress(address, req.getTypeHash() == null ? null : req.getTypeHash().trim(), req.getPage(), req.getPageSize()));

  }
}
