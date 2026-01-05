package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellInfoResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellInfoResponse extends BaseResponse<Long> {
  /**
   * 容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger capacity;

  /**
   * 占用的容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger occupiedCapacity;

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

  private Integer dataSize;

  private Integer cellType;

  private ExtraInfoResponse extraInfo; // 根据不同资产 cellType 显示不同的信息
}