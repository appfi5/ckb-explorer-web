package com.ckb.explorer.domain.dto;

import java.util.List;
import lombok.Data;

@Data
public class CellOutputDto {
  private Long id;
  /**
   * 单元格容量
   */
  private Long capacity;

  /**
   * 占用的容量
   */
  private Long occupiedCapacity;

  private byte[] lockCodeHash;

  private byte[] lockArgs;

  private Short lockHashType;

  private Integer status;

  private String consumedTxHash;

  /**
   * 生成的交易哈希
   */
  private String generatedTxHash;

  /**
   * 单元格索引
   */
  private Integer cellIndex;

  /**
   * 单元格类型
   */
  private Integer cellType;

  /**
   * 标签列表
   */
  private List<String> tags;

  private String args;

  private String codeHash;

  private Short hashType;

  private Long transactionId;
}
