package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Since信息的内部类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SinceResponse {


  /**
   * 原始since值（十六进制）
   */
  private String raw;

  /**
   * 中值时间戳
   */
  private Long medianTimestamp; // 页面不用，不要

}
