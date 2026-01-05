package com.ckb.explorer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreOutpointTxHashAndIndexDto {

  /**
   * 生成的交易哈希
   */
  private String generatedTxHash;

  /**
   * 单元格索引
   */
  private Integer cellIndex;
}
