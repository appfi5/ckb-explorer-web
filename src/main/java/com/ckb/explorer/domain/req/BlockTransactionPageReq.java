package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlockTransactionPageReq extends BasePageReq {
  /**
   * 交易哈希
   */
  @Schema(description = "交易哈希")
  private String txHash;


  /**
   * 地址哈希
   */
  @Schema(description = "地址")
  private String addressHash;
}
