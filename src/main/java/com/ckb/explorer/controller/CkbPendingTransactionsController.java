package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.PendingTransactionPageResponse;
import com.ckb.explorer.facade.ICkbPendingTransactionCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ckb_pending_transactions")
@Validated
public class CkbPendingTransactionsController {

  @Resource
  private ICkbPendingTransactionCacheFacade pendingTransactionCacheFacade;

  /**
   * 查询交易列表
   * @param req
   * @return
   */
  @GetMapping
  @Operation(summary = "获取Pending交易列表")
  public ResponseInfo<Page<PendingTransactionPageResponse>> index(@Valid BasePageReq req) {

    // 查询带缓存
    return ResponseInfo.SUCCESS(pendingTransactionCacheFacade.getPendingTransactionsByPage(req.getPage(), req.getPageSize()));

  }

}
