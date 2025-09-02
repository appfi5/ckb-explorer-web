package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellInfoResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellInfoResponse {
  /**
   * 容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long capacity;

  /**
   * 占用的容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long occupiedCapacity;

  /**
   * 状态 0-live;1-dead
   */
  private Integer status;

  /**
   * 单元格索引
   */
  private Integer cellIndex;

  /**
   * 生成的交易哈希
   */
  private String generatedTxHash;

  /**
   * 消耗的交易哈希
   */
  private String consumedTxHash;

  private LockScriptResponse lockScript;

  private String address;

  private TypeScriptResponse typeScript;

  private String data;

}