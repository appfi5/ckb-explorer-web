package com.ckb.explorer.domain.dto;

import com.ckb.explorer.domain.resp.NervosDaoInfoResponse;
import java.math.BigInteger;
import java.util.List;
import lombok.Data;

@Data
public class CellInputDto {
  private Long id;

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
   * since信息
   */
  private byte[] sinceRaw;

  /**
   * 标签列表
   */
  private List<String> tags;

  private String args;

  private String codeHash;

  private Short hashType;

  private Long transactionId;

  private byte[] data;

  private NervosDaoInfoResponse nervosDaoInfo;

  private Long blockNumber;
}
