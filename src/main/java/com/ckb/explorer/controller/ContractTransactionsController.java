package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.ContractTransactionsPageReq;
import com.ckb.explorer.domain.resp.ContractTransactionPageResponse;
import com.ckb.explorer.facade.IContractTransactionCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合约交易控制器 处理合约相关的交易查询请求
 */
@RestController
@RequestMapping("/api/v1/contract_transactions")
public class ContractTransactionsController {

  @Resource
  private IContractTransactionCacheFacade contractTransactionCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  /**
   * 查询合约的交易列表
   *
   * @param req 分页请求参数
   * @return 交易列表响应
   */
  @Operation(summary = "获取Dao交易列表", description = "获取Dao相关的交易列表")
  @GetMapping("/nervos_dao")
  public ResponseInfo<Page<ContractTransactionPageResponse>> getContractTransactions(
      ContractTransactionsPageReq req) {

    // 校验入参
    if (StringUtils.isNotEmpty(req.getTxHash()) && (!queryKeyUtils.isValidHex(req.getTxHash()))) {
      throw new ServerException(I18nKey.PARAMS_INVALID_CODE,
          i18n.getMessage(I18nKey.PARAMS_INVALID_MESSAGE));
    }

    if (StringUtils.isNotEmpty(req.getAddressHash()) && (!queryKeyUtils.isValidAddress(
        req.getAddressHash()))) {
      throw new ServerException(I18nKey.PARAMS_INVALID_CODE,
          i18n.getMessage(I18nKey.PARAMS_INVALID_MESSAGE));
    }
    // 使用缓存门面获取数据
    Page<ContractTransactionPageResponse> transactions =
        contractTransactionCacheFacade.getContractTransactions(req);

    return ResponseInfo.SUCCESS(transactions);
  }
}
