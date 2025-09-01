package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.AddressTransactionsPageReq;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.facade.IAddressTransactionCacheFacade;
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


/**
 * AddressTransactionsController 处理地址交易的API请求 对应Ruby代码中的Api::V1::AddressTransactionsController
 */
@RestController
@RequestMapping("/api/v1/address_transactions")
@Validated
public class AddressTransactionsController {

  @Resource
  private IAddressTransactionCacheFacade addressTransactionCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  /**
   * 获取地址的交易列表
   *
   * @param address 地址哈希
   * @param req     请求参数
   * @return 交易列表响应
   */
  @GetMapping("/{address}")
  @Operation(summary = "获取地址的交易列表")
  public ResponseInfo<Page<AddressTransactionPageResponse>> show(
      @PathVariable @NotNull String address,
      @Valid AddressTransactionsPageReq req) {

    // address格式校验
    if(!queryKeyUtils.isValidAddress(address)){
      throw new ServerException("入参必须是正确的地址");
    }

    // 查询地址的交易列表（通过缓存门面）
    Page<AddressTransactionPageResponse> transactions = addressTransactionCacheFacade.getAddressTransactions(
        address, req.getSort(), req.getPage(), req.getPageSize());

    // 构建响应
    ResponseInfo<Page<AddressTransactionPageResponse>> response = ResponseInfo.SUCCESS(
        transactions);

    return response;
  }
}