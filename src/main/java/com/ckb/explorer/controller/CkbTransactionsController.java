package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.TransactionPageReq;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ckb_transactions")
@Validated
public class CkbTransactionsController {

  @Resource
  private ICkbTransactionCacheFacade transactionCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  /**
   * 查询首页交易列表
   * @param pageSize
   * @return
   */
  @GetMapping("/homePage")
  @Operation(summary = "获取首页交易列表")
  public ResponseInfo<List<TransactionPageResponse>> homePage(Integer pageSize) {

    pageSize =(pageSize == null || pageSize < 1) ? 10 : pageSize;
    pageSize = pageSize > 100 ? 100 : pageSize;
    return ResponseInfo.SUCCESS(transactionCacheFacade.getHomePageTransactions(pageSize));
  }

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
  public ResponseInfo<TransactionResponse> show(@PathVariable String txHash) {

    // 校验入参
    if(StringUtils.isEmpty(txHash) || (!queryKeyUtils.isValidHex(txHash))){
      throw new ServerException(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE));
    }
    // 查询带缓存
    return ResponseInfo.SUCCESS(transactionCacheFacade.getTransactionByHash(txHash));
  }

  /**
   * 获取交易输入单元格列表
   * @param txHash 交易哈希
   * @param req  请求
   * @return 分页的单元格输入列表及元数据
   */
  @GetMapping("/{txHash}/display_inputs")
  @Operation(summary = "获取交易输入单元格列表")
  public ResponseInfo<Page<CellInputResponse>> displayInputs(
      @PathVariable String txHash,
      @Valid BasePageReq req) {

    // 校验入参
    if(StringUtils.isEmpty(txHash) || (!queryKeyUtils.isValidHex(txHash))){
      throw new ServerException(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE));
    }

    return ResponseInfo.SUCCESS(transactionCacheFacade.getDisplayInputs(txHash, req.getPage(), req.getPageSize()));
  }

  /**
   * 获取交易输出单元格列表
   * @param txHash 交易哈希
   * @param req 请求
   * @return 分页的单元格输出列表及元数据
   */
  @GetMapping("/{txHash}/display_outputs")
  @Operation(summary = "获取交易输出单元格列表")
  public ResponseInfo<Page<CellOutputResponse>> displayOutputs(
      @PathVariable String txHash,
      @Valid BasePageReq req) {

    // 校验入参
    if(StringUtils.isEmpty(txHash) || (!queryKeyUtils.isValidHex(txHash))){
      throw new ServerException(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE));
    }

    return ResponseInfo.SUCCESS(transactionCacheFacade.getDisplayOutputs(txHash, req.getPage(), req.getPageSize()));
  }

}
