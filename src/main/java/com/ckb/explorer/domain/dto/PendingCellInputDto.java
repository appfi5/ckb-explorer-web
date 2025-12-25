package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class PendingCellInputDto {
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

  private byte[] lockScriptHash;

  /**
   * 生成的交易哈希
   */
  private String generatedTxHash;

  /**
   * 单元格索引
   */
  private Integer cellIndex;

  private Integer inputIndex;

  /**
   * since信息
   */
  private byte[] sinceRaw;

  private byte[] typeCodeHash;

  private byte[] typeArgs;

  private Short typeHashType;

  private byte[] typeScriptHash;

  private byte[] data;
}
