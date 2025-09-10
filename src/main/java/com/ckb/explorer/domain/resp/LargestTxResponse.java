package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LargestTxResponse {
  /**
   * 最大交易哈希
   */
  private String txHash;

  /**
   * 最大交易字节数
   */
  private Long bytes;
}
