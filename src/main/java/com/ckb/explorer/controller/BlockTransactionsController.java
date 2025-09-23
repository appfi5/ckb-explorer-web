package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.BlockTransactionPageReq;
import com.ckb.explorer.domain.resp.BlockTransactionPageResponse;
import com.ckb.explorer.facade.IBlockTransactionCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * BlockTransactionsController 处理区块内交易的API请求 对应Ruby代码中的Api::V1::BlockTransactionsController
 */
@RestController
@RequestMapping("/api/v1/block_transactions")
public class BlockTransactionsController {

  @Resource
  private IBlockTransactionCacheFacade blockTransactionCacheFacade;

  @Resource
  private I18n i18n;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  /**
   * 显示区块内的交易列表
   *
   * @param blockId 区块哈希或区块号
   * @param req     请求
   * @return 交易列表响应
   */
  @GetMapping("/{id}")
  @Operation(summary = "获取区块内的交易列表")
  public ResponseInfo<Page<BlockTransactionPageResponse>> show(
      @PathVariable("id") String blockId,
      @Valid BlockTransactionPageReq req) {

     // 验证查询参数
    validateQueryParams(blockId, req.getTxHash(), req.getAddressHash());

    // 获取区块内的交易列表
    var transactions = blockTransactionCacheFacade.getBlockTransactions(blockId, req.getTxHash(),
        req.getAddressHash(), req.getPage(), req.getPageSize());

    return ResponseInfo.SUCCESS(transactions);
  }

  /**
   * 验证查询参数
   */
  private void validateQueryParams(String blockHash, String txHash, String addressHash) {
    // 验证区块ID
    if (StringUtils.isEmpty(blockHash) || !queryKeyUtils.isValidHex(blockHash)) {
      throw new ServerException(I18nKey.BLOCK_HASH_INVALID_CODE,
          i18n.getMessage(I18nKey.BLOCK_HASH_INVALID_MESSAGE));
    }

    // 验证交易哈希（如果提供）
    if (txHash != null && !txHash.isEmpty() && !queryKeyUtils.isValidHex(txHash)) {
      throw new ServerException(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_CODE,
          i18n.getMessage(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE));
    }

    // 验证地址哈希（如果提供）
    if (addressHash != null && !addressHash.isEmpty() && !queryKeyUtils.isValidAddress(
        addressHash)) {
      throw new ServerException(I18nKey.ADDRESS_HASH_INVALID_CODE,
          i18n.getMessage(I18nKey.ADDRESS_HASH_INVALID_MESSAGE));
    }
  }
}