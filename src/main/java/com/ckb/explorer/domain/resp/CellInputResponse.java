package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellInputResponse 用于将 CellInput 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellInputResponse extends BaseResponse<Long> {

  /**
   * 是否来自cellbase交易
   */
  private Boolean fromCellbase;
  
  /**
   * 单元格容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long capacity;
  
  /**
   * 占用的容量
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long occupiedCapacity;
  
  /**
   * 地址哈希
   */
  private String addressHash;

  /**
   * 目标块高
   */
  private Long targetBlockNumber;
  
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
  private SinceResponse since;
  
  /**
   * 类型脚本
   */
  private ScriptResponse typeScript;
}