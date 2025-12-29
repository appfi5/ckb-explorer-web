package com.ckb.explorer.domain.dto;

import java.math.BigInteger;
import lombok.Data;

@Data
public class PendingCellOutputDto {
  /**
   * 单元格容量
   */
  private BigInteger capacity;

  /**
   * 占用的容量
   */
  private BigInteger occupiedCapacity;

  private byte[] lockCodeHash;

  private byte[] lockArgs;

  private Short lockHashType;

  private byte[] lockScriptHash;

  /**
   * 生成的交易哈希
   */
  private byte[] generatedTxHash;

  /**
   * 单元格索引
   */
  private Integer cellIndex;

  private byte[] typeCodeHash;

  private byte[] typeArgs;

  private Short typeHashType;

  private byte[] typeScriptHash;

  private byte[] data;
}
