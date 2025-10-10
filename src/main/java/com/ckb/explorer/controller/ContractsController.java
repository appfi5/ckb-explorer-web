package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.DaoContractResponse;
import com.ckb.explorer.facade.IContractCacheFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractsController {

  @Resource
  private IContractCacheFacade contractCacheFacade;

  /**
   * 获取Dao合约统计信息
   * @return
   */
  @GetMapping("/nervos_dao")
  public ResponseInfo<DaoContractResponse> nervosDao() {

    return ResponseInfo.SUCCESS(contractCacheFacade.getDaoContract());
  }
}
