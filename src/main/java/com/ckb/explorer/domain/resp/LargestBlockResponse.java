package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LargestBlockResponse {
  /**
   * 最大区块号
   */
  private Long number;

  /**
   * 最大区块大小
   */
  private Long size;
}
