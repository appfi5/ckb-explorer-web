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
 * CellOutputResponse 用于将 CellOutput 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellOutputResponse extends BaseResponse<Long> {
  
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
   * 地址哈希
   */
  private String addressHash;

  /**
   * 目标块高
   */
  private Long targetBlockNumber;
  
  /**
   * 状态
   */
  private Integer status;
  
  /**
   * 消耗的交易哈希
   */
  private String consumedTxHash;
  
  /**
   * 单元格类型
   */
  private String cellType;
  
  /**
   * 生成的交易哈希
   */
  private String generatedTxHash;
  
  /**
   * 单元格索引
   */
  private Integer cellIndex;
  
  /**
   * 类型脚本
   */
  private ScriptResponse typeScript;
}