package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.TransactionPageReq;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
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

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

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

    // 校验入参
    if(StringUtils.isEmpty(txHash) || (!queryKeyUtils.isValidHex(txHash))){
      throw new ServerException(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE));
    }
    // 查询带缓存
    return ResponseInfo.SUCCESS(transactionCacheFacade.getTransactionByHash(txHash));
  }
}
