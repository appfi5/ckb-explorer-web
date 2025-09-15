package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellDependencyResponse 用于序列化 Cell 依赖信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellDependencyResponse {
  @JsonIgnore
  private Long id;

  private OutPoint outPoint;

  private String depType;
}