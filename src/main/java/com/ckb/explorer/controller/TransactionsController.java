package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.TransactionPageReq;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@Validated
public class TransactionsController {

  @Resource
  private ICkbTransactionCacheFacade transactionCacheFacade;

  /**
   * 查询交易列表
   * @param req
   * @return
   */
  @GetMapping
  @Operation(summary = "获取交易列表")
  public ResponseInfo<Page<TransactionPageResponse>> index(@Valid TransactionPageReq req) {

    // 查询带缓存
    return ResponseInfo.SUCCESS(transactionCacheFacade.getTransactionsByPage(req.getPage(), req.getPageSize(), req.getSort()));

  }
  
  /**
   * 查询交易详情
   * @param txHash
   * @return
   */
  @GetMapping("/{txHash}")
  @Operation(summary = "获取交易详情")
  public ResponseInfo<TransactionPageResponse> show(@PathVariable String txHash) {

    // 查询带缓存
    return ResponseInfo.SUCCESS(transactionCacheFacade.getTransactionByHash(txHash));
  }
}
