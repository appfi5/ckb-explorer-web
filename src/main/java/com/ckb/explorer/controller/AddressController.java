package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import com.ckb.explorer.facade.IScriptCacheFacade;
import com.ckb.explorer.facade.IUdtAccountsCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/addresses")
@Validated
public class AddressController {

  @Resource
  private IScriptCacheFacade scriptCacheFacade;

  @Resource
  private IUdtAccountsCacheFacade udtAccountsCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  @GetMapping("/{address}")
  @Operation(summary = "获取地址详情")
  public ResponseInfo<AddressResponse> show(@PathVariable @NotNull String address) {
    validAddress( address);
    // 查询地址详情
    AddressResponse addressResponse = scriptCacheFacade.getAddressInfo(address);
    // 返回响应
    return ResponseInfo.SUCCESS(addressResponse);
  }

  // 地址UDT的余额 原AddressResponse里的 List<UdtAccountResponse> udtAccounts;
  @GetMapping("/{address}/udts")
  @Operation(summary = "获取地址UDT的余额")
  public ResponseInfo<List<AccountUdtBalanceResponse>> udts(@PathVariable @NotNull String address) {
    validAddress(address);
    List<AccountUdtBalanceResponse> udtBalanceResponses = udtAccountsCacheFacade.getUdtBalance(address);
    return ResponseInfo.SUCCESS(udtBalanceResponses);
  }

  // 地址校验
  private boolean validAddress(String address) {
    // 检查是否为有效的十六进制字符串
    if (!queryKeyUtils.isValidHex(address) && !queryKeyUtils.isValidAddress(address)) {
      throw new ServerException(I18nKey.ADDRESS_HASH_INVALID_CODE,
          i18n.getMessage(I18nKey.ADDRESS_HASH_INVALID_MESSAGE));
    }
    return true;
  }
}